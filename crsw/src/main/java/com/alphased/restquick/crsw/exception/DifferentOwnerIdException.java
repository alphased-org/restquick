package com.alphased.restquick.crsw.exception;

public class DifferentOwnerIdException extends CRSWException {

    public DifferentOwnerIdException() {
        super("OwnerId not matched.");
    }
}
