package Lokei.aplication.application.dto.aluguel;

import jakarta.validation.constraints.NotBlank;

public record ReprovarAluguelRequest(
        @NotBlank(message = "Motivo da reprovacao e obrigatorio.")
        String motivo
) {
}
