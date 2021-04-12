package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.annotation.CPFOrCNPJ;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String documento;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private BigDecimal salario;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
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
    }

    /**
     * Hibernate Only
     */
    @Deprecated
    public Proposta() {
    }
}
