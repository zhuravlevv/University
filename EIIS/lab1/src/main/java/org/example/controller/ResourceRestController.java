package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.models.ResourceStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ResourceRestController {

    private final ResourceStore resourceStore;

    @GetMapping("/resource")
    public List<String> findByRequest(@RequestParam(name = "request") final String request){
        return resourceStore.findMostSuitable(request);
    }

}
