package Lokei.aplication.adapters.dtos.req;

import Lokei.aplication.adapters.dtos.res.ImagemResponseDTO;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record AtualizarAnuncioRequest(
        @NotNull
        FerramentaRequest ferramenta,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotNull(message = "O valor da diaria é obrigatória")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que R$ 0,00")
        BigDecimal valorDiario,

        @NotNull
        StatusAnuncioEnum status,

        List<ImagemResponseDTO> imagens
) {
}
