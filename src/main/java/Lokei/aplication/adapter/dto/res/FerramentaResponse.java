package Lokei.aplication.adapter.dto.res;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record FerramentaResponse(
        Long id,
        String nome,
        CategoriaEnum categoria
) {
}
