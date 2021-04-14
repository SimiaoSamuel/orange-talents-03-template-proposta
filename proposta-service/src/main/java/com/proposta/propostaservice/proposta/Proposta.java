package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.cartao.Cartao;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String documento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private StatusProposta statusProposta;

    @ManyToOne
    private Cartao cartao;

    public void adicionaCartao(Cartao cartao) {
        this.cartao = cartao;
        this.statusProposta = StatusProposta.CARTAO_ATRELADO;
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public StatusProposta getStatusProposta() {
        return statusProposta;
    }

    public void statusValido() {
        this.statusProposta = StatusProposta.ELEGIVEL;
    }

    /**
     *
     * @param documento CPF ou CNPJ em formato de String
     * @param email Email em formato de String
     * @param nome Nome de quem fez a proposta em formato de String
     * @param endereco Endereço de quem fez a proposta em formato de String
     * @param salario Salário de quem fez a proposta em formato de BigDecimal
     */
    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.statusProposta = StatusProposta.NAO_ELEGIVEL;
    }

    /**
     * Hibernate Only
     */
    @Deprecated
    public Proposta() {
    }
}
