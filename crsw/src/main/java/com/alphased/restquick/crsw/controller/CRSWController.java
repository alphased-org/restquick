package com.alphased.restquick.crsw.controller;

import com.alphased.restquick.crsw.exception.CRSWException;
import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.processor.EndPointProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CRSWController {

    private final EndPointProcessor endPointProcessor;

    private Response process(String ownerId, HttpServletRequest httpServletRequest) throws CRSWException {
        return endPointProcessor.apply(ownerId, httpServletRequest);
    }

    @GetMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public Response getEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws CRSWException {
        return process(ownerId, httpServletRequest);
    }

    @PutMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public Response putEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws CRSWException {
        return process(ownerId, httpServletRequest);
    }

    @PostMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public Response postEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws CRSWException {
        return process(ownerId, httpServletRequest);
    }

    @DeleteMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public Response deleteEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws CRSWException {
        return process(ownerId, httpServletRequest);
    }

}
