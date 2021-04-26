package com.proposta.propostaservice.cartao.viagem;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoFeignResource;
import com.proposta.propostaservice.cartao.CartaoRepository;
import com.proposta.propostaservice.cartao.biometria.ExecutorTransacao;
import com.proposta.propostaservice.shared.handler.ErroApiException;
import feign.FeignException;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes/{idCartao}/avisos")
@Validated
public class AvisoController {

    private final CartaoRepository cartaoRepository;
    private final ExecutorTransacao transacao;
    private final CartaoFeignResource feign;
    private final Tracer tracer;

    public AvisoController(CartaoRepository cartaoRepository, ExecutorTransacao transacao, CartaoFeignResource feign, Tracer tracer) {
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
        this.feign = feign;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<Void> enviarAviso(@RequestBody @Valid AvisoRequest avisoRequest,
                                            @PathVariable @NotBlank String idCartao,
                                            HttpServletRequest request,
                                            UriComponentsBuilder uriBuilder) {
        Span span = tracer.activeSpan();
        if (span != null) {
            span.setTag("cartao.viagem", idCartao);
            span.log("tentativa de criar um aviso de viagem");
        }

        Optional<Cartao> cartaoDB = cartaoRepository.findById(idCartao);

        if (cartaoDB.isEmpty())
            throw new ErroApiException(null, "Erro relacionado ao cartão", HttpStatus.NOT_FOUND);

        try {
            feign.avisoCartao(idCartao, avisoRequest);
        } catch (FeignException ex) {
            throw new ErroApiException(null, "Não foi possível cadastrar o aviso", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        AvisoViagem avisoViagem = avisoRequest.toAvisoViagem(cartaoDB.get(), request.getRemoteAddr(),
                request.getHeader("User-Agent"));
        transacao.salvaEComita(avisoViagem);

        URI uri = uriBuilder.path("cartoes/{idCartao}/avisos/{id}")
                .buildAndExpand(idCartao, avisoViagem.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}
