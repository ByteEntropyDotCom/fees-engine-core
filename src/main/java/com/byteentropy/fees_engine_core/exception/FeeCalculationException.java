package com.byteentropy.fees_engine_core.exception;

public class FeeCalculationException extends RuntimeException {
    private final String errorCode;

    public FeeCalculationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}