package com.alphased.restquick.crsw.filter;

import com.alphased.restquick.crsw.exception.OutdoorAuthFailedException;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

@Component
@Log4j2
public class AuthorizationFilter {

    private RestTemplate restTemplate = new RestTemplate();

    public boolean apply(WorkerAuthorizationInformation workerAuthorizationInformation) throws OutdoorAuthFailedException {
        return true;
    }
}
