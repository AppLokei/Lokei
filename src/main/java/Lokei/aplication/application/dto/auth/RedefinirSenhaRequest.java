package Lokei.aplication.application.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequest(
        @NotBlank(message = "Token e obrigatorio.")
        String token,
        @NotBlank(message = "Nova senha e obrigatoria.")
        String novaSenha
) {
}
