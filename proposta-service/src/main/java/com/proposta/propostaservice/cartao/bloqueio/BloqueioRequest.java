package com.proposta.propostaservice.cartao.bloqueio;

import com.proposta.propostaservice.cartao.Cartao;

import javax.validation.constraints.NotBlank;

public class BloqueioRequest {
    @NotBlank
    private String ip;

    @NotBlank
    private String userAgent;

    public BloqueioRequest( @NotBlank String ip, @NotBlank String userAgent) {
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public BloqueioCartao toBloqueioCartao(Cartao cartao){
        return new BloqueioCartao(cartao,ip,userAgent);
    }
}
