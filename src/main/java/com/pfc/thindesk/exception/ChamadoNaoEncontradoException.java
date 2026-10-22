package com.pfc.thindesk.exception;

public class ChamadoNaoEncontradoException extends RuntimeException {

    public ChamadoNaoEncontradoException(String id) {
        super("Chamado não encontrado: " + id);
    }
}
