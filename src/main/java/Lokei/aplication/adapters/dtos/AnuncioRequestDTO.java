package Lokei.aplication.adapters.dtos;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

import java.math.BigDecimal;

public record AnuncioRequestDTO (
        String titulo,
        String descricao,
        BigDecimal valorDiario,
        StatusAnuncioEnum status,
        CategoriaEnum categoria
) {
}
