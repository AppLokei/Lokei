package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.infrastructure.persistence.entities.FerramentaEntity;

public class FerramentaMapper {
    public static FerramentaEntity toEntity(Ferramenta ferramenta) {
        return new FerramentaEntity(
                ferramenta.getId(),
                ferramenta.getNome(),
                ferramenta.getCategoria()
        );
    }

    public static Ferramenta toDomain(FerramentaEntity entity) {
        return new Ferramenta(
                entity.getId(),
                entity.getNome(),
                entity.getCategoria()
        );
    }
}
