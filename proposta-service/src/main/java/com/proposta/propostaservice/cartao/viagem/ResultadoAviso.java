package com.proposta.propostaservice.cartao.viagem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultadoAviso {
    private String resultado;

    @JsonCreator
    public ResultadoAviso(@JsonProperty("resultado") String resultado) {
        this.resultado = resultado;
    }
}
