package com.proposta.propostaservice.solicitante;

public class ResponseSolicitacaoCartao {
    private String documento;
    private String nome;
    private String resultadoSolicitacao;
    private String idProposta;

    public ResponseSolicitacaoCartao(String documento, String nome, String resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }
}
