package com.alphased.restquick.crsw.processor;

import com.alphased.restquick.crsw.CRSWContainer;
import com.alphased.restquick.crsw.exception.*;
import com.alphased.restquick.crsw.filter.AuthorizationFilter;
import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import com.alphased.restquick.crsw.util.ResponseDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
@RequiredArgsConstructor
public class EndPointProcessor {

    // available operation methods.
    public final static String GET = "GET";
    public final static String POST = "POST";
    public final static String PUT = "PUT";
    public final static String DELETE = "DELETE";

    private final CRSWContainer crswContainer;
    private final AuthorizationFilter authorizationFilter;

    private final JsonNodeFactory jsonNodeFactory = JsonNodeFactory.withExactBigDecimals(false);

    public Response apply(String ownerId, HttpServletRequest httpServletRequest) throws Exception {
        checkOwnerId(ownerId);
        String requestPath = httpServletRequest.getRequestURI().substring(ownerId.length() + 5);
        String method = httpServletRequest.getMethod();
        JsonNode allVariables = new ObjectNode(jsonNodeFactory);
        OpenAPI openAPI = crswContainer.getOpenAPI();
        Operation operation = findOperation(requestPath, method, openAPI);
        preHandleRequest(allVariables, openAPI, operation);
        handleRequest(httpServletRequest, method, operation, allVariables);
        // TODO: DoAction for operation. Response Type will be String and Json Format.
        Map body = Map.of("asd", "asd");
        // TODO: checkResponse method will be create there also comparing with operation response modal.
        postHandleRequest(body, method, operation, openAPI.getComponents().getSchemas());
        return ResponseDispatcher.successResponse(body);
    }

    private void checkOwnerId(String ownerId) throws DifferentOwnerIdException {
        if (crswContainer.getWorkerInformation().getOwnerId().equals(ownerId)) {
            throw new DifferentOwnerIdException();
        }
    }

    private Operation findOperation(String requestPath, String method, OpenAPI openAPI) throws NotSupportedMethodException, OperationNotFoundException {
        Operation operation = null;
        try {
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
        } catch (Exception e) {
            if (e instanceof NotSupportedMethodException) {
                throw e;
            }
            log.debug(e.getMessage());
        }
        if (operation == null) {
            throw new OperationNotFoundException();
        }
        return operation;
    }

    private void preHandleRequest(JsonNode allVariables, OpenAPI openAPI, Operation operation) throws UnAuthorizedException {
        checkSecurity(openAPI.getSecurity(), operation.getSecurity());
        checkOutdoorAuthorization(allVariables);
    }

    private void handleRequest(HttpServletRequest httpServletRequest, String method, Operation operation, JsonNode allVariables) {
        MethodStrategyMapping.STRATEGY.get(method).accept(operation, httpServletRequest, allVariables);
    }

    private void postHandleRequest(Object body, String method, Operation operation, Map<String, Schema> schemas) {
    }

    private void checkSecurity(List<SecurityRequirement> openAPISecurity, List<SecurityRequirement> operationSecurity) {
    }

    private void checkOutdoorAuthorization(JsonNode allVariables) throws UnAuthorizedException {
        WorkerAuthorizationInformation workerAuthorizationInformation = crswContainer.getWorkerAuthorizationInformation();
        if (workerAuthorizationInformation.isHasAuth()) {
            try {
                if (!(checkAuth(workerAuthorizationInformation))) {
                    throw new UnAuthorizedException();
                }
            } catch (OutdoorAuthFailedException e) {
                throw new UnAuthorizedException();
            }
        }
    }

    private boolean checkAuth(WorkerAuthorizationInformation workerAuthorizationInformation) throws OutdoorAuthFailedException {
        return authorizationFilter.apply(workerAuthorizationInformation);
    }

    private static class MethodStrategyMapping {
        public static Map<String, TriConsumer<Operation, HttpServletRequest, JsonNode>> STRATEGY = new HashMap<>();

        static {
            STRATEGY.put(GET, MethodStrategyMapping::doCommon);
            STRATEGY.put(DELETE, MethodStrategyMapping::doCommon);
            STRATEGY.put(POST, (operation, httpServletRequest, allVariables) -> {
                doCommon(operation, httpServletRequest, allVariables);
                checkAndDeclareRequestBody(operation.getRequestBody(), httpServletRequest, allVariables);
            });
            STRATEGY.put(PUT, (operation, httpServletRequest, allVariables) -> {
                doCommon(operation, httpServletRequest, allVariables);
                checkAndDeclareRequestBody(operation.getRequestBody(), httpServletRequest, allVariables);
            });
        }

        private static void checkAndDeclareRequestBody(RequestBody requestBody, HttpServletRequest httpServletRequest, JsonNode allVariables) {
        }

        private static void doCommon(Operation operation, HttpServletRequest httpServletRequest, JsonNode allVariables) {
            checkAndDeclareHeaders(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("header")).collect(Collectors.toList()), httpServletRequest, allVariables);
            checkAndDeclarePath(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("path")).collect(Collectors.toList()), httpServletRequest, allVariables);
            checkAndDeclareQueryParams(operation.getParameters().stream().filter(parameter -> parameter.getIn().equals("query")).collect(Collectors.toList()), httpServletRequest, allVariables);
        }

        private static void checkAndDeclareQueryParams(List<Parameter> query, HttpServletRequest httpServletRequest, JsonNode allVariables) {
            /**
             * QueryParams karşılaştırması yap.
             * Auth kontrolü varsa eğer eşleşmeyi kontrol et.
             * Bütün headerları key, value şeklinde sakla.
             */
        }

        private static void checkAndDeclarePath(List<Parameter> path, HttpServletRequest httpServletRequest, JsonNode allVariables) {
            /**
             * Path variable karşılaştırması yap.
             * Auth kontrolü varsa eğer eşleşmeyi kontrol et.
             * Bütün headerları key, value şeklinde sakla.
             */
        }

        private static void checkAndDeclareHeaders(List<Parameter> header, HttpServletRequest httpServletRequest, JsonNode allVariables) {
            /**
             * Header karşılaştırması yap.
             * Auth kontrolü varsa eğer eşleşmeyi kontrol et.
             * Bütün headerları key, value şeklinde sakla.
             */
        }
    }
}
