package com.university.train;

import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class TrainTuberImageNetVG16 {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainTuberImageNetVG16.class);

    public static final long seed = 12345;
    public static final Random RAND_NUM_GEN = new Random(seed);
    public static final String[] ALLOWED_FORMATS = BaseImageLoader.ALLOWED_FORMATS;
    public static ParentPathLabelGenerator LABEL_GENERATOR_MAKER = new ParentPathLabelGenerator();
    public static BalancedPathFilter PATH_FILTER = new BalancedPathFilter(RAND_NUM_GEN, ALLOWED_FORMATS, LABEL_GENERATOR_MAKER);

    private static final int EPOCH = 5;
    private static final int BATCH_SIZE = 16;
    private static final int TRAIN_SIZE = 85;
    public static final int NUM_POSSIBLE_LABELS = 2;

    private static final int SAVING_INTERVAL = 100;

    public static String DATA_PATH = "resources";
    public static final String TRAIN_FOLDER = DATA_PATH + "/train_tuber";
    public static final String TEST_FOLDER = DATA_PATH + "/test_tuber";
    private static final String SAVING_PATH = DATA_PATH + "/saved/tuber_modelIteration_";

    public static final String FREEZE_UNTIL_LAYER = "fc2";

    public static void main(String[] args) throws IOException {
        LOGGER.info("Start Downloading VGG16 model...");
        ComputationGraph preTrainedNet = ComputationGraph.load(new File("classpath:vgg16_dl4j_inference.zip"), true);
//        ComputationGraph preTrainedNet = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
        LOGGER.info(preTrainedNet.summary());

        LOGGER.info("Start Downloading Data...");

//        downloadAndUnzipDataForTheFirstTime();
        LOGGER.info("Data unzipped");
        // Define the File Paths
        File trainData = new File(TRAIN_FOLDER);
        File testData = new File(TEST_FOLDER);
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, RAND_NUM_GEN);
        FileSplit test = new FileSplit(testData, NativeImageLoader.ALLOWED_FORMATS, RAND_NUM_GEN);

        //вычитает все файлы из test_both чередуя каждый класс (cat124, dog42, cat9, dog 241...)
        //85% пойдет на trainIterator
        //15% на devIterator
        InputSplit[] sample = train.sample(PATH_FILTER, TRAIN_SIZE, 100 - TRAIN_SIZE);
        DataSetIterator trainIterator = getDataSetIterator(sample[0]);
        DataSetIterator devIterator = getDataSetIterator(sample[1]);


        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                .seed(seed)
                .build();

        ComputationGraph vgg16Transfer = new TransferLearning.GraphBuilder(preTrainedNet)
                .fineTuneConfiguration(fineTuneConf)
                .setFeatureExtractor(FREEZE_UNTIL_LAYER)
                .removeVertexKeepConnections("predictions")
                .addLayer("predictions",
                        new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                                .nIn(4096).nOut(NUM_POSSIBLE_LABELS)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.SOFTMAX).build(), FREEZE_UNTIL_LAYER)
                .build();
        vgg16Transfer.setListeners(new ScoreIterationListener(5));
        LOGGER.info(vgg16Transfer.summary());

        DataSetIterator testIterator = getDataSetIterator(test.sample(PATH_FILTER, 1, 0)[0]);
        int iEpoch = 0;
        int i = 0;
        while (iEpoch < EPOCH) {
            while (trainIterator.hasNext()) {
                System.out.println("Internal iteration - " + i);
                DataSet trained = trainIterator.next();
                vgg16Transfer.fit(trained);
                if (i % SAVING_INTERVAL == 0 && i != 0) {

                    ModelSerializer.writeModel(vgg16Transfer, new File(SAVING_PATH + i + "_epoch_" + iEpoch + ".zip"), false);
                    evalOn(vgg16Transfer, devIterator, i);
                }
                i++;
            }
            trainIterator.reset();
            iEpoch++;

            evalOn(vgg16Transfer, testIterator, iEpoch);
        }
    }

    public static void evalOn(ComputationGraph vgg16Transfer, DataSetIterator testIterator, int iEpoch) throws IOException {
        LOGGER.info("Evaluate model at iteration " + iEpoch + " ....");
        Evaluation eval = vgg16Transfer.evaluate(testIterator);
        LOGGER.info(eval.stats());
        testIterator.reset();

    }

    public static DataSetIterator getDataSetIterator(InputSplit sample) throws IOException {
        ImageRecordReader imageRecordReader = new ImageRecordReader(224, 224, 3, LABEL_GENERATOR_MAKER);
        imageRecordReader.initialize(sample);

        DataSetIterator iterator = new RecordReaderDataSetIterator(imageRecordReader, BATCH_SIZE, 1, NUM_POSSIBLE_LABELS);
        iterator.setPreProcessor(new VGG16ImagePreProcessor());
        return iterator;
    }


}
