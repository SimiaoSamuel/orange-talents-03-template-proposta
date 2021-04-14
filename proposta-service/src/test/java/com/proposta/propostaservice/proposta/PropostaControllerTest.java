package com.proposta.propostaservice.proposta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestExecution;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PropostaControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @MethodSource("provideValuesForTestElegivelRequest")
    @DisplayName("Se o documento não tiver sido associado a uma proposta e não começar com 3")
    @Order(1)
    public void TesteDocumentoElegivel(String documento, String email, String nome, String endereco,
                                       BigDecimal salario) throws Exception {

        String request = getRequestInJsonFormat(documento, email, nome, endereco, salario);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/propostas").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        PropostaResponse propostaResponse = objectMapper.readValue(contentAsString, PropostaResponse.class);
        assertEquals(StatusProposta.ELEGIVEL, propostaResponse.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideValuesForTestNaoElegivelRequest")
    @DisplayName("Se o documento não tiver sido associado a uma proposta e começar com 3")
    @Order(2)
    public void TesteDocumentoNaoElegivel(String documento, String email, String nome,
                                          String endereco, BigDecimal salario) throws Exception {

        String request = getRequestInJsonFormat(documento, email, nome, endereco, salario);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/propostas").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated()).andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        PropostaResponse propostaResponse = objectMapper.readValue(contentAsString, PropostaResponse.class);
        assertEquals(StatusProposta.NAO_ELEGIVEL, propostaResponse.getStatus());
    }

    @ParameterizedTest
    @AfterTestExecution
    @MethodSource("provideValuesForTestElegivelRequest")
    @DisplayName("Documento igual")
    @Order(3)
    public void TesteDocumentoIgual(String documento, String email, String nome, String endereco,
                                    BigDecimal salario) throws Exception {

        String request = getRequestInJsonFormat(documento, email, nome, endereco, salario);

        mvc.perform(MockMvcRequestBuilders.post("/propostas").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isUnprocessableEntity());
    }

    public String getRequestInJsonFormat(String documento, String email, String nome,
                                         String endereco, BigDecimal salario) {
        return "{\n" +
                "    \"documento\":\"" + documento + "\",\n" +
                "    \"email\":\"" + email + "\",\n" +
                "    \"nome\":\"" + nome + "\",\n" +
                "    \"endereco\":\"" + endereco + "\",\n" +
                "    \"salario\":" + salario + "\n" +
                "}";
    }

    private static Stream<Arguments> provideValuesForTestElegivelRequest() {
        return Stream.of(
                Arguments.of("430.886.770-39", "email@email.com", "nome", "endereco", BigDecimal.valueOf(1)),
                Arguments.of("982.719.320-12", "xp@xp.com", "name", "adress", BigDecimal.valueOf(10))
        );
    }

    private static Stream<Arguments> provideValuesForTestNaoElegivelRequest() {
        return Stream.of(
                Arguments.of("315.271.350-98", "email@email.com", "nome", "endereco", BigDecimal.valueOf(1)),
                Arguments.of("310.552.550-52", "xp@xp.com", "name", "adress", BigDecimal.valueOf(10))
        );
    }
}

