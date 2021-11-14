package com.university.service;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TuberculosisRecognitionService {

    private final ComputationGraph preTrainedNet;

    public TuberculosisRecognitionService() {
        try {
            preTrainedNet = ModelSerializer
                    .restoreComputationGraph(ResourceUtils.getFile("classpath:tuber_modelIteration_200_epoch_2.zip"), true);
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

            return output.getDouble(0) > output.getDouble(1)
                    ? "Normal"
                    : "Tuberculosis";
        } catch (Exception e) {
            e.printStackTrace();
            return "Something went wrong";
        }
    }

}
