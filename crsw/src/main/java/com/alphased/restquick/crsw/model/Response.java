package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Response {

    private boolean success;
    private String failedMessage;
    private Object body;

    @Builder(builderMethodName = "successResponseBuilder", builderClassName = "SuccessResponseBuilder")
    public Response(Object body) {
        this.success = true;
        this.body = body;
    }

    @Builder(builderMethodName = "failedResponseBuilder", builderClassName = "FailedResponseBuilder")
    public Response(String failedMessage) {
        this.success = false;
        this.failedMessage = failedMessage;
    }

}
