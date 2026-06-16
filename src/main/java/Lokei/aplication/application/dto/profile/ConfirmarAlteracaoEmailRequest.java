package Lokei.aplication.application.dto.profile;

import jakarta.validation.constraints.NotBlank;

public record ConfirmarAlteracaoEmailRequest(
        @NotBlank(message = "Token e obrigatorio.")
        String token
) {
}
