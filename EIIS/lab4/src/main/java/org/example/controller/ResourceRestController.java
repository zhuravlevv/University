package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.ResourceStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResourceRestController {

    private final ResourceStore resourceStore;

    @GetMapping
    public String getLanguage(@RequestParam(name = "request") final String file) throws Exception{
        return resourceStore.getLanguage(file);
    }

}
