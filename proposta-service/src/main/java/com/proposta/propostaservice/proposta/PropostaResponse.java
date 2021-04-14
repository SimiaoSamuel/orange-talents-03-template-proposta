package com.proposta.propostaservice.proposta;

import java.math.BigDecimal;

public class PropostaResponse {
    private Long id;
    private String nome;
    private BigDecimal salario;
    private StatusProposta status;
    private String email;
    private String endereco;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public String getEmail() {
        return email;
    }

    public String getEndereco() {
        return endereco;
    }

    public StatusProposta getStatus() {
        return status;
    }

    /**
     *
     * @param proposta Proposta que você quer retornar como um dto
     */
    public PropostaResponse(Proposta proposta) {
        this.id = proposta.getId();
        this.nome = proposta.getNome();
        this.email = proposta.getEmail();
        this.salario = proposta.getSalario();
        this.endereco = proposta.getEndereco();
        this.status = proposta.getStatusProposta();
    }

    /**
     * Default constructor only for test mockmvc
     */
    public PropostaResponse() {
    }
}
