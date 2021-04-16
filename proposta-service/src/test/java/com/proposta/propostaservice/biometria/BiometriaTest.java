package com.proposta.propostaservice.biometria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proposta.propostaservice.proposta.Proposta;
import com.proposta.propostaservice.proposta.PropostaResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class BiometriaTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Faz uma requisição e deve retornar not found se o cartão não existe")
    public void seNaoEncontrarCartaoRetornarNotFound() throws Exception {

        String request = buildJsonRequest("c2FtdWVs");

        mockMvc.perform(post("/cartoes/cartaoInvalido/biometrias")
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Faz uma requisiçao e deve retornar created com a location se o cartão e biometria forem validos")
    public void testeBiometriaValidaComCartaoValido() throws Exception {
        String request = buildJsonRequest("c2FtdWVs");
        String cartao = getCartao();
        String url = "/cartoes/" + cartao + "/biometrias";
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost" + url + "/1"));
    }

    @Test
    @DisplayName("Faz uma requisição e deve retornar bad request se o cartão for valido e biometria invalida")
    public void testeBiometriaInvalidaComCartaoValido() throws Exception {
        String request = buildJsonRequest("samuel");
        String cartao = getCartao();
        String url = "/cartoes/" + cartao + "/biometrias";
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isBadRequest());
    }

    public String getCartao() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/propostas/1")).andReturn();
        String resultadoJson = mvcResult.getResponse().getContentAsString();

        PropostaResponse proposta = objectMapper.readValue(resultadoJson, PropostaResponse.class);
        return proposta.getCartao();
    }

    public String buildJsonRequest(String biometria) {
        return "{\n" +
                "\"biometria\":\"" + biometria + "\"\n" +
                "}";
    }
}
