package com.pfc.thindesk.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EditarTecnicoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @Email(message = "Email inválido")
        String email,
        String telefone,
        String especialidade,
        boolean ativo) {
}
