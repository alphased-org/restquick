package com.alphased.restquick.crsw;

import com.alphased.restquick.crsw.exception.LoadErrorCRSWContainerException;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import com.alphased.restquick.crsw.model.WorkerInformation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

public class CRSWContainer {

    private final WorkerInformation workerInformation;

    private WorkerAuthorizationInformation workerAuthorizationInformation;
    private OpenAPI openAPI;

    public CRSWContainer(WorkerInformation workerInformation) throws LoadErrorCRSWContainerException {
        this.workerInformation = workerInformation;
        createCRSWContainer(workerInformation);
    }

    public void reloadContainer() throws LoadErrorCRSWContainerException {
        createCRSWContainer(workerInformation);
    }

    private void createCRSWContainer(WorkerInformation workerInformation) throws LoadErrorCRSWContainerException {
        try {
            openAPI = fetchOpenApiFromOwnerId(workerInformation.getOwnerId());
            workerAuthorizationInformation = fetchAuthorizationInformation(workerInformation.getOwnerId());
        } catch (Exception e) {
            throw new LoadErrorCRSWContainerException(e.getMessage());
        }
    }


    private WorkerAuthorizationInformation fetchAuthorizationInformation(String ownerId) {
        return WorkerAuthorizationInformation.noAuthBuilder().build();
    }

    private OpenAPI fetchOpenApiFromOwnerId(String ownerId) {
        ParseOptions parseOptions = new ParseOptions();
        parseOptions.setResolve(true);
        return new OpenAPIV3Parser().readContents("openapi: 3.0.2\n" +
                "info:\n" +
                "    title: New API\n" +
                "    version: 1.0.0\n" +
                "    description: A brand new API with no content.  Go nuts!\n" +
                "paths:\n" +
                "    /seljukeGames:\n" +
                "        get:\n" +
                "            parameters:\n" +
                "                -\n" +
                "                    name: seljukeId\n" +
                "                    description: ''\n" +
                "                    schema:\n" +
                "                        type: string\n" +
                "                    in: query\n" +
                "                    required: true\n" +
                "            responses:\n" +
                "                '200':\n" +
                "                    content:\n" +
                "                        application/json:\n" +
                "                            schema:\n" +
                "                                $ref: '#/components/schemas/games'\n" +
                "components:\n" +
                "    schemas:\n" +
                "        games:\n" +
                "            description: ''\n" +
                "            type: object").getOpenAPI();
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
