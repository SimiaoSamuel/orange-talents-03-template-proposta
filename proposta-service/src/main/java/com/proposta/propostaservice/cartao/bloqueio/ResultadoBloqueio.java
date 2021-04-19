package com.proposta.propostaservice.cartao.bloqueio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultadoBloqueio {
    private String resultado;

    @JsonCreator
    public ResultadoBloqueio(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }
}
