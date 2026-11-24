package com.pfc.thindesk.exception;

import org.springframework.http.HttpStatus;

public class RegraDeNegocioException extends RuntimeException {

    private final HttpStatus status;

    public RegraDeNegocioException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
