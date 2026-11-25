package com.pfc.thindesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResolverChamadoRequest(
        @NotBlank(message = "O motivo da resolução é obrigatório")
        @Size(max = 2000, message = "O motivo deve ter no máximo 2000 caracteres")
        String motivo) {
}
