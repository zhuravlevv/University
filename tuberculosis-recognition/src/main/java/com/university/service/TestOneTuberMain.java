package com.university.service;

//import org.datavec.api.split.FileSplit;
//import org.datavec.api.split.InputSplit;
//import org.datavec.image.loader.NativeImageLoader;
//import org.deeplearning4j.nn.graph.ComputationGraph;
//import org.deeplearning4j.util.ModelSerializer;
//import org.nd4j.linalg.api.ndarray.INDArray;
//import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
//import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
//import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
//import org.slf4j.Logger;
//import ramo.klevis.ml.vg16.PetType;
//import ramo.klevis.ml.vg16.TrainImageNetVG16;
//
//import java.io.File;
//import java.io.FileInputStream;
//
//import static ramo.klevis.ml.vg16.TrainImageNetVG16.*;

public class TestOneTuberMain {

//    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TrainImageNetVG16.class);
//    private static final int TRAIN_SIZE = 95;
//
//    public static void main(String[] args) throws Exception {
//        LOGGER.info("Start Downloading VGG16 model...");
//        ComputationGraph preTrainedNet = ModelSerializer
//                .restoreComputationGraph(new File("C:\\Jala\\CatAndDogRecognizer-master\\resources\\saved\\tuber_modelIteration_100_epoch_.zip"), true);
////        ComputationGraph preTrainedNet = (ComputationGraph) zooModel.initPretrained(PretrainedType.IMAGENET);
//        LOGGER.info(preTrainedNet.summary());
//        preTrainedNet.init();
//
////        String TRAIN_FOLDER = DATA_PATH + "/train_tuber/Normal/Normal-1.png";
//        String TRAIN_FOLDER = DATA_PATH + "/train_tuber/Tuberculosis/Tuberculosis-14.png";
////        String TRAIN_FOLDER = DATA_PATH + "/tuber-test2.jpeg";
//        File file = new File(TRAIN_FOLDER);
//
//        NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
//        INDArray image = loader.asMatrix(new FileInputStream(file));
//        DataNormalization scaler = new VGG16ImagePreProcessor();
//        scaler.transform(image);
//        INDArray output = preTrainedNet.outputSingle(false, image);
//        System.out.println("Normal - " + output.getDouble(0));
//        System.out.println("Tuberculosis - " + output.getDouble(1));
//
//    }
}
