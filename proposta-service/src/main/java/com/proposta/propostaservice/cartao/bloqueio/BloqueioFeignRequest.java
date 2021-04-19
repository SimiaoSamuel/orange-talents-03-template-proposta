package com.proposta.propostaservice.cartao.bloqueio;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BloqueioFeignRequest {
    private String sistemaResponsavel;

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }

    @JsonCreator
    public BloqueioFeignRequest(@JsonProperty("sistemaResponsavel") String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }
}
