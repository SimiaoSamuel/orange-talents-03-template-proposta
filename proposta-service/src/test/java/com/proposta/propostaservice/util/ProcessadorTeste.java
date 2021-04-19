package com.proposta.propostaservice.util;

import com.proposta.propostaservice.proposta.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProcessadorTeste {
    @Autowired
    PropostaController propostaController;

    @Autowired
    ProcessadorScheduled processadorScheduled;

    @Test
    @Order(1)
    @DisplayName("Busca por propostas elegiveis no banco e atrela um cartão a elas")
    public void associaCartaoSeForElegivel() throws InterruptedException {
        PropostaRequest propostaRequest = new PropostaRequest("877.254.230-60", "samuel@gmail.com", "samuel",
                "endereco", BigDecimal.TEN);
        ResponseEntity<PropostaResponse> propostaResponsePost = propostaController.enviaProposta(propostaRequest,
                UriComponentsBuilder.newInstance());
        Long id = propostaResponsePost.getBody().getId();

        Thread.sleep(10000);
        processadorScheduled.gerarCartao();
        ResponseEntity<PropostaResponse> propostaResponseEntity = propostaController.buscarProposta(id);
        PropostaResponse proposta = propostaResponseEntity.getBody();

        assertNotNull(proposta);
        assertEquals(StatusProposta.CARTAO_ATRELADO,proposta.getStatus());
    }

    @Test
    @Order(2)
    @DisplayName("Busca por propostas elegiveis no banco e não deve atrelar essa por não ser elegivel")
    public void naoAssociaCartao(){
        PropostaRequest propostaRequest = new PropostaRequest("392.061.070-97", "samuel@gmail.com", "samuel",
                "endereco", BigDecimal.ONE);
        ResponseEntity<PropostaResponse> propostaResponsePost = propostaController.enviaProposta(propostaRequest,
                UriComponentsBuilder.newInstance());
        Long id = Objects.requireNonNull(propostaResponsePost.getBody()).getId();

        processadorScheduled.gerarCartao();
        ResponseEntity<PropostaResponse> propostaResponseEntity = propostaController.buscarProposta(id);
        PropostaResponse proposta = propostaResponseEntity.getBody();

        assertNotNull(proposta);
        assertEquals(StatusProposta.NAO_ELEGIVEL,proposta.getStatus());
    }
}
