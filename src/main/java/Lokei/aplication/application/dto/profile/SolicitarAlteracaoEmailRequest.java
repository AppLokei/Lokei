package Lokei.aplication.application.dto.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SolicitarAlteracaoEmailRequest(
        @NotBlank(message = "Novo e-mail e obrigatorio.")
        @Email(message = "Informe um e-mail valido.")
        String novoEmail
) {
}
