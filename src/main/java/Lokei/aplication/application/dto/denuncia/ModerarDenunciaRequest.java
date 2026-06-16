package Lokei.aplication.application.dto.denuncia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModerarDenunciaRequest(
        @NotNull(message = "Informe se a denuncia foi aprovada.")
        Boolean aprovada,
        @NotBlank(message = "Parecer administrativo e obrigatorio.")
        String parecer
) {
}
