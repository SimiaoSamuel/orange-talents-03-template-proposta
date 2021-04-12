package com.proposta.propostaservice.handler;

import org.springframework.http.HttpStatus;

public class ErroApiException extends RuntimeException{
    private String field;
    private String reason;
    private HttpStatus status;

    public String getField() {
        return field;
    }

    public String getReason() {
        return reason;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ErroApiException(String field, String reason, HttpStatus status) {
        this.field = field;
        this.reason = reason;
        this.status = status;
    }
}
