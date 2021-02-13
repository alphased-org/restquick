package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomHeaders {

    private String headerFieldName;
    private Type headerFieldType;
    private boolean required;

}
