package Lokei.aplication.adapter.dto.req;

import Lokei.aplication.domain.enums.CategoriaEnum;

import java.math.BigDecimal;

public record AnuncioFiltroRequest (
        String titulo,
        CategoriaEnum categoria,
        BigDecimal valorMin,
        BigDecimal valorMax
) {
}
