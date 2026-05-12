package Lokei.aplication.adapters.dtos.res;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AnuncioResponseDTO (
        Long id,
        String titulo,
        String descricao,
        BigDecimal valorDiario,
        LocalDateTime dataCriacao,
        StatusAnuncioEnum status,
        CategoriaEnum categoria
) {
}
