package com.proposta.propostaservice.cartao.bloqueio;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String idCartao;

    @Column(nullable = false)
    private LocalDateTime instanteBloqueio;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private String userAgent;

    public Long getId() {
        return id;
    }

    public BloqueioCartao(String idCartao, String ip, String userAgent) {
        this.idCartao = idCartao;
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
