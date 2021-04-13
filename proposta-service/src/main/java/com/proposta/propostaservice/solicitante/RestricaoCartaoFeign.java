package com.proposta.propostaservice.solicitante;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Acesso a API externa para fazer a validação dos dados dá proposta e verificar se
 * o solicitante é elegível ou não a ter um cartão
 */
@FeignClient(name = "restricao", url = "${cartao.endereco}")
public interface RestricaoCartaoFeign {
    @RequestMapping(method = RequestMethod.POST, value = "/api/solicitacao",
            consumes = "application/json")
    ResponseSolicitacaoCartao retornaDadosSolicitante(@RequestBody RequestSolicitacaoCartao dados);
}
