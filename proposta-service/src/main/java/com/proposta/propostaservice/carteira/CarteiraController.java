package com.proposta.propostaservice.carteira;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoFeignResource;
import com.proposta.propostaservice.cartao.CartaoRepository;
import com.proposta.propostaservice.cartao.biometria.ExecutorTransacao;
import com.proposta.propostaservice.shared.handler.ErroApiException;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/cartoes/{idCartao}/carteiras")
@Validated
public class CarteiraController {

    private final CartaoFeignResource feign;
    private final CartaoRepository cartaoRepository;
    private final ExecutorTransacao transacao;
    private final CarteiraRepository carteiraRepository;

    public CarteiraController(CartaoFeignResource feign, CartaoRepository cartaoRepository, ExecutorTransacao transacao, CarteiraRepository carteiraRepository) {
        this.feign = feign;
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
        this.carteiraRepository = carteiraRepository;
    }

    @PostMapping
    public ResponseEntity<Void> salvaCarteira(@PathVariable @NotBlank String idCartao,
                                              @RequestBody @Valid CarteiraRequest carteiraRequest,
                                              UriComponentsBuilder uriBuilder) {
        Optional<Cartao> cartaoDB = cartaoRepository.findById(idCartao);

        if (cartaoDB.isEmpty())
            throw new ErroApiException(null, "Erro ao procurar pelo cartão informado", HttpStatus.NOT_FOUND);

        Optional<Carteira> carteiraDB = carteiraRepository.findByCartaoAndPagamento(cartaoDB.get(),
                GatewayPagamento.valueOf(carteiraRequest.getCarteira()));

        if(carteiraDB.isPresent())
            throw new ErroApiException(null, "Erro ao associar carteira informada", HttpStatus.UNPROCESSABLE_ENTITY);

        CarteiraFeignResponse carteiraFeignResponse;
        try {
            carteiraFeignResponse = feign.associandoCarteira(idCartao, carteiraRequest);
            Carteira carteira = carteiraRequest.toCarteira(cartaoDB.get(),
                    carteiraFeignResponse.getId());
            transacao.salvaEComita(carteira);
        } catch (FeignException e) {
            e.printStackTrace();
            throw new ErroApiException(null, "Erro ao associar nova carteira",
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        URI uri = uriBuilder.path("cartoes/{idCartao}/carteira/id")
                .buildAndExpand(idCartao, carteiraFeignResponse.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }
}
