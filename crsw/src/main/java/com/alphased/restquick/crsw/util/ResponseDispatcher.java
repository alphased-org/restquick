package com.alphased.restquick.crsw.util;

import com.alphased.restquick.crsw.model.Response;
import com.alphased.restquick.utils.JsonUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nullable;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResponseDispatcher {

    private static final Map<Class<? extends InternalResponse>, Function<InternalResponse, Response>> statusStrategyMap = new HashMap<>();

    static {
        statusStrategyMap.put(SuccessResponse.class, (internalResponse) -> {
            SuccessResponse successResponse = (SuccessResponse) internalResponse;
            return Response.successResponseBuilder().body(successResponse.getBody()).bodyType(successResponse.getBodyType()).build();
        });
        statusStrategyMap.put(FailureResponse.class, (internalResponse) -> {
            FailureResponse failureResponse = (FailureResponse) internalResponse;
            return Response.failedResponseBuilder()
                    .body(failureResponse.getBody())
                    .bodyType(failureResponse.getBodyType())
                    .code(failureResponse.getCode())
                    .failedMessage(failureResponse.getFailedMessage())
                    .exceptionType(failureResponse.getExceptionType())
                    .requestPath(failureResponse.getRequestPath())
                    .build();
        });
    }

    @SneakyThrows
    private static Response applyStrategy(InternalResponse internalResponse) {
        convertBody(internalResponse);
        return statusStrategyMap.getOrDefault(Class.forName(internalResponse.getClass().getName()), (_internalResponse) -> Response.successResponseBuilder().body(internalResponse.getBody()).build()).apply(internalResponse);
    }

    public static Response successResponse(@Nullable Object body) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        SuccessResponse successResponse = SuccessResponse.builder()
                .body(body)
                .code(HttpStatus.OK.value())
                .requestPath(request.getRequestURI())
                .build();
        return applyStrategy(successResponse);
    }

    public static Response failureResponse(HttpStatus httpStatus, @Nullable Exception e, @Nullable Object body) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        FailureResponse failureResponse = FailureResponse.builder()
                .exceptionType(e.getClass().getName())
                .body(body)
                .failedMessage(e.getMessage() != null ? e.getMessage() : httpStatus.getReasonPhrase())
                .requestPath(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI).toString())
                .code(httpStatus.value())
                .build();
        return applyStrategy(failureResponse);
    }

    private static void convertBody(InternalResponse internalResponse) {
        Object body = internalResponse.getBody();
        if(body != null) {
            if (body instanceof ObjectNode) {
                internalResponse.setBody(body);
                internalResponse.setBodyType(Response.BodyType.JSON);
            } else if (body instanceof Number) {
                internalResponse.setBody(body);
                internalResponse.setBodyType(Response.BodyType.NUMBER);
            } else if (body instanceof String) {
                try {
                    internalResponse.setBody(JsonUtils.jsonNodeCreator((String) body));
                    internalResponse.setBodyType(Response.BodyType.JSON);
                } catch (Exception exception) {
                    internalResponse.setBody(body);
                    internalResponse.setBodyType(Response.BodyType.TEXT);
                }
            } else {
                try {
                    internalResponse.setBody(JsonUtils.serializeToJsonNode(body));
                    internalResponse.setBodyType(Response.BodyType.JSON);
                } catch (Exception exception) {
                    internalResponse.setBody(body);
                    internalResponse.setBodyType(Response.BodyType.UNKNOWN);
                }
            }
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    private static class SuccessResponse extends InternalResponse {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder
    private static class FailureResponse extends InternalResponse {
        private String exceptionType;
        private String failedMessage;
    }

    @Getter
    @Setter
    @SuperBuilder
    private static abstract class InternalResponse {
        private int code;
        private Object body;
        private Response.BodyType bodyType;
        private String requestPath;
    }
}
