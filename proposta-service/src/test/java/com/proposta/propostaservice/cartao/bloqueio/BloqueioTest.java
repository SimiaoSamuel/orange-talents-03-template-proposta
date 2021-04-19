package com.proposta.propostaservice.cartao.bloqueio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proposta.propostaservice.proposta.PropostaResponse;
import org.junit.experimental.results.ResultMatchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BloqueioTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void seCartaoInvalidoRetornaNotFound() throws Exception {
        mvc.perform(post("/cartoes/cartao-invalido/bloqueios"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(1)
    public void seCartaoValidoEstaComHeaderEmBrancoRetornaBadRequest() throws Exception {
        String cartao = getCartao();
        String resourceUri = "/cartoes/" + cartao + "/bloqueios";
        mvc.perform(post(resourceUri))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(2)
    public void seCartaoValidoNaoBloqueadoRetornaCreated() throws Exception {
        String cartao = getCartao();
        String resourceUri = "/cartoes/" + cartao + "/bloqueios";
        mvc.perform(post(resourceUri).header("User-Agent","Postman"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith(resourceUri + "/1")));
    }

    @Test
    @Order(3)
    public void seCartaoValidoEstaBloqueadoRetornaUnprocessable() throws Exception {
        String cartao = getCartao();
        String resourceUri = "/cartoes/" + cartao + "/bloqueios";
        mvc.perform(post(resourceUri).header("User-Agent","Postman"))
                .andExpect(status().isUnprocessableEntity());
    }

    public String getCartao() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/propostas/1")).andReturn();
        String resultadoJson = mvcResult.getResponse().getContentAsString();

        PropostaResponse proposta = mapper.readValue(resultadoJson, PropostaResponse.class);
        return proposta.getCartao();
    }
}
