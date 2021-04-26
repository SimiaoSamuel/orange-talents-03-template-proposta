package com.proposta.propostaservice.cartao.biometria;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoRepository;
import com.proposta.propostaservice.shared.handler.ErroApiException;
import com.proposta.propostaservice.shared.metrics.Metrica;
import io.opentracing.Span;
import io.opentracing.Tracer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes/{idCartao}/biometrias")
@Validated
public class BiometriaController {

    private final CartaoRepository cartaoRepository;
    private final ExecutorTransacao transacao;
    private final Metrica metrica;
    private final BiometriaRepository biometriaRepository;
    private final Tracer tracer;

    public BiometriaController(CartaoRepository cartaoRepository, ExecutorTransacao transacao,
                               Metrica metrica, BiometriaRepository biometriaRepository, Tracer tracer) {
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
        this.metrica = metrica;
        this.biometriaRepository = biometriaRepository;
        this.tracer = tracer;
    }

    @PostMapping
    public ResponseEntity<Void> salvarBiometria(@PathVariable("idCartao") @NotBlank String idCartao,
                                                @RequestBody @Valid BiometriaRequest biometriaRequest,
                                                UriComponentsBuilder uriBuilder) {
        Span span = tracer.activeSpan();
        if (span != null) {
            span.setTag("cartao.biometria", idCartao);
            span.log("tentativa de salvar uma biometria para um cartão");
        }

        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);
        if (cartao.isEmpty())
            throw new ErroApiException(null, "cartão não encontrado", HttpStatus.NOT_FOUND);

        Biometria biometria = biometriaRequest.toBiometria(cartao.get());

        Boolean existe = biometriaRepository.existsByFingerPrint(biometria.getFingerPrint());

        if (existe)
            throw new ErroApiException("biometria", "Não foi possível cadastrar essa biometria",
                    HttpStatus.UNPROCESSABLE_ENTITY);

        transacao.salvaEComita(biometria);

        URI uri = uriBuilder.path("/cartoes/{idCartao}/biometrias/{id}")
                .buildAndExpand(idCartao, biometria.getId()).toUri();

        metrica.biometriaContador();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiometriaResponse> retornarBiometria(@PathVariable @NotNull Long id) {
        Optional<Biometria> biometria = biometriaRepository.findById(id);
        if (biometria.isEmpty())
            throw new ErroApiException(null, "Erro ao procurar pela biometria", HttpStatus.NOT_FOUND);

        BiometriaResponse biometriaResponse = new BiometriaResponse(biometria.get());

        return ResponseEntity.ok(biometriaResponse);
    }
}
