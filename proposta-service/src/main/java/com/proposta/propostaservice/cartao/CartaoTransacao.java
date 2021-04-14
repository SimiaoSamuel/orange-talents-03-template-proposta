package com.proposta.propostaservice.cartao;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class CartaoTransacao {
    private final CartaoRepository repository;

    public CartaoTransacao(CartaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Cartao save(Cartao cartao){
        return repository.save(cartao);
    }
}
