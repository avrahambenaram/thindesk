package com.pfc.thindesk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ChamadoNaoEncontradoException.class)
    public ProblemDetail tratarChamadoNaoEncontrado(ChamadoNaoEncontradoException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Chamado não encontrado");
        return problemDetail;
    }
}
