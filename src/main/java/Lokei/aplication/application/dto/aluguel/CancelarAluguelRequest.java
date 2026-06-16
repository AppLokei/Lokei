package Lokei.aplication.application.dto.aluguel;

import jakarta.validation.constraints.NotBlank;

public record CancelarAluguelRequest(
        @NotBlank(message = "Motivo do cancelamento e obrigatorio.")
        String motivo
) {
}
