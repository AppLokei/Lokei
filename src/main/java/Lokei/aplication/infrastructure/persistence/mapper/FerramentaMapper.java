package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.infrastructure.persistence.entity.FerramentaEntity;

public class FerramentaMapper {
    public static FerramentaEntity toEntity(Ferramenta ferramenta) {
        return new FerramentaEntity(
                ferramenta.id(),
                ferramenta.nome(),
                ferramenta.categoria()
        );
    }

    public static Ferramenta toDomain(FerramentaEntity entity) {
        return new Ferramenta(entity.getId(), entity.getNome(), entity.getCategoria());
    }
}
