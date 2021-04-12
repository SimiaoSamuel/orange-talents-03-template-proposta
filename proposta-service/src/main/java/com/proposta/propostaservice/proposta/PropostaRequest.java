package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.annotation.CPFOrCNPJ;
import com.proposta.propostaservice.handler.ErroApiException;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class PropostaRequest {
    @NotBlank
    @CPFOrCNPJ
    private String documento;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nome;

    @NotBlank
    private String endereco;

    @NotNull
    @PositiveOrZero
    private BigDecimal salario;

    public PropostaRequest(@NotNull String documento, @NotBlank @Email String email, @NotBlank String nome, @NotBlank String endereco, @NotNull BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    /**
     * Converte esse dto para uma proposta
     * @return A proposta com os dados do dto
     */
    public Proposta toProposta(){
        documento = documento.replace(".","").replace("-","");
        return new Proposta(documento,email,nome,endereco,salario);
    }
}
