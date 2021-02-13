package com.alphased.restquick.crsw;

import com.alphased.restquick.crsw.model.EndPoint;
import com.alphased.restquick.crsw.model.WorkerAuthorizationInformation;
import com.alphased.restquick.crsw.model.WorkerInformation;

import java.util.HashMap;

public class CRSWContainer {

    private final WorkerInformation workerInformation;
    private final HashMap<String, EndPoint> endPointMapping;
    private final WorkerAuthorizationInformation workerAuthorizationInformation;

    public CRSWContainer(WorkerInformation workerInformation) {
        this.workerInformation = workerInformation;
        endPointMapping = fetchEndPoint(workerInformation.getOwnerId());
        workerAuthorizationInformation = fetchAuthorizationInformation(workerInformation.getOwnerId());
    }

    private WorkerAuthorizationInformation fetchAuthorizationInformation(String ownerId) {
        return WorkerAuthorizationInformation.noAuthBuilder().build();
    }

    private HashMap<String, EndPoint> fetchEndPoint(String ownerId) {
        return new HashMap<>();
    }

    public WorkerInformation getWorkerInformation() {
        return workerInformation;
    }

    public HashMap<String, EndPoint> getEndPointMapping() {
        return endPointMapping;
    }

    public WorkerAuthorizationInformation getWorkerAuthorizationInformation() {
        return workerAuthorizationInformation;
    }
}
