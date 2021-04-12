package com.proposta.propostaservice.handler;

public class ErrorDto {
    private String field;
    private String message;
    private Integer statusCode;

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public ErrorDto(String field, String message, Integer statusCode) {
        this.field = field;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ErrorDto() {
    }
}
