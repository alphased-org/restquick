package com.alphased.restquick.crsw.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class WorkerInformation {

    private String ownerId;
    private long createdDate;
    private String workerPlan;

}
