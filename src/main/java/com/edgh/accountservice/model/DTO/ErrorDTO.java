package com.edgh.accountservice.model.DTO;

public class ErrorDTO {

    private static final String BUSINESS_EXCEPTION = "BusinessException";
    private static final String ERROR = "Error";

    private String errorClass;
    private String errorMessage;

    public ErrorDTO() {
    }

    public ErrorDTO(Exception e) {
        this.errorClass = e.getClass().getSimpleName().equals(BUSINESS_EXCEPTION) ? ERROR : e.getClass().getSimpleName();
        this.errorMessage = e.getMessage() != null ? e.getMessage().replaceAll("'", " ").replace('\"', ' ') : null;
    }

    public String getErrorClass() {
        return errorClass;
    }

    public void setErrorClass(String errorClass) {
        this.errorClass = errorClass;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
