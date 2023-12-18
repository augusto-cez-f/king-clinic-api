package com.kingtest.exceptions;

public class ValidationException extends Exception {
    private String message;

    public ValidationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }    
}
