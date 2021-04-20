package com.proposta.propostaservice.cartao.biometria;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BiometriaRepository extends JpaRepository<Biometria,Long> {
    Boolean existsByFingerPrint(String biometria);
}
