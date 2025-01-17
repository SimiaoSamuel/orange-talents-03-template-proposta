package com.proposta.propostaservice.shared.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> onConstraintValidationException(ConstraintViolationException e) {
        List<ErrorDto> error = new ArrayList<>();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.add(new ErrorDto(violation.getPropertyPath().toString(), violation.getMessage(), null));
        }
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErroApiException.class)
    public ResponseEntity<Object> handleErroApiException(ErroApiException ex){
        ErrorDto erro = new ErrorDto(ex);
        return ResponseEntity.status(ex.getStatus()).body(erro);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(buildErrorList(ex,status),status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(buildErrorList(ex,status),status);
    }

    public List<ErrorDto> buildErrorList(BindException ex, HttpStatus status) {
        List<ErrorDto> errors = new ArrayList<>();
        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        globalErrors.forEach(
                e -> errors.add(new ErrorDto(e.getObjectName(), e.getDefaultMessage(), status))
        );

        fieldErrors.forEach(
                e -> errors.add(new ErrorDto(e.getField(), e.getDefaultMessage(), status))
        );

        return errors;
    }

}
