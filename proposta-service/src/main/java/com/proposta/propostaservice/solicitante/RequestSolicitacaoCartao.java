package com.proposta.propostaservice.solicitante;

import com.proposta.propostaservice.proposta.Proposta;

import javax.validation.constraints.NotBlank;

public class RequestSolicitacaoCartao {
    @NotBlank
    private String documento;
    @NotBlank
    private String nome;
    @NotBlank
    private String idProposta;

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }

    /**
     *
     * @param proposta  proposta que você vai enviar para fazer a solicitação do cartão
     */
    public RequestSolicitacaoCartao(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.nome = proposta.getNome();
        this.idProposta = proposta.getId().toString();
    }
}
