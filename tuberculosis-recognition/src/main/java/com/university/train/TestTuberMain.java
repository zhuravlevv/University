package com.university.train;

import org.datavec.api.split.FileSplit;
import org.datavec.api.split.InputSplit;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;

import java.io.File;

import static com.university.train.TrainTuberImageNetVG16.*;


public class TestTuberMain {

    public static void main(String[] args) {

        try {
            ComputationGraph preTrainedNet = ModelSerializer
                    .restoreComputationGraph(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuber_modelIteration_200_epoch_2.zip"), true);
            String trainFolder = "/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/test_tuber";
            File trainData = new File(trainFolder);
            FileSplit train = new FileSplit(trainData, NativeImageLoader.ALLOWED_FORMATS, RAND_NUM_GEN);

            InputSplit[] sample = train.sample(PATH_FILTER, 35, 35);

            DataSetIterator devIterator = getDataSetIterator(sample[1]);

            preTrainedNet.init();
            evalOn(preTrainedNet, devIterator, 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
