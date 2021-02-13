package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WorkerAuthorizationInformation {

    private boolean hasAuth;

    private String outdoorEP;
    private String outdoorRefreshEP;

    private String headerTokenFieldName;
    private String headerTokenRefreshFieldName;
    private String requestParamUsernameFieldName;
    private String requestParamPasswordFieldName;

    private boolean headerTokenAuth;
    private boolean requestParamBasicAuth;

    @Builder(builderMethodName = "noAuthBuilder", builderClassName = "noAuthBuilder")
    public WorkerAuthorizationInformation() {
        this.hasAuth = false;
    }


    @Builder(builderMethodName = "headerTokenAuthBuilder", builderClassName = "fromHeaderTokenAuthBuilder")
    public WorkerAuthorizationInformation(String headerTokenFieldName, String headerTokenRefreshFieldName, String outdoorEP, String outdoorRefreshEP) {
        this.hasAuth = true;
        this.headerTokenAuth = true;
        this.headerTokenFieldName = headerTokenFieldName;
        this.headerTokenRefreshFieldName = headerTokenRefreshFieldName;
        this.outdoorEP = outdoorEP;
        this.outdoorRefreshEP = outdoorRefreshEP;
    }

    @Builder(builderMethodName = "requestParamBasicAuthBuilder", builderClassName = "fromRequestParamBasicAuthBuilder")
    public WorkerAuthorizationInformation(String requestParamUsernameFieldName, String requestParamPasswordFieldName, String outdoorEP) {
        this.hasAuth = true;
        this.requestParamBasicAuth = true;
        this.requestParamUsernameFieldName = requestParamUsernameFieldName;
        this.requestParamPasswordFieldName = requestParamPasswordFieldName;
        this.outdoorEP = outdoorEP;
    }

}
