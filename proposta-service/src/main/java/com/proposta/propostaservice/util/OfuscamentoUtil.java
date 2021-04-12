package com.proposta.propostaservice.util;

import java.util.Collections;

public class OfuscamentoUtil {
    /**
     *
     * @param dadoSensivel - Informação que você quer ofuscar no formato de String
     * @return Informação ofuscada
     */
    public static String Ofuscar(String dadoSensivel){
        if(dadoSensivel.isBlank())
            return null;

        int tamanhoTexto = dadoSensivel.length();
        char primeiraLetra = dadoSensivel.charAt(0);

        if(tamanhoTexto % 2 == 0 && tamanhoTexto > 2){
            String dadosVisiveis = dadoSensivel.substring(tamanhoTexto / 2);
            int quantidadeDeCaracteresOfuscados = (tamanhoTexto/2);
            String dadosOfuscados = String
                    .join("", Collections.nCopies(quantidadeDeCaracteresOfuscados,"*"));

            return dadosOfuscados + dadosVisiveis;
        }
        else if(tamanhoTexto == 1)
            return "*";

        return primeiraLetra + String.join("", Collections.nCopies(tamanhoTexto - 1 ,"*"));
    }
}
