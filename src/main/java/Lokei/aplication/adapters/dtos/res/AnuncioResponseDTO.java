package Lokei.aplication.adapters.dtos.res;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AnuncioResponseDTO (
        Long id,
        FerramentaResponse ferramentaResponse,
        String descricao,
        BigDecimal valorDiario,
        LocalDateTime dataCriacao,
        StatusAnuncioEnum status,
        List<ImagemResponseDTO> imagens
) {
}
