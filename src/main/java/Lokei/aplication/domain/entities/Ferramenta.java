package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.CategoriaEnum;

public record Ferramenta(
        Long id,
        String nome,
        CategoriaEnum categoria
) {
}
