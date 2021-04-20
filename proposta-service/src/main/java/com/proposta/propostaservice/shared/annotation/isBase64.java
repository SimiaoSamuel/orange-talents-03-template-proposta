package com.proposta.propostaservice.shared.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ReportAsSingleViolation
@Pattern(regexp = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
public @interface isBase64 {
    String message() default "Biometria não foi enviada no formato correto";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
