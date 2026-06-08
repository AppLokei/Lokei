package Lokei.aplication.application.dto.anuncio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AtualizarAnuncioRequest(
        @NotBlank(message = "Titulo e obrigatorio.")
        String titulo,
        @NotBlank(message = "Descricao e obrigatoria.")
        String descricao,
        @NotNull(message = "Valor da diaria e obrigatorio.")
        @DecimalMin(value = "0.01", message = "O valor da diaria deve ser maior que R$ 0,00.")
        BigDecimal valorDiario,
        @NotBlank(message = "Categoria e obrigatoria.")
        String categoria,
        @NotEmpty(message = "O anuncio deve possuir ao menos uma foto.")
        List<Integer> imagemIds
) {
}
