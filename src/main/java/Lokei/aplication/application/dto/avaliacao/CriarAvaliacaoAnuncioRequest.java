package Lokei.aplication.application.dto.avaliacao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarAvaliacaoAnuncioRequest(
        @NotNull(message = "Aluguel e obrigatorio.")
        Integer aluguelId,
        @NotNull(message = "Nota e obrigatoria.")
        @Min(value = 1, message = "Nota minima e 1.")
        @Max(value = 5, message = "Nota maxima e 5.")
        Integer nota,
        @NotBlank(message = "Comentario e obrigatorio.")
        String comentario
) {
}
