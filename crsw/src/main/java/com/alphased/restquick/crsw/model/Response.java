package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;

@Getter
public class Response {

    private final boolean success;
    private String failedMessage;
    private int code;
    private Object body;
    private String bodyType;
    private String exceptionType;
    private String requestPath;

    @Builder(builderMethodName = "successResponseBuilder", builderClassName = "SuccessResponseBuilder")
    public Response(@Nullable Object body) {
        this.success = true;
        this.code = HttpStatus.OK.value();
        this.body = body;
        if (body != null) {
            this.bodyType = body.getClass().getSimpleName();
        }
    }

    @Builder(builderMethodName = "failedResponseBuilder", builderClassName = "FailedResponseBuilder")
    public Response(int code, String failedMessage, @Nullable Object body, String exceptionType, String requestPath) {
        this.success = false;
        this.code = code;
        this.failedMessage = failedMessage;
        this.body = body;
        this.exceptionType = exceptionType;
        this.requestPath = requestPath;
        if (body != null) {
            this.bodyType = body.getClass().getSimpleName();
        }
    }

}
