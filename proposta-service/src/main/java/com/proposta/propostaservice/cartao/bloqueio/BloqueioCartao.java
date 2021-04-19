package com.proposta.propostaservice.cartao.bloqueio;

import com.proposta.propostaservice.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @Column(nullable = false)
    private LocalDateTime instanteBloqueio;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String userAgent;

    public Long getId() {
        return id;
    }

    /**
     *
     * @param cartao cartao que vai ser bloqueado
     * @param ip ip do usuario
     * @param userAgent agente da requisição
     */
    public BloqueioCartao(Cartao cartao, String ip, String userAgent) {
        this.cartao = cartao;
        this.instanteBloqueio = LocalDateTime.now();
        this.ip = ip;
        this.userAgent = userAgent;
    }

    /**
     * Hibernate only
     */
    @Deprecated
    public BloqueioCartao() {
    }
}
