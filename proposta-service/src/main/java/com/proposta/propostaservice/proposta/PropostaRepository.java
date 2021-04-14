package com.proposta.propostaservice.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Boolean existsByDocumento(String documento);
    List<Proposta> findByStatusProposta(StatusProposta status);
}
