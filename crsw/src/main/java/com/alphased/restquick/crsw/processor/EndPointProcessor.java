package com.alphased.restquick.crsw.processor;

import com.alphased.restquick.crsw.CRSWContainer;
import com.alphased.restquick.crsw.exception.*;
import com.alphased.restquick.crsw.filter.AuthorizationFilter;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class EndPointProcessor {

    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    private final CRSWContainer crswContainer;
    private final AuthorizationFilter authorizationFilter;

    public String apply(String ownerId, HttpServletRequest httpServletRequest) throws DifferentOwnerIdException, EndPointProcessorException, OutdoorAuthFailedException, NotSupportedMethodException, OperationNotFoundException {
        checkOwnerId(ownerId);
        String requestPath = httpServletRequest.getRequestURI().substring(0, ownerId.length() + 1);
        String method = httpServletRequest.getMethod();
        OpenAPI openAPI = crswContainer.getOpenAPI();

        if (crswContainer.getWorkerAuthorizationInformation().isHasAuth()) {
            if (!checkAuth(crswContainer.getWorkerAuthorizationInformation())) {
                return "Unauthorized.";
            }
        }

        Operation operation = findOperation(requestPath, method, openAPI);
        StrategyMapping.STRATEGY.get(method).accept(operation, httpServletRequest);

        if (operation != null) {
            throw new EndPointProcessorException("asd");
        } else {
            return "true";
        }
    }

    private Operation findOperation(String requestPath, String method, OpenAPI openAPI) throws NotSupportedMethodException, OperationNotFoundException {
        Operation operation;
        if (method.equals(GET)) {
            operation = openAPI.getPaths().get(requestPath).getGet();
        } else if (method.equals(PUT)) {
            operation = openAPI.getPaths().get(requestPath).getPut();
        } else if (method.equals(POST)) {
            operation = openAPI.getPaths().get(requestPath).getPost();
        } else if (method.equals(DELETE)) {
            operation = openAPI.getPaths().get(requestPath).getDelete();
        } else {
            throw new NotSupportedMethodException();
        }
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        return operation;
    }

    private boolean checkAuth(WorkerAuthorizationInformation workerAuthorizationInformation) throws OutdoorAuthFailedException {
        return authorizationFilter.apply(workerAuthorizationInformation);
    }

    private void checkOwnerId(String ownerId) throws DifferentOwnerIdException {
        if (crswContainer.getWorkerInformation().getOwnerId().equals(ownerId)) {
            throw new DifferentOwnerIdException("OwnerId not equals.");
        }
    }

    private static class StrategyMapping {

        public static Map<String, BiConsumer<Operation, HttpServletRequest>> STRATEGY = new HashMap<>();

        static {
            STRATEGY.put(GET, StrategyMapping::doCommon);
            STRATEGY.put(DELETE, StrategyMapping::doCommon);
            STRATEGY.put(POST, (operation, httpServletRequest) -> {
                doCommon(operation, httpServletRequest);
                checkAndDeclareRequestBody(operation.getRequestBody(), httpServletRequest);
            });
            STRATEGY.put(PUT, (operation, httpServletRequest) -> {
                doCommon(operation, httpServletRequest);
                checkAndDeclareRequestBody(operation.getRequestBody(), httpServletRequest);
            });
        }

        private static void checkAndDeclareRequestBody(RequestBody requestBody, HttpServletRequest httpServletRequest) {
        }

        private static void doCommon(Operation operation, HttpServletRequest httpServletRequest) {
            checkAndDeclareHeaders(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("header")).collect(Collectors.toList()), httpServletRequest);
            checkAndDeclarePath(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("path")).collect(Collectors.toList()), httpServletRequest);
            checkAndDeclareQueryParams(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("query")).collect(Collectors.toList()), httpServletRequest);
        }

        private static void checkAndDeclareQueryParams(List<Parameter> query, HttpServletRequest httpServletRequest) {
            //TODO kullanılacak olan queryParamsları, header json altına al.
        }

        private static void checkAndDeclarePath(List<Parameter> path, HttpServletRequest httpServletRequest) {
            //TODO kullanılacak olan PathVariableri, pathVariable json altına al.
        }

        private static void checkAndDeclareHeaders(List<Parameter> header, HttpServletRequest httpServletRequest) {
            //TODO kullanılacak olan headerları, header json altına al.
        }
    }
}
