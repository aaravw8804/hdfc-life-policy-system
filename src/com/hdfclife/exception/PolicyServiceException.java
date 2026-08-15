package com.hdfclife.exception;

public class PolicyServiceException extends RuntimeException {
    public PolicyServiceException(String message) {

        super(message);
    }

    public PolicyServiceException (String message, Throwable cause){
        super(message,cause);
    }
}
