package com.university;

import org.datavec.api.io.filters.BalancedPathFilter;
import org.datavec.api.io.labels.ParentPathLabelGenerator;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.BaseImageLoader;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.recordreader.ImageRecordReader;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.transferlearning.FineTuneConfiguration;
import org.deeplearning4j.nn.transferlearning.TransferLearning;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.deeplearning4j.zoo.PretrainedType;
import org.deeplearning4j.zoo.ZooModel;
import org.deeplearning4j.zoo.model.VGG16;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.university.MinstClassifier.OUT_MODEL_FILE;

public class MainZoo {

    public static final int HEIGHT = 512;
    public static final int WIDTH = 512;
    public static final int N_OUTCOMES = 2;

    //    static File normalFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/mnist_png/training/0");
    static File normalFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuberculosis_data/Normal");
    //    static File tuberculosisFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/mnist_png/training/9");
    static File tuberculosisFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuberculosis_data/Tuberculosis");

    static File[] normalImages = normalFolder.listFiles();
    static File[] tuberculosisImages = tuberculosisFolder.listFiles();

    private static String DATA_PATH = "src/main/resources";
    private static final String SAVING_PATH = DATA_PATH + "/saved/modelIteration_";
    public static final String TRAIN_FOLDER = DATA_PATH + "/train_both";

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MainZoo.class);
    private static final long seed = 12345;

    private static final int EPOCH = 5;
    private static final int BATCH_SIZE = 16;
    private static final int TRAIN_SIZE = 85;
    private static final int NUM_POSSIBLE_LABELS = 2;
    private static final String FREEZE_UNTIL_LAYER = "fc2";

    private static final int SAVING_INTERVAL = 100;

    public static final Random RAND_NUM_GEN = new Random(seed);
    public static final String[] ALLOWED_FORMATS = BaseImageLoader.ALLOWED_FORMATS;
    public static ParentPathLabelGenerator LABEL_GENERATOR_MAKER = new ParentPathLabelGenerator();
    public static BalancedPathFilter PATH_FILTER = new BalancedPathFilter(RAND_NUM_GEN, ALLOWED_FORMATS, LABEL_GENERATOR_MAKER);


    public static void main(String[] args) throws IOException {
        ZooModel zooModel = VGG16.builder().build();

        LOGGER.info("Starting...");
//        ComputationGraph preTrainedNet = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
        ComputationGraph preTrainedNet = ComputationGraph.load(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/vgg16_dl4j_inference.zip"), true);

//        preTrainedNet.save(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/zoo"));
//        INDArray input = Nd4j.create(new int[]{1+1, HEIGHT*WIDTH*3});
//        preTrainedNet.setInputs();
        System.out.println(Arrays.toString(preTrainedNet.getInputs()));
//        preTrainedNet.setInputs(Nd4j.create(new int[]));
        System.out.println("-----------------------------------");
        System.out.println(preTrainedNet.summary());
        System.out.println("-----------------------------------");

        FineTuneConfiguration fineTuneConf = new FineTuneConfiguration.Builder()
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Updater.NESTEROVS)
                .seed(seed)
                .build();

        ComputationGraph vgg16Transfer = new TransferLearning.GraphBuilder(preTrainedNet)
                .fineTuneConfiguration(fineTuneConf)
                .setFeatureExtractor(FREEZE_UNTIL_LAYER)
//                .removeVertexKeepConnections("input_1")
//                .addLayer("input_1",
//                        new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
//                                .nIn(4096).nOut(NUM_POSSIBLE_LABELS)
//                                .weightInit(WeightInit.XAVIER)
//                                .activation(Activation.SOFTMAX).build(), FREEZE_UNTIL_LAYER)
                .removeVertexKeepConnections("predictions")
                .addLayer("predictions",
                        new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                                .nIn(4096).nOut(NUM_POSSIBLE_LABELS)
                                .weightInit(WeightInit.XAVIER)
                                .activation(Activation.SOFTMAX).build(), FREEZE_UNTIL_LAYER)
                .build();

        System.out.println("-----------------------------------");
        System.out.println(vgg16Transfer.summary());
        System.out.println("-----------------------------------");

        File trainData = new File(TRAIN_FOLDER);
        FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, RAND_NUM_GEN);
        InputSplit[] sample = train.sample(PATH_FILTER, 1, 1);
        DataSetIterator trainIterator = getDataSetIterator(sample[0]);

        int iEpoch = 0;
        int i = 0;
        while (iEpoch < EPOCH) {
            while (trainIterator.hasNext()) {
                DataSet trained = trainIterator.next();
                vgg16Transfer.fit(trained);
                if (i % SAVING_INTERVAL == 0 && i != 0) {

                    ModelSerializer.writeModel(vgg16Transfer, new File(SAVING_PATH + i + "_epoch_" + iEpoch + ".zip"), false);
                    evalOn(vgg16Transfer, trainIterator, i);
//                    evalOn(vgg16Transfer, devIterator, i);
                }
                i++;
            }
            trainIterator.reset();
            iEpoch++;

            evalOn(vgg16Transfer, trainIterator, iEpoch);
//            evalOn(vgg16Transfer, testIterator, iEpoch);
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
