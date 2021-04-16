package com.proposta.propostaservice.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.net.URL;

@Component
public class AnaliseService implements HealthIndicator {
    private static final String URI = "http://localhost:9999";
    private final Logger LOG = LoggerFactory.getLogger(AnaliseService.class);

    @Override
    public Health health() {
        try (Socket socket = new Socket(new URL(URI).getHost(),9999)){
            return Health.up().build();
        } catch (Exception e) {
            LOG.warn("Failed to connect to: {}",URI);
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}
