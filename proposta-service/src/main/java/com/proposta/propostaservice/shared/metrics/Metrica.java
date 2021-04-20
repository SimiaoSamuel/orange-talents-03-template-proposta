package com.proposta.propostaservice.shared.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.keycloak.common.util.Time;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
public class Metrica {

    private final MeterRegistry meterRegistry;

    public Metrica(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public void adicionaPropostaPrometheus() {
        Counter contadorDePropostasCriadas = this.meterRegistry
                .counter("propostas_criadas");

        contadorDePropostasCriadas.increment();
    }

    public void temporizadorProposta(Long start){
        Timer timerConsultarProposta = this.meterRegistry.timer("consultar_propostas");
        timerConsultarProposta.record(System.currentTimeMillis() - start, TimeUnit.MILLISECONDS);
    }

    public void biometriaContador() {
        Counter contadorDePropostasCriadas = this.meterRegistry
                .counter("biometria_criadas");

        contadorDePropostasCriadas.increment();
    }
}
