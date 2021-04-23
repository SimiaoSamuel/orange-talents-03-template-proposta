package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.cartao.biometria.ExecutorTransacao;
import com.proposta.propostaservice.shared.handler.ErroApiException;
import com.proposta.propostaservice.shared.metrics.Metrica;
import com.proposta.propostaservice.solicitante.SolicitacaoCartaoRequest;
import com.proposta.propostaservice.solicitante.RestricaoCartaoResponse;
import com.proposta.propostaservice.solicitante.RestricaoCartaoFeign;
import com.proposta.propostaservice.util.OfuscamentoUtil;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
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
    private final PropostaRepository propostaRepository;
    private final ExecutorTransacao transacao;
    private final Metrica metrica;
    private final Tracer tracer;

    public PropostaController(RestricaoCartaoFeign restricaoCartaoFeign, Metrica metrica,
                              PropostaRepository propostaRepository, ExecutorTransacao transacao, Tracer tracer) {
        this.restricaoCartaoFeign = restricaoCartaoFeign;
        this.propostaRepository = propostaRepository;
        this.transacao = transacao;
        this.metrica = metrica;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> enviaProposta(@RequestBody @Valid PropostaRequest propostaRequest,
                                                          UriComponentsBuilder uriBuilder){
        Proposta proposta = propostaRequest.toProposta();
        Boolean existeDocumentoIgual = propostaRepository.existsByDocumento(proposta.getDocumento());

        Span span = tracer.activeSpan();
        span.setTag("Proposta.documento",proposta.getDocumento());

        span.log("criação de uma proposta");

        if(existeDocumentoIgual)
            throw new ErroApiException("documento","Esse documento já está cadastrado em uma proposta",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        transacao.salvaEComita(proposta);
        solicitacaoParaValidarDados(proposta);

        String emailOfuscado = OfuscamentoUtil.Ofuscar(proposta.getEmail());
        String documento = OfuscamentoUtil.Ofuscar(proposta.getDocumento());
        LOG.info("Proposta com email: {} documento: {} status:{} criada com sucesso!",emailOfuscado
                ,documento, proposta.getStatusProposta());

        PropostaResponse propostaResponse = new PropostaResponse(proposta);

        metrica.adicionaPropostaPrometheus();

        URI uri = uriBuilder.path("propostas/{id}").buildAndExpand(propostaResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(propostaResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropostaResponse> buscarProposta(@PathVariable Long id){
        long timeStartMetrics = System.currentTimeMillis();

        Optional<Proposta> proposta = propostaRepository.findById(id);
        if(proposta.isEmpty())
            throw new ErroApiException(null,"Não há nenhum recurso para essa url",HttpStatus.NOT_FOUND);

        PropostaResponse propostaResponse = new PropostaResponse(proposta.get());

        metrica.temporizadorProposta(timeStartMetrics);
        return ResponseEntity.ok(propostaResponse);
    }

    public void solicitacaoParaValidarDados(Proposta proposta){
        SolicitacaoCartaoRequest requestCartao = new SolicitacaoCartaoRequest(proposta);
        try {
            RestricaoCartaoResponse dadosCartao = restricaoCartaoFeign.retornaDadosSolicitante(requestCartao);
            proposta.statusValido();
            transacao.atualizaEComita(proposta);
        } catch (FeignException e){
            LOG.error("[{}] during [POST] to [http://localhost:9999/api/solicitacao]",e.status());
            LOG.warn("O estado dessa proposta é NÃO_ELEGÍVEL");
        }
    }
}
