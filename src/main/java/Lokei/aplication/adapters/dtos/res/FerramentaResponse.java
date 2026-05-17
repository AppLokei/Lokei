package Lokei.aplication.adapters.dtos.res;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record FerramentaResponse(
        Long id,
        String nome,
        CategoriaEnum categoria
) {
}
