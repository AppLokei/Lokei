package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;

public class AluguelMapper {

    private AluguelMapper() {
    }

    public static Aluguel toDomain(AluguelEntity entity) {
        return new Aluguel(
                entity.getId().longValue(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getValorTotal(),
                entity.getStatusAluguel(),
                entity.getAnuncio() != null ? entity.getAnuncio().getId() : null,
                entity.getLocatario() != null ? entity.getLocatario().getId() : null
        );
    }

    public static AluguelEntity toEntity(Aluguel aluguel) {
        AluguelEntity entity = new AluguelEntity();
        entity.setId(aluguel.getId());
        entity.setDataInicio(aluguel.getDataInicio());
        entity.setDataFim(aluguel.getDataFim());
        entity.setValorTotal(aluguel.getValorTotal());
        entity.setStatusAluguel(aluguel.getStatus());
        return entity;
    }
}

