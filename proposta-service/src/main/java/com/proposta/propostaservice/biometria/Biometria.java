package com.proposta.propostaservice.biometria;

import com.proposta.propostaservice.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
public class Biometria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fingerPrint;

    @ManyToOne
    private Cartao cartao;

    private LocalDateTime criadaEM;

    public Biometria(String fingerPrint, Cartao cartao) {
        this.fingerPrint = fingerPrint;
        this.cartao = cartao;
        criadaEM = LocalDateTime.now().withNano(0);
    }

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
