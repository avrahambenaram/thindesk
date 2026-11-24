package com.pfc.thindesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CriarUsuarioRequest(
        @NotBlank(message = "O login é obrigatório")
        String username,
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String senha,
        @NotEmpty(message = "Informe ao menos um perfil")
        List<String> roles) {
}
