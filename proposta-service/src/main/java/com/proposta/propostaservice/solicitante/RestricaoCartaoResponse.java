package com.proposta.propostaservice.solicitante;

public class RestricaoCartaoResponse {
    private String documento;
    private String nome;
    private String resultadoSolicitacao;
    private String idProposta;

    public RestricaoCartaoResponse(String documento, String nome, String resultadoSolicitacao,
                                   String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }
}
