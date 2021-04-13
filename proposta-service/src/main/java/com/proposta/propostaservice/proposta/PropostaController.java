package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.handler.ErroApiException;
import com.proposta.propostaservice.solicitante.RequestSolicitacaoCartao;
import com.proposta.propostaservice.solicitante.ResponseSolicitacaoCartao;
import com.proposta.propostaservice.solicitante.RestricaoCartaoFeign;
import com.proposta.propostaservice.util.OfuscamentoUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final Logger LOG = LoggerFactory.getLogger(PropostaController.class);
    private final RestricaoCartaoFeign restricaoCartaoFeign;
    private final ExecutorTransacao executorTransacao;

    public PropostaController(RestricaoCartaoFeign restricaoCartaoFeign, ExecutorTransacao executorTransacao) {
        this.restricaoCartaoFeign = restricaoCartaoFeign;
        this.executorTransacao = executorTransacao;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> enviaProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){
        Proposta proposta = propostaRequest.toProposta();
        Boolean existeDocumentoIgual = executorTransacao.existsByDocumento(proposta.getDocumento());

        if(existeDocumentoIgual)
            throw new ErroApiException("documento","Esse documento já está cadastrado em uma proposta",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        executorTransacao.save(proposta);
        solicitacaoParaValidarDados(proposta);

        String emailOfuscado = OfuscamentoUtil.Ofuscar(proposta.getEmail());
        String documento = OfuscamentoUtil.Ofuscar(proposta.getDocumento());
        LOG.info("Proposta com email: {} documento: {} status:{} criada com sucesso!",emailOfuscado
                ,documento, proposta.getStatusProposta());

        PropostaResponse propostaResponse = new PropostaResponse(proposta);

        URI uri = uriBuilder.path("propostas/{id}").buildAndExpand(propostaResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(propostaResponse);
    }

    public void solicitacaoParaValidarDados(Proposta proposta){
        RequestSolicitacaoCartao requestCartao = new RequestSolicitacaoCartao(proposta);
        try {
            ResponseSolicitacaoCartao dadosCartao = restricaoCartaoFeign.retornaDadosSolicitante(requestCartao);
            proposta.statusValido();
            executorTransacao.save(proposta);
        }
        catch (FeignException e){
            LOG.error("[{}] during [POST] to [http://localhost:9999/api/solicitacao]",e.status());
            LOG.warn("O estado dessa proposta é NÃO_ELEGÍVEL");
        }
    }
}
