package com.alphased.restquick.crsw.controller;

import com.alphased.restquick.crsw.exception.DifferentOwnerIdException;
import com.alphased.restquick.crsw.exception.EndPointProcessorException;
import com.alphased.restquick.crsw.exception.OutdoorAuthFailedException;
import com.alphased.restquick.crsw.processor.EndPointProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class CRSWController {

    private final EndPointProcessor endPointProcessor;

    private String process(String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException {
        return endPointProcessor.apply(ownerId, httpServletRequest);
    }

    @GetMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public String getEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException {
        return process(ownerId, httpServletRequest);
    }

    @PutMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public String putEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException {
        return process(ownerId, httpServletRequest);
    }

    @PostMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public String postEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws EndPointProcessorException, DifferentOwnerIdException, OutdoorAuthFailedException {
        return process(ownerId, httpServletRequest);
    }

    @DeleteMapping(path = "/{ownerId}/*/*", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public String deleteEP(@PathVariable String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException {
        return process(ownerId, httpServletRequest);
    }


}
