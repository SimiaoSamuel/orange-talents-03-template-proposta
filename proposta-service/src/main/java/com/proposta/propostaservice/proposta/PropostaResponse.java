package com.proposta.propostaservice.proposta;

public class PropostaResponse {
    private Long id;
    private String nome;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    /**
     *
     * @param proposta Proposta que você quer retornar como um dto
     */
    public PropostaResponse(Proposta proposta) {
        this.nome = proposta.getNome();
        this.id = proposta.getId();
    }
}
