package com.alphased.restquick.crsw.controller;

import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.processor.EndPointProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/generated")
public class CRSWController {

    private final EndPointProcessor endPointProcessor;

    private Response process() throws Exception {
        return endPointProcessor.apply();
    }

    @GetMapping(path = "/**", consumes = MediaType.ALL_VALUE)
    public Response getEP() throws Exception {
        return process();
    }

    @PutMapping(path = "/**", consumes = MediaType.ALL_VALUE)
    public Response putEP() throws Exception {
        return process();
    }

    @PostMapping(path = "/**", consumes = MediaType.ALL_VALUE)
    public Response postEP() throws Exception {
        return process();
    }

    @DeleteMapping(path = "/**", consumes = MediaType.ALL_VALUE)
    public Response deleteEP() throws Exception {
        return process();
    }

}
