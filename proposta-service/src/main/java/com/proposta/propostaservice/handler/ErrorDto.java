package com.proposta.propostaservice.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

public class ErrorDto{
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HttpStatus statusCode;

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public ErrorDto(String field, String message, HttpStatus statusCode) {
        this.field = field;
        this.message = message;
        this.statusCode = statusCode;
    }

    public ErrorDto(ErroApiException ex) {
        this.field = ex.getField();
        this.message = ex.getReason();
        this.statusCode = ex.getStatus();
    }
}
