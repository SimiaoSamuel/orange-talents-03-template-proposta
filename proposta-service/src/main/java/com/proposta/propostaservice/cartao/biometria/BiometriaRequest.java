package com.proposta.propostaservice.cartao.biometria;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.proposta.propostaservice.annotation.isBase64;
import com.proposta.propostaservice.cartao.Cartao;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {
    @NotBlank
    @isBase64
    private String biometria;

    @JsonCreator
    public BiometriaRequest(@JsonProperty("biometria") String biometria) {
        this.biometria = biometria;
    }

    public Biometria toBiometria(Cartao cartao) {
        return new Biometria(biometria, cartao);
    }
}
