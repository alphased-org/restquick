package com.alphased.restquick.crsw.controller;

import com.alphased.restquick.crsw.CRSWContainer;
import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.util.ResponseDispatcher;
import com.alphased.restquick.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class CRSWManagerController {

    private final CRSWContainer crswContainer;

    @GetMapping(path = "/apiDetails/get", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getContainerDetails() throws Exception {
        return ResponseDispatcher.createResponse(OK, JsonUtils.mergedObjectSerialize(crswContainer.getOpenAPI(), crswContainer.getWorkerAuthorizationInformation()));
    }

    @PostMapping(path = "/apiDetails/reload", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response reloadContainer() throws Exception {
        crswContainer.reloadContainer();
        return ResponseDispatcher.createResponse(OK, null);
    }

}
