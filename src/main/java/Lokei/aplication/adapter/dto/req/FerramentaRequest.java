package Lokei.aplication.adapter.dto.req;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record FerramentaRequest(
        String nome,
        CategoriaEnum categoria
) {
}
