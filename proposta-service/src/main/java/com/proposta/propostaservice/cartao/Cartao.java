package com.proposta.propostaservice.cartao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Cartao {
    @Id
    private String id;
    @Column(nullable = false)
    private LocalDateTime emitidoEm;

    @Column(nullable = false)
    private String titular;

    @Column(nullable = false)
    private Integer limite;

    @Column(nullable = false)
    private String idProposta;

    public String getId() {
        return id;
    }

    /**
     *
     * @param id id retornado do cartão
     * @param emitidoEm data de emissão do cartão
     * @param titular nome do titular do cartão
     * @param limite saldo limite
     * @param idProposta id da proposta enviada
     */
    public Cartao(String id, LocalDateTime emitidoEm, String titular, Integer limite,
                  String idProposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.idProposta = idProposta;
    }

    /**
     * Hibernate only
     */
    @Deprecated
    public Cartao() {
    }
}
