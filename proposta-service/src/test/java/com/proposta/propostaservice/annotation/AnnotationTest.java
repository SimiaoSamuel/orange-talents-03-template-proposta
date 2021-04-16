package com.proposta.propostaservice.annotation;

import com.proposta.propostaservice.biometria.BiometriaRequest;
import com.proposta.propostaservice.proposta.PropostaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnnotationTest {
    private LocalValidatorFactoryBean validator;
    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @BeforeEach
    private void setUpEach() {
        SpringConstraintValidatorFactory springConstraintValidatorFactory =
                new SpringConstraintValidatorFactory(
                        applicationContext.getAutowireCapableBeanFactory()
                );

        validator = new LocalValidatorFactoryBean();
        validator.setConstraintValidatorFactory(springConstraintValidatorFactory);
        validator.setApplicationContext(applicationContext);
        validator.afterPropertiesSet();
    }

    @Test
    public void biometriaCorretaTest(){
        BiometriaRequest request = new BiometriaRequest("U2FtdWVs");
        Set<ConstraintViolation<BiometriaRequest>> validate = validator.validate(request);

        assertTrue(validate.isEmpty());
    }

    @Test
    public void biometriaComBase64InvalidaTest(){
        BiometriaRequest request = new BiometriaRequest("samuel");
        Set<ConstraintViolation<BiometriaRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());
    }

    @Test
    public void cpfErrado(){
        PropostaRequest request = new PropostaRequest("430.886.770", "email@email.com",
                "nome", "endereco", BigDecimal.valueOf(1));

        Set<ConstraintViolation<PropostaRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());
    }

    @Test
    public void cpfCorretoTest(){
        PropostaRequest request = new PropostaRequest("430.886.770-39", "email@email.com",
                "nome", "endereco", BigDecimal.valueOf(1));

        Set<ConstraintViolation<PropostaRequest>> validate = validator.validate(request);

        assertTrue(validate.isEmpty());
    }


    @Test
    public void cnpjCorretoTest(){
        PropostaRequest request = new PropostaRequest("05.731.949/0001-85", "email@email.com",
                "nome", "endereco", BigDecimal.valueOf(1));

        Set<ConstraintViolation<PropostaRequest>> validate = validator.validate(request);

        assertTrue(validate.isEmpty());
    }

    @Test
    public void cnpjErradoTest(){
        PropostaRequest request = new PropostaRequest("05.731.949/000", "email@email.com",
                "nome", "endereco", BigDecimal.valueOf(1));

        Set<ConstraintViolation<PropostaRequest>> validate = validator.validate(request);

        assertFalse(validate.isEmpty());
    }
}
