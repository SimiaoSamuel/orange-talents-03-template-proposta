package com.proposta.propostaservice.carteira;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CarteiraFeignResponse {
    private String resultado;
    private String id;

    public String getId() {
        return id;
    }

    @JsonCreator
    public CarteiraFeignResponse(@JsonProperty("resultado") String resultado,
                                 @JsonProperty("id") String id) {
        this.resultado = resultado;
        this.id = id;
    }
}
