package com.alphased.restquick.crsw;

import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import com.alphased.restquick.crsw.model.WorkerInformation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

public class CRSWContainer {

    private final WorkerInformation workerInformation;
    private final WorkerAuthorizationInformation workerAuthorizationInformation;
    private final OpenAPI openAPI;

    public CRSWContainer(WorkerInformation workerInformation) {
        this.workerInformation = workerInformation;
        openAPI = fetchOpenApiFromOwnerId(workerInformation.getOwnerId());
        workerAuthorizationInformation = fetchAuthorizationInformation(workerInformation.getOwnerId());
    }

    private WorkerAuthorizationInformation fetchAuthorizationInformation(String ownerId) {
        return WorkerAuthorizationInformation.noAuthBuilder().build();
    }

    private OpenAPI fetchOpenApiFromOwnerId(String ownerId) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        return new OpenAPIV3Parser().readContents("").getOpenAPI();
    }

    public WorkerInformation getWorkerInformation() {
        return workerInformation;
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public WorkerAuthorizationInformation getWorkerAuthorizationInformation() {
        return workerAuthorizationInformation;
    }
}
