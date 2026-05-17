package Lokei.aplication.adapters.dtos.req;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record CriarFerramentaRequest(
        String nome,
        CategoriaEnum categoria
) {
}
