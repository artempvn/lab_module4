package com.epam.esm.exception;

public class ResourceIsBoundException extends ResourceException {

    public ResourceIsBoundException(String message, Long resourceId) {
        super(message, resourceId);
    }
}
