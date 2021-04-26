package com.proposta.propostaservice.cartao.bloqueio;

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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/cartoes/{idCartao}/bloqueios")
@Validated
public class BloqueioController {

    private final CartaoRepository cartaoRepository;
    private final ExecutorTransacao transacao;
    private final Validator validator;
    private final CartaoFeignResource cartaoFeign;

    private final Tracer tracer;

    public BloqueioController(CartaoRepository cartaoRepository, ExecutorTransacao transacao,
                              Validator validator, CartaoFeignResource cartaoFeign, Tracer tracer) {
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
        this.validator = validator;
        this.cartaoFeign = cartaoFeign;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<Void> bloquearCartao(@PathVariable @NotBlank String idCartao,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletRequest servlet) {
        Span span = tracer.activeSpan();
        if (span != null) {
            span.setTag("cartao.bloqueio", idCartao);
            span.log("tentativa de bloquear o cartão");
        }

        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

        if (cartao.isEmpty())
            throw new ErroApiException(null, "Requisição inválida", HttpStatus.NOT_FOUND);

        Cartao cartaoEntity = cartao.get();

        if (cartaoEntity.isBloqueado())
            throw new ErroApiException(null, "Cartão já está bloqueado", HttpStatus.UNPROCESSABLE_ENTITY);

        BloqueioRequest bloqueioRequest = new BloqueioRequest(servlet.getRemoteAddr(),
                servlet.getHeader("User-Agent"));
        Set<ConstraintViolation<BloqueioRequest>> errors = validator.validate(bloqueioRequest);

        if (!errors.isEmpty())
            throw new ConstraintViolationException(errors);

        try {
            cartaoFeign.bloqueiaCartaoPeloId(idCartao, new BloqueioFeignRequest("proposta-service"));
        } catch (FeignException e) {
            throw new ErroApiException(null, "Não foi possível bloquear seu cartão", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        BloqueioCartao bloqueio = bloqueioRequest.toBloqueioCartao(cartaoEntity);
        cartaoEntity.bloquear();

        transacao.atualizaEComita(cartaoEntity);
        transacao.salvaEComita(bloqueio);

        URI uri = uriBuilder.path("/cartoes/{idCartao}/bloqueios/{id}").buildAndExpand(idCartao, bloqueio.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
