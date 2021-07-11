package com.edgh.accountservice.exception;

public class BusinessException extends Exception {

    private String code;
    private String description;

    public BusinessException(String description) {
        this(null, description);
    }

    public BusinessException(String message, String description) {
        this(message, null, description);
    }

    public BusinessException(String message, String code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}