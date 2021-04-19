package com.proposta.propostaservice.cartao.bloqueio;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoRepository;
import com.proposta.propostaservice.cartao.biometria.ExecutorTransacao;
import com.proposta.propostaservice.handler.ErroApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.net.URI;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/cartoes/{idCartao}/bloqueios")
public class BloqueioController {

    private final CartaoRepository cartaoRepository;
    private final ExecutorTransacao transacao;
    private final javax.validation.Validator validator;

    public BloqueioController(CartaoRepository cartaoRepository, ExecutorTransacao transacao, Validator validator) {
        this.cartaoRepository = cartaoRepository;
        this.transacao = transacao;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Void> bloquearCartao(@PathVariable String idCartao,
                                               UriComponentsBuilder uriBuilder,
                                               HttpServletRequest servlet){
        Optional<Cartao> cartao = cartaoRepository.findById(idCartao);

        if(cartao.isEmpty())
            throw new ErroApiException(null,"Requisição inválida", HttpStatus.NOT_FOUND);

        Cartao cartaoEntity = cartao.get();

        if(cartaoEntity.isBloqueado())
            throw new ErroApiException(null,"Cartão já está bloqueado", HttpStatus.UNPROCESSABLE_ENTITY);

        BloqueioRequest bloqueioRequest = new BloqueioRequest(idCartao, servlet.getRemoteAddr(),
                servlet.getHeader("User-Agent"));
        Set<ConstraintViolation<BloqueioRequest>> errors = validator.validate(bloqueioRequest);

        if(!errors.isEmpty())
            throw new ConstraintViolationException(errors);

        BloqueioCartao bloqueio = bloqueioRequest.toBloqueioCartao();
        cartaoEntity.bloquear();

        transacao.atualizaEComita(cartaoEntity);
        transacao.salvaEComita(bloqueio);

        URI uri = uriBuilder.path("/cartoes/{idCartao}/bloqueios/{id}").buildAndExpand(idCartao, bloqueio.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }
}
