package com.proposta.propostaservice.cartao;

import java.time.LocalDateTime;

public class CartaoResponse {
    private String id;
    private LocalDateTime emitidoEm;
    private String titular;
    private Integer limite;
    private String idProposta;

    public CartaoResponse(String id, LocalDateTime emitidoEm, String titular, Integer limite, String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.idProposta = idProposta;
    }

    /**
     *
     * @return Cartão com os valores dos atributos mapeados
     */
    public Cartao toCartao(){
        return new Cartao(id,emitidoEm,titular,limite,idProposta);
    }
}

