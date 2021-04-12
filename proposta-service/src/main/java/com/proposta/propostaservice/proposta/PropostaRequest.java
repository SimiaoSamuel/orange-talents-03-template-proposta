package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.annotation.CPFOrCNPJ;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
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
    @Positive
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
        return new Proposta(documento,email,nome,endereco,salario);
    }
}
