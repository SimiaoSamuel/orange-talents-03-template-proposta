package com.proposta.propostaservice.cartao.aviso;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proposta.propostaservice.proposta.PropostaResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ViagemAvisoTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Faz uma requisição para proposta")
    public void seCartaoInvalidoComDadosValidosRetornaNotFound() throws Exception {
        String url = "/cartoes/" + "cartao-invalido" + "/avisos";

        String body = buildJsonRequest("ABC", "2021-12-12");

        mvc.perform(post(url).content(body).contentType("application/json")).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Faz uma requisição para proposta")
    public void SeDadosInvalidoRetornaBadRequest() throws Exception {
        String url = "/cartoes/" + "cartao-invalido" + "/avisos";

        String body = buildJsonRequest("", "2020-12-12");

        mvc.perform(post(url).content(body).contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Faz uma requisição para proposta")
    public void SeDadosCorretosECartaoValidosRetornaCreated() throws Exception {
        String url = "/cartoes/" + getCartao() + "/avisos";
        String body = buildJsonRequest("ABC", LocalDate.now().toString());

        mvc.perform(post(url).content(body).contentType("application/json"))
                .andExpect(status().isCreated());
    }

    public String buildJsonRequest(String destino, String terminaEm) {
        return "{\n" +
                "\"destino\":\"" + destino + "\",\n" +
                "\"terminaEm\":\"" + terminaEm + "\"\n" +
                "}";
    }

    public String getCartao() throws Exception {
        MvcResult mvcResult = mvc.perform(get("/propostas/1")).andReturn();
        String resultadoJson = mvcResult.getResponse().getContentAsString();

        PropostaResponse proposta = mapper.readValue(resultadoJson, PropostaResponse.class);
        return proposta.getCartao();
    }
}
