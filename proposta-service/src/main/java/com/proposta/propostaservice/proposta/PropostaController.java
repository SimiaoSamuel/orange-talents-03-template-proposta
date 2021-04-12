package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.util.OfuscamentoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    private final PropostaRepository propostaRepository;
    private final Logger LOG = LoggerFactory.getLogger(PropostaController.class);

    public PropostaController(PropostaRepository propostaRepository) {
        this.propostaRepository = propostaRepository;
    }

    @PostMapping
    public ResponseEntity<PropostaResponse> enviaProposta(@RequestBody @Valid PropostaRequest propostaRequest, UriComponentsBuilder uriBuilder){
        Proposta proposta = propostaRepository.save(propostaRequest.toProposta());

        String emailOfuscado = OfuscamentoUtil.Ofuscar(proposta.getEmail());
        LOG.info("Proposta com email: {} criada com sucesso!",emailOfuscado);

        PropostaResponse propostaResponse = new PropostaResponse(proposta);

        URI uri = uriBuilder.path("propostas/{id}").buildAndExpand(propostaResponse.getId()).toUri();
        return ResponseEntity.created(uri).body(propostaResponse);
    }
}
