package com.proposta.propostaservice.util;

import com.proposta.propostaservice.cartao.Cartao;
import com.proposta.propostaservice.cartao.CartaoFeignResource;
import com.proposta.propostaservice.cartao.CartaoResponse;
import com.proposta.propostaservice.cartao.CartaoTransacao;
import com.proposta.propostaservice.proposta.Proposta;
import com.proposta.propostaservice.proposta.PropostaTransacao;
import com.proposta.propostaservice.solicitante.SolicitacaoCartaoRequest;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@EnableScheduling
public class ProcessadorScheduled {
    private final CartaoFeignResource cartaoFeign;
    private final CartaoTransacao cartaoTransacao;
    private final PropostaTransacao executorTransacao;
    private final Logger LOG = LoggerFactory.getLogger(ProcessadorScheduled.class);

    public ProcessadorScheduled(CartaoFeignResource cartaoFeign, CartaoTransacao cartaoTransacao,
                                PropostaTransacao executorTransacao) {
        this.cartaoFeign = cartaoFeign;
        this.cartaoTransacao = cartaoTransacao;
        this.executorTransacao = executorTransacao;
    }

    @Transactional
    @Scheduled(fixedDelay = 10000)
    public void gerarCartao() {
        List<Proposta> listaDePropostas = executorTransacao.findPropostasValidas();
        listaDePropostas.forEach(this::logic);
    }

    public void logic(Proposta proposta) {
        try {
            CartaoResponse cartaoResponse = cartaoFeign.getCartaoPeloIdProposta(proposta.getId().toString());
            Cartao cartao = cartaoTransacao.save(cartaoResponse.toCartao());
            proposta.adicionaCartao(cartao);
        } catch (FeignException e) {
            LOG.error("[{}] during [POST] to [http://localhost:8888/api/cartoes]", e.status());
            LOG.warn("Erro ao tentar atrelar cartão");
        }
    }
}
