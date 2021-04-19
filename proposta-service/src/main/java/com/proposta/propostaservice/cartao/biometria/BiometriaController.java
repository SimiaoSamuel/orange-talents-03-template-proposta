package com.proposta.propostaservice.cartao.biometria;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoRepository;
import com.proposta.propostaservice.handler.ErroApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes/{idCartao}/biometrias")
public class BiometriaController {

   private final CartaoRepository cartaoRepository;
   private final ExecutorTransacao transacao;

    public BiometriaController(CartaoRepository cartaoRepository, ExecutorTransacao transacao) {
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
    }

    @PostMapping
    public ResponseEntity<Void> salvarBiometria(@PathVariable String idCartao, @RequestBody @Valid BiometriaRequest biometriaRequest,
                                                  UriComponentsBuilder uriBuilder){
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);
        if(cartao.isEmpty())
            throw new ErroApiException(null,"cartão não encontrado", HttpStatus.NOT_FOUND);

        Biometria biometria = biometriaRequest.toBiometria(cartao.get());
        transacao.salvaEComita(biometria);

        URI uri = uriBuilder.path("/cartoes/{idCartao}/biometrias/{id}")
                .buildAndExpand(idCartao,biometria.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

}
