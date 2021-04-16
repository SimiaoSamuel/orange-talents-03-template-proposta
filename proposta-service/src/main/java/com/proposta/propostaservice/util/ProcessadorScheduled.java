package com.proposta.propostaservice.util;

import com.proposta.propostaservice.biometria.ExecutorTransacao;
import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoFeignResource;
import com.proposta.propostaservice.cartao.CartaoResponse;
import com.proposta.propostaservice.proposta.Proposta;
import com.proposta.propostaservice.proposta.PropostaRepository;
import com.proposta.propostaservice.proposta.StatusProposta;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class ProcessadorScheduled {
    private final CartaoFeignResource cartaoFeign;
    private final PropostaRepository propostaRepository;
    private final ExecutorTransacao transacao;
    private final Logger LOG = LoggerFactory.getLogger(ProcessadorScheduled.class);

    public ProcessadorScheduled(CartaoFeignResource cartaoFeign, PropostaRepository propostaRepository,
                                ExecutorTransacao transacao) {
        this.cartaoFeign = cartaoFeign;
        this.propostaRepository = propostaRepository;
        this.transacao = transacao;
    }

    @Scheduled(fixedDelay = 10000)
    public void gerarCartao() {
        List<Proposta> listaDePropostas = propostaRepository
                .findByStatusProposta(StatusProposta.ELEGIVEL);
        listaDePropostas.forEach(this::logic);
    }

    public void logic(Proposta proposta) {
        try {
            CartaoResponse cartaoResponse = cartaoFeign.getCartaoPeloIdProposta(proposta.getId().toString());
            Cartao cartao = transacao.salvaEComita(cartaoResponse.toCartao());
            proposta.adicionaCartao(cartao);
            transacao.atualizaEComita(proposta);
        } catch (FeignException e) {
            LOG.error("[{}] during [POST] to [http://localhost:8888/api/cartoes]", e.status());
            LOG.warn("Erro ao tentar atrelar cartão");
        }
    }
}
