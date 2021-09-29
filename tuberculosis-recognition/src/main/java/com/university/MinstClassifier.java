package com.university;

import org.apache.log4j.BasicConfigurator;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;


import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MinstClassifier {

    public static final String RESOURCES_FOLDER_PATH = "/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/mnist_png";
    public static final File OUT_MODEL_FILE = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuber2");

//    public static final int HEIGHT = 28;
//    public static final int WIDTH = 28;

    public static final int HEIGHT = 512;
    public static final int WIDTH = 512;

    public static final int N_SAMPLE_NORMAL = 3500;
    public static final int N_SAMPLE_TUBERCULOSIS = 700;
    public static final int N_SAMPLES_TRAINING = 60000;
    public static final int N_SAMPLES_TESTING = 10000;

//    static File normalFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/mnist_png/training/0");
    static File normalFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuberculosis_data/Normal");
//    static File tuberculosisFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/mnist_png/training/9");
    static File tuberculosisFolder = new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuberculosis_data/Tuberculosis");

    static File[] normalImages = null;
    static File[] tuberculosisImages = null;

//    public static final int N_OUTCOMES = 10;
    public static final int N_OUTCOMES = 2;
    private static long t0 = System.currentTimeMillis();

    public static void main(String[] args) throws IOException {

        normalImages = normalFolder.listFiles();
        tuberculosisImages = tuberculosisFolder.listFiles();

        t0 = System.currentTimeMillis();

        MultiLayerNetwork model = MultiLayerNetwork.load(new File("/home/vladislav/IdeaProjects/University/tuberculosis-recognition/src/main/resources/tuber"), true);
//        buildModel(18, model);
        buildModel();

    }

    private static void buildModel(int i, MultiLayerNetwork model) throws IOException {
            DataSetIterator dsi = getTuberculosisDataSetIterator(i);
            System.out.println("Train Model..." + i);
            model.fit(dsi);

        model.save(OUT_MODEL_FILE);
//        //Evaluation
//        DataSetIterator testDsi = getDataSetIterator(RESOURCES_FOLDER_PATH+"/testing", N_SAMPLES_TESTING);
//        System.out.print("Evaluating Model...");
//        Evaluation eval = model.evaluate(testDsi);
//        System.out.print(eval.stats());
//
        long t1 = System.currentTimeMillis();
        double t = (double)(t1-t0)/1000.0;
        System.out.print("\n\nTotal time: "+t+" seconds");
    }

    private static void buildModel() throws IOException {

        int rngSeed = 123;
        int nEpochs = 2;

        System.out.print("Build Model...");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(new Nesterovs(0.1, 0.9))
                .maxNumLineSearchIterations(10000)
                .l2(0.0001).list()
                .layer(new DenseLayer.Builder()
                        .nIn(HEIGHT*WIDTH*3).nOut(50).activation(Activation.RELU)
                        .weightInit(WeightInit.XAVIER).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(50).nOut(N_OUTCOMES).activation(Activation.SOFTMAX)
                        .weightInit(WeightInit.XAVIER).build())
                .backprop(true)
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();
        //Print score every 500 interaction
        model.setListeners(new ScoreIterationListener(500));
//        for (int i = 0; i < 1; i++) {
            DataSetIterator dsi = getTuberculosisDataSetIterator(0);
            System.out.println("Train Model...");
            model.fit(dsi);
//        }

        DataSetIterator testDsi = getTuberculosisDataSetIterator(0);
        System.out.print("Evaluating Model...");
        Evaluation eval = model.evaluate(testDsi);
        System.out.print(eval.stats());

//        model.save(OUT_MODEL_FILE);
//        //Evaluation
//        DataSetIterator testDsi = getDataSetIterator(RESOURCES_FOLDER_PATH+"/testing", N_SAMPLES_TESTING);
//        System.out.print("Evaluating Model...");
//        Evaluation eval = model.evaluate(testDsi);
//        System.out.print(eval.stats());
//
        long t1 = System.currentTimeMillis();
        double t = (double)(t1-t0)/1000.0;
        System.out.print("\n\nTotal time: "+t+" seconds");
    }

    public static DataSetIterator getTuberculosisDataSetIterator(int i) throws IOException {
        try {
//            NativeImageLoader nativeImageLoader = new NativeImageLoader(HEIGHT, WIDTH); //28x28 512x512
            NativeImageLoader nativeImageLoader = new NativeImageLoader(HEIGHT, WIDTH, 3); //28x28 512x512
            ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0,1); //translate image into seq of 0..1 input values

//            INDArray input = Nd4j.create(new int[]{1+1, HEIGHT*WIDTH});
            INDArray input = Nd4j.create(new int[]{1+1, HEIGHT*WIDTH*3});
            INDArray output = Nd4j.create(new int[]{1+1, N_OUTCOMES});

            int n = 0;
            for (int j = 1*i; j < 1*i + 1; j++) {
                File imgFile = normalImages[i];
                INDArray img = nativeImageLoader.asRowVector(imgFile);
                scaler.transform(img);
                input.putRow(n, img);
                output.put(n, 0, 1.0);
                n++;
            }
            for (int j = 1*i; j < 1*i + 1; j++) {
                File imgFile = tuberculosisImages[i];
                INDArray img = nativeImageLoader.asRowVector(imgFile);
                scaler.transform(img);
                input.putRow(n, img);
                output.put(n, 1, 1.0);
                n++;
            }
//            int n = 0;
//            //scan all 0 to 9 digit subfolders
//            for (File digitFolder: digitFolders) {
//                int labelDigit = Integer.parseInt(digitFolder.getName());
//                File[] imageFiles = digitFolder.listFiles();
//
//                for (File imgFile : imageFiles) {
//                    INDArray img = nativeImageLoader.asRowVector(imgFile);
//                    scaler.transform(img);
//                    input.putRow(n, img);
//                    output.put(n, labelDigit, 1.0);
//                    n++;
//                }
//            }//End of For-loop

            //Joining input and output matrices into a dataset
            DataSet dataSet = new DataSet(input, output);
            //Convert the dataset into a list
            List<DataSet> listDataSet = dataSet.asList();
            //Shuffle content of list randomly
            Collections.shuffle(listDataSet, new Random(System.currentTimeMillis()));
            int batchSize = 1;

            //Build and return a dataset iterator
            DataSetIterator dsi = new ListDataSetIterator<DataSet>(listDataSet, batchSize);
            return dsi;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    } //End of DataIterator Method

    public static DataSetIterator getDataSetIterator(String folderPath, int nSamples) throws IOException {
        try {
            File folder = new File(folderPath);
            File[] digitFolders = folder.listFiles();

            NativeImageLoader nativeImageLoader = new NativeImageLoader(HEIGHT, WIDTH); //28x28
            ImagePreProcessingScaler scaler = new ImagePreProcessingScaler(0,255); //translate image into seq of 0..1 input values

            INDArray input = Nd4j.create(new int[]{nSamples, HEIGHT*WIDTH});
            INDArray output = Nd4j.create(new int[]{nSamples, N_OUTCOMES});

            int n = 0;
            //scan all 0 to 9 digit subfolders
            for (File digitFolder: digitFolders) {
                int labelDigit = Integer.parseInt(digitFolder.getName());
                File[] imageFiles = digitFolder.listFiles();

                for (File imgFile : imageFiles) {
                    INDArray img = nativeImageLoader.asRowVector(imgFile);
                    scaler.transform(img);
                    input.putRow(n, img);
                    output.put(n, labelDigit, 1.0);
                    n++;
                }
            }//End of For-loop

            //Joining input and output matrices into a dataset
            DataSet dataSet = new DataSet(input, output);
            //Convert the dataset into a list
            List<DataSet> listDataSet = dataSet.asList();
            //Shuffle content of list randomly
//            Collections.shuffle(listDataSet, new Random(System.currentTimeMillis()));
            int batchSize = 10;

            //Build and return a dataset iterator
            DataSetIterator dsi = new ListDataSetIterator<DataSet>(listDataSet, batchSize);
            return dsi;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    } //End of DataIterator Method
}