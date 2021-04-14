package com.proposta.propostaservice.cartao;

import com.proposta.propostaservice.solicitante.SolicitacaoCartaoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cartao", url = "http://localhost:8888/")
public interface CartaoFeignResource {
    @RequestMapping(path = "/api/cartoes", method = RequestMethod.POST)
    CartaoResponse getCartao(SolicitacaoCartaoRequest solicitacaoCartao);

    @RequestMapping(path = "/api/cartoes", method = RequestMethod.GET)
    CartaoResponse getCartaoPeloIdProposta(@RequestParam(value = "idProposta") String idProposta);
}
