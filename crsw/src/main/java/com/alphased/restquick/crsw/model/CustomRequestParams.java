package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomRequestParams {

    private String requestParamFieldName;
    private Type requestParamFieldType;
    private boolean required;

}
