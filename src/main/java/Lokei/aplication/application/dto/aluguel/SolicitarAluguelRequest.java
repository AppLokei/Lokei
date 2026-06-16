package Lokei.aplication.application.dto.aluguel;

import jakarta.validation.constraints.NotBlank;

public record SolicitarAluguelRequest(
        @NotBlank(message = "Data de inicio e obrigatoria.")
        String dataInicio,
        @NotBlank(message = "Data de fim e obrigatoria.")
        String dataFim
) {
}
