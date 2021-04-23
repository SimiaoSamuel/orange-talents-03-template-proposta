package com.proposta.propostaservice.carteira;

import com.proposta.propostaservice.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarteiraRepository extends JpaRepository<Carteira,Long> {
    Optional<Carteira> findByCartaoAndPagamento(Cartao cartao, GatewayPagamento pagamento);
}
