package Lokei.aplication.adapters.dtos.req;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record FerramentaRequest(
        String nome,
        CategoriaEnum categoria
) {
}
