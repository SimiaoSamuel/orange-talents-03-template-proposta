package com.proposta.propostaservice.cartao.bloqueio;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class BloqueioRequest {
    @NotBlank
    private String idCartao;

    @NotBlank
    private String ip;

    @NotBlank
    private String userAgent;

    public BloqueioRequest(@NotBlank String idCartao, @NotBlank String ip, @NotBlank String userAgent) {
        this.idCartao = idCartao;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public BloqueioCartao toBloqueioCartao(){
        return new BloqueioCartao(idCartao,ip,userAgent);
    }
}
