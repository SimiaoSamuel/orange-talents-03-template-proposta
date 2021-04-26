package com.proposta.propostaservice.proposta;

import com.proposta.propostaservice.shared.annotation.CPFOrCNPJ;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

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

    public String getDocumento() {
        return documento;
    }

    /**
     * Converte esse dto para uma proposta
     * @return A proposta com os dados do dto
     */
    public Proposta toProposta(){
        documento = documento.replace(".","")
                .replace("-","").replace("/","");

        String docEncrypt = Encryptors.text("secret", "1c68578e74dc3ff8").encrypt(documento);
        return new Proposta(docEncrypt,email,nome,endereco,salario);
    }
}
