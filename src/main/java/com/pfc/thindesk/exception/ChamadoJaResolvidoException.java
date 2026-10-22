package com.pfc.thindesk.exception;

public class ChamadoJaResolvidoException extends RuntimeException {

    public ChamadoJaResolvidoException(String id) {
        super("Chamado já resolvido e não pode ser alterado: " + id);
    }
}
