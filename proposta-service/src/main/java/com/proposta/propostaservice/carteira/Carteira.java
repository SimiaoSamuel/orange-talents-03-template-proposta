package com.proposta.propostaservice.carteira;

import com.proposta.propostaservice.cartao.Cartao;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Carteira {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private GatewayPagamento pagamento;

    @Column(nullable = false)
    private String idCarteira;

    /**
     *
     * @param cartao
     * @param email
     * @param pagamento
     * @param idCarteira
     */
    public Carteira(Cartao cartao, String email, GatewayPagamento pagamento, String idCarteira) {
        this.cartao = cartao;
        this.email = email;
        this.idCarteira = idCarteira;
        this.pagamento = pagamento;
    }

    /**
     * Hibernate only
     */
    @Deprecated
    public Carteira() {
    }
}
