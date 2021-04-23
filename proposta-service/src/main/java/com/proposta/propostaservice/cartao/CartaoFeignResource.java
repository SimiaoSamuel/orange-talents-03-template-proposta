package com.proposta.propostaservice.cartao;

import com.proposta.propostaservice.cartao.bloqueio.BloqueioFeignRequest;
import com.proposta.propostaservice.cartao.bloqueio.ResultadoBloqueio;
import com.proposta.propostaservice.cartao.viagem.AvisoRequest;
import com.proposta.propostaservice.cartao.viagem.ResultadoAviso;
import com.proposta.propostaservice.carteira.CarteiraFeignResponse;
import com.proposta.propostaservice.carteira.CarteiraRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cartao", url = "${cartao.criacao}")
public interface CartaoFeignResource {
    @RequestMapping(path = "/api/cartoes/{id}", method = RequestMethod.GET)
    CartaoResponse getCartao(@PathVariable("id") String id);

    @RequestMapping(path = "/api/cartoes", method = RequestMethod.GET)
    CartaoResponse getCartaoPeloIdProposta(@RequestParam(value = "idProposta") String idProposta);

    @RequestMapping(path = "/api/cartoes/{id}/bloqueios", method = RequestMethod.POST,
            consumes = "application/json")
    ResultadoBloqueio bloqueiaCartaoPeloId(@PathVariable(value = "id") String id,
                                           @RequestBody BloqueioFeignRequest request);

    @RequestMapping(path = "/api/cartoes/{id}/avisos", method = RequestMethod.POST,
            consumes = "application/json")
    ResultadoAviso avisoCartao(@PathVariable(value = "id") String id, @RequestBody AvisoRequest avisoRequest);

    @RequestMapping(path = "/api/cartoes/{id}/carteiras", method = RequestMethod.POST,
            consumes = "application/json")
    CarteiraFeignResponse associandoCarteira(@PathVariable(value = "id") String id, @RequestBody CarteiraRequest carteiraRequest);
}
