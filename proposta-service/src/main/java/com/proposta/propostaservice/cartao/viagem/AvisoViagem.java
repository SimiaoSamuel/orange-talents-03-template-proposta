package com.proposta.propostaservice.cartao.viagem;

import com.proposta.propostaservice.cartao.Cartao;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoViagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate dataTermino;

    private String ip;

    private String userAgent;

    @CreationTimestamp
    private LocalDateTime instanteAviso;


    /**
     *
     * @param cartao Cartão que emitiu o aviso
     * @param destino Lugar da viagem
     * @param dataTermino Dia que vai terminar a viagem
     * @param ip Ip do usuario
     * @param userAgent De onde veio essa requisição
     */
    public AvisoViagem(Cartao cartao, String destino, LocalDate dataTermino, String ip,
                       String userAgent) {
        this.cartao = cartao;
        this.destino = destino;
        this.dataTermino = dataTermino;
        this.ip = ip;
        this.userAgent = userAgent;
    }

    /**
     * Hibernate only
     */
    @Deprecated
    public AvisoViagem() {
    }

    public Long getId() {
        return id;
    }
}
