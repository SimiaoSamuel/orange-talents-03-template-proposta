package com.proposta.propostaservice.proposta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.proposta.propostaservice.handler.ErroApiException;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(SpringExtension.class)
public class PropostaControllerTest {
    @InjectMocks
    private PropostaController controller;

    @Mock
    private PropostaRepository propostaRepository;

    @BeforeEach
    public void setUp(){
        when(propostaRepository.existsByDocumento("027.294.610-94")).thenReturn(true);
    }

    @Test
    @DisplayName("Se o documento já estiver cadastrado no banco de dados lança uma exceção")
    public void seDocumentoJaEstaCadastradoRetornaUmaException() {
        PropostaRequest propostaRequest = new PropostaRequest("027.294.610-94",
                "google@example.org", "Google", "Endereco", BigDecimal.valueOf(42L));

        assertThrows(ErroApiException.class,
                () -> controller.enviaProposta(propostaRequest, UriComponentsBuilder.newInstance()));
    }

    @Test
    @DisplayName("Se o documento não estiver cadastrado não lança uma exceção")
    public void seDocumentoNaoEstaCadastradoRetornaProposta() {
        PropostaRequest propostaRequest = new PropostaRequest("100.200.610-94",
                "robot@example.org", "Robot", "Endereco", BigDecimal.valueOf(42L));

        ResponseEntity<PropostaResponse> propostaResult = controller.enviaProposta(propostaRequest,
                UriComponentsBuilder.newInstance());

        assertEquals(HttpStatus.CREATED, propostaResult.getStatusCode());
        assertTrue(propostaResult.hasBody());
        PropostaResponse body = propostaResult.getBody();
        assertEquals("Robot", body.getNome());
        assertNull(body.getId());
    }
}

