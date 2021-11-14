package com.university.controller;

import com.university.service.TuberculosisRecognitionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("tuberculosis/recognition")
public class TuberculosisRecognitionController {

    private final TuberculosisRecognitionService tuberculosisRecognitionService;

    public TuberculosisRecognitionController(TuberculosisRecognitionService tuberculosisRecognitionService) {
        this.tuberculosisRecognitionService = tuberculosisRecognitionService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String recognize(final MultipartFile file){
        return tuberculosisRecognitionService.recognize(file);
    }

}
