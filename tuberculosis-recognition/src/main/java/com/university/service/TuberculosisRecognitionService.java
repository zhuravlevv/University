package com.university.service;

import org.apache.commons.io.FileUtils;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@Service
public class TuberculosisRecognitionService {

    private final ComputationGraph preTrainedNet;
    private ResourceLoader resourceLoader;

    public TuberculosisRecognitionService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        try {
//            File file = resourceLoader.getClassLoader().getResourceAsStream("classpath:normal/normal-1.jpeg");
//            System.out.println("FILE PATH - " + file.getAbsolutePath());
            InputStream inputStream = this.resourceLoader.getClassLoader().getResourceAsStream("tuber_modelIteration_200_epoch_2.zip");
            File targetFile = new File("tuber-recon.zip");
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            preTrainedNet = ModelSerializer
                    .restoreComputationGraph(targetFile, true);
            preTrainedNet.init();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String recognize(final MultipartFile multipartFile) {
        try {

            NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
            INDArray image = loader.asMatrix(multipartFile.getInputStream());
            DataNormalization scaler = new VGG16ImagePreProcessor();
            scaler.transform(image);
            INDArray output = preTrainedNet.outputSingle(false, image);

//            System.out.println("Normal - " + output.getDouble(0));
//            System.out.println("Tuberculosis - " + output.getDouble(1));

            System.out.println("Result normal - " + output.getDouble(0));
            System.out.println("Result tuberculosis - " + output.getDouble(1));
            return output.getDouble(0) > output.getDouble(1)
                    ? "Normal"
                    : "Tuberculosis";
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong";
        }
    }

}
