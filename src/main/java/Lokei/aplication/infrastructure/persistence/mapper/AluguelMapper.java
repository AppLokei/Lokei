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
                entity.getAnuncio().getId()
        );
    }
}

