package com.proposta.propostaservice.proposta;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class ExecutorTransacao {

    private final PropostaRepository repository;

    public ExecutorTransacao(PropostaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Proposta save(Proposta proposta){
        return repository.save(proposta);
    }

    public Boolean existsByDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }
}
