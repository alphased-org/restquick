package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.ToString;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@ToString
@Builder
public class EndPoint {

    private HttpMethod httpMethod;
    private String path;

    @Singular
    private List<CustomHeaders> customHeaders;

    @Singular
    private List<CustomRequestParams> customRequestParams;

    private String jsonSchemaForRequestBody;

}
