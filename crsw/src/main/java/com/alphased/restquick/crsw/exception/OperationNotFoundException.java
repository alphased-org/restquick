package com.alphased.restquick.crsw.exception;

public class OperationNotFoundException extends CRSWException {

    public OperationNotFoundException() {
        super("Operation not found for this owner.");
    }
}
