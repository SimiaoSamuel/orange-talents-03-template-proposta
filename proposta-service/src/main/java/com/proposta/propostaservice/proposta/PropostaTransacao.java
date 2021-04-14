package com.proposta.propostaservice.proposta;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public class PropostaTransacao {

    private final PropostaRepository repository;

    public PropostaTransacao(PropostaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Proposta save(Proposta proposta){
        return repository.save(proposta);
    }

    public Boolean existsByDocumento(String documento) {
        return repository.existsByDocumento(documento);
    }

    public List<Proposta> findPropostasValidas(){
        return repository.findByStatusProposta(StatusProposta.ELEGIVEL);
    }

    public Optional<Proposta> findPropostaById(Long id){
        return repository.findById(id);
    }
}
