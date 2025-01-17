package com.proposta.propostaservice.cartao.viagem;

import com.proposta.propostaservice.cartao.Cartao;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoRequest {
    @NotBlank
    private String destino;

    @NotNull
    @FutureOrPresent
    private LocalDate validoAte;

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }

    public AvisoRequest(String destino, LocalDate terminaEm) {
        this.destino = destino;
        this.validoAte = terminaEm;
    }

    public AvisoViagem toAvisoViagem(Cartao cartao, String ip, String userAgent){
        return new AvisoViagem(cartao,destino, validoAte,ip,userAgent);
    }
}
