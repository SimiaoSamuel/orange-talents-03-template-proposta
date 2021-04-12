package com.proposta.propostaservice.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class OfuscamentoUtilTest {

    @Test
    public void seDadoTiverTamanhoImparOfuscaTudoMenosPrimeiraLetra() {
        assertEquals("D************", OfuscamentoUtil.Ofuscar("Dado Sensivel"));
        assertEquals("4****", OfuscamentoUtil.Ofuscar("42422"));
    }

    @Test
    public void seDadoTiverTamanhoParOfuscaMetade(){
        assertEquals("4*", OfuscamentoUtil.Ofuscar("42"));
        assertEquals("**do", OfuscamentoUtil.Ofuscar("dado"));
    }

    @Test
    public void seDadoTiverTamanhoIgualAUmOfuscaEle(){
        assertEquals("*", OfuscamentoUtil.Ofuscar("4"));
    }

    @Test
    public void seDadoForVazioRetornaNulo() {
        assertNull(OfuscamentoUtil.Ofuscar(""));
    }
}

