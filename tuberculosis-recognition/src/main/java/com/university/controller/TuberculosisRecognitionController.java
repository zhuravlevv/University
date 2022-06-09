package com.university.controller;

import com.university.service.TuberculosisRecognitionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> recognize(final MultipartFile file){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        String result = tuberculosisRecognitionService.recognize(file);
        System.out.println(result);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(result);
    }

    @GetMapping
    public ResponseEntity<String> testGet(){
        System.out.println("Got request");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body("Response with header using ResponseEntity");
    }

}
