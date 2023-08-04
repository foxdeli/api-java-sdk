package com.foxdeli.exception;

public class FoxdeliConnectionException extends RuntimeException {

    public FoxdeliConnectionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
