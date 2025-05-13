package com.uniedu.support.processing.exceptions;

public class MinioOperationException extends RuntimeException {
    public MinioOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}