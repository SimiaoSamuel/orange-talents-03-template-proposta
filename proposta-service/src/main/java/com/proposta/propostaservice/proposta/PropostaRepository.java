package com.proposta.propostaservice.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {
    Boolean existsByDocumento(String documento);
}
