package com.proposta.propostaservice.carteira;

import com.proposta.propostaservice.cartao.Cartao;

import javax.validation.constraints.NotBlank;

public class CarteiraRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String carteira;

    public String getEmail() {
        return email;
    }

    public String getCarteira() {
        return carteira.toUpperCase();
    }

    public CarteiraRequest(@NotBlank String email, @NotBlank String carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public Carteira toCarteira(Cartao cartao, String idCarteira){
        return new Carteira(cartao, email, GatewayPagamento.valueOf(carteira.toUpperCase()),idCarteira);
    }
}
