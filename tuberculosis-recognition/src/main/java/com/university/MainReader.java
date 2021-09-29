package com.university;

import org.bytedeco.javacv.FrameFilter;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;

import static com.university.MinstClassifier.*;

public class MainReader {

    public static void main(String[] args) throws Exception {
                //Evaluation
//        MultiLayerNetwork model = MultiLayerNetwork.load(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/model"), true);
//        DataSetIterator testDsi = getDataSetIterator(RESOURCES_FOLDER_PATH+"/testing", N_SAMPLES_TESTING);
//        System.out.print("Evaluating Model...");
//        Evaluation eval = model.evaluate(testDsi);
//        System.out.print(eval.stats());


        normalImages = normalFolder.listFiles();
        tuberculosisImages = tuberculosisFolder.listFiles();
        MultiLayerNetwork model = MultiLayerNetwork.load(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuber2"), true);
        DataSetIterator testDsi = getTuberculosisDataSetIterator(0);
        System.out.print("Evaluating Model...");
        Evaluation eval = model.evaluate(testDsi);
        System.out.print(eval.stats());

    }
}
