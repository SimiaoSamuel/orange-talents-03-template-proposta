package com.proposta.propostaservice.biometria;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
public class ExecutorTransacao {

    private final EntityManager manager;

    public ExecutorTransacao(EntityManager manager) {
        this.manager = manager;
    }

    @Transactional
    public <T> T salvaEComita(T objeto) {
        manager.persist(objeto);
        return objeto;
    }

    @Transactional
    public <T> T atualizaEComita(T objeto) {
        return manager.merge(objeto);
    }

}
