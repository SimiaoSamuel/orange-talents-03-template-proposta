package com.proposta.propostaservice.cartao.biometria;

import com.proposta.propostaservice.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Biometria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fingerPrint;

    @ManyToOne
    private Cartao cartao;

    private LocalDateTime criadaEM;

    /**
     *
     * @param fingerPrint String de biometria compatível com Base64
     * @param cartao cartão a qual essa biometria vai ser relacionada
     */
    public Biometria(String fingerPrint, Cartao cartao) {
        this.fingerPrint = fingerPrint;
        this.cartao = cartao;
        criadaEM = LocalDateTime.now().withNano(0);
    }

    /**
     * Hibernate Only
     */
    @Deprecated
    public Biometria() {
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public Long getId() {
        return id;
    }
}
