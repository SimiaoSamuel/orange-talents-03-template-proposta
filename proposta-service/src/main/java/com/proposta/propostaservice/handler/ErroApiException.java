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

    /**
     *
     * @param field  campo que aconteceu o erro, caso não tenha um especifico é nulo
     * @param reason  motivo da exceção
     * @param status  status que vai retornar na resposta
     */
    public ErroApiException(String field, String reason, HttpStatus status) {
        this.field = field;
        this.reason = reason;
        this.status = status;
    }
}
