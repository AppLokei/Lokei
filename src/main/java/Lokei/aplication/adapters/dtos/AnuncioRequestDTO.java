package Lokei.aplication.adapters.dtos;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AnuncioRequestDTO (
        @NotBlank(message = "O titulo é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O valor da diaria é obrigatória")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que R$ 0,00")
        BigDecimal valorDiario,
        StatusAnuncioEnum status,

        @NotNull(message = "Selecione uma categoria para a ferramenta")
        CategoriaEnum categoria
) {
}
