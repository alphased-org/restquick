package com.alphased.restquick.crsw.processor;

import com.alphased.restquick.crsw.CRSWContainer;
import com.alphased.restquick.crsw.exception.DifferentOwnerIdException;
import com.alphased.restquick.crsw.exception.EndPointProcessorException;
import com.alphased.restquick.crsw.exception.OutdoorAuthFailedException;
import com.alphased.restquick.crsw.filter.AuthorizationFilter;
import com.alphased.restquick.crsw.model.EndPoint;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Log4j2
@RequiredArgsConstructor
public class EndPointProcessor {

    private final CRSWContainer crswContainer;
    private final AuthorizationFilter authorizationFilter;

    public String apply(String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException {
        checkOwnerId(ownerId);
        if (crswContainer.getWorkerAuthorizationInformation().isHasAuth()) {
            if (!checkAuth(crswContainer.getWorkerAuthorizationInformation())) {
                return "Unauthorized.";
            }
        }
        EndPoint endPoint = crswContainer.getEndPointMapping().get(httpServletRequest.getRequestURI());
        if (endPoint != null) {
            throw new EndPointProcessorException("asd");
        } else {
            return "true";
        }
    }

    private boolean checkAuth(WorkerAuthorizationInformation workerAuthorizationInformation) throws OutdoorAuthFailedException {
        return authorizationFilter.apply(workerAuthorizationInformation);
    }

    private void checkOwnerId(String ownerId) throws DifferentOwnerIdException {
        if (crswContainer.getWorkerInformation().getOwnerId().equals(ownerId)) {
            throw new DifferentOwnerIdException("OwnerId not equals.");
        }
    }
}
