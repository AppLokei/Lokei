package Lokei.aplication.adapters.dtos.res;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record CriarFerramentaResponse(
        Long id,
        String nome,
        CategoriaEnum categoria
) {
}
