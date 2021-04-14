package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.handler.ErroApiException;
import com.proposta.propostaservice.solicitante.SolicitacaoCartaoRequest;
import com.proposta.propostaservice.solicitante.RestricaoCartaoResponse;
import com.proposta.propostaservice.solicitante.RestricaoCartaoFeign;
import com.proposta.propostaservice.util.OfuscamentoUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/propostas")
@EnableScheduling
public class PropostaController {

    private final Logger LOG = LoggerFactory.getLogger(PropostaController.class);
    private final RestricaoCartaoFeign restricaoCartaoFeign;
    private final PropostaTransacao propostaTransacao;

    public PropostaController(RestricaoCartaoFeign restricaoCartaoFeign, PropostaTransacao executorTransacao) {
        this.restricaoCartaoFeign = restricaoCartaoFeign;
        this.propostaTransacao = executorTransacao;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> enviaProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){
        Proposta proposta = propostaRequest.toProposta();
        Boolean existeDocumentoIgual = propostaTransacao.existsByDocumento(proposta.getDocumento());

        if(existeDocumentoIgual)
            throw new ErroApiException("documento","Esse documento já está cadastrado em uma proposta",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        propostaTransacao.save(proposta);
        solicitacaoParaValidarDados(proposta);

        String emailOfuscado = OfuscamentoUtil.Ofuscar(proposta.getEmail());
        String documento = OfuscamentoUtil.Ofuscar(proposta.getDocumento());
        LOG.info("Proposta com email: {} documento: {} status:{} criada com sucesso!",emailOfuscado
                ,documento, proposta.getStatusProposta());

        PropostaResponse propostaResponse = new PropostaResponse(proposta);

        URI uri = uriBuilder.path("propostas/{id}").buildAndExpand(propostaResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(propostaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proposta> buscarProposta(@PathVariable Long id){
        Optional<Proposta> proposta = propostaTransacao.findPropostaById(id);
        if(proposta.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(proposta.get());
    }

    public void solicitacaoParaValidarDados(Proposta proposta){
        SolicitacaoCartaoRequest requestCartao = new SolicitacaoCartaoRequest(proposta);
        try {
            RestricaoCartaoResponse dadosCartao = restricaoCartaoFeign.retornaDadosSolicitante(requestCartao);
            proposta.statusValido();
            propostaTransacao.save(proposta);
        } catch (FeignException e){
            LOG.error("[{}] during [POST] to [http://localhost:9999/api/solicitacao]",e.status());
            LOG.warn("O estado dessa proposta é NÃO_ELEGÍVEL");
        }
    }
}
