package com.pfc.thindesk.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastrarTecnicoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @Email(message = "Email inválido")
        String email,
        String telefone,
        String especialidade,
        @NotBlank(message = "O login é obrigatório")
        String username,
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha) {
}
