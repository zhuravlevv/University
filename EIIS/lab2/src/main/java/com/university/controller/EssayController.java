package com.university.controller;

import com.university.entity.Essay;
import com.university.service.EssayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EssayController {

    private final EssayService essayService;

    @GetMapping("essay")
    public Essay getEssay(@RequestParam(name = "file") final String fileName) throws Exception {
        return essayService.getEssay(fileName);
    }

}
