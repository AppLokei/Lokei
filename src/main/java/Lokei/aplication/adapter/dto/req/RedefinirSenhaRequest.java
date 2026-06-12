package Lokei.aplication.adapter.dto.req;

import jakarta.validation.constraints.NotBlank;

public record RedefinirSenhaRequest(
        @NotBlank(message = "Token e obrigatorio.")
        String token,
        @NotBlank(message = "Nova senha e obrigatoria.")
        String novaSenha
) {
}
