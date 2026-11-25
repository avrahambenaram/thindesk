package com.pfc.thindesk.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        String telefone,
        String setor) {
}
