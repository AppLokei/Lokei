package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.res.AluguelResponse;
import Lokei.aplication.domain.entities.Aluguel;

public class AluguelControllerMapper {

    private AluguelControllerMapper() {
    }

    public static AluguelResponse toResponse(Aluguel aluguel) {
        return new AluguelResponse(
                aluguel.getId(),
                aluguel.getDataInicio(),
                aluguel.getDataFim(),
                aluguel.getValorTotal(),
                aluguel.getStatus(),
                aluguel.getAnuncioId(),
                aluguel.getLocatarioId()
        );
    }
}

