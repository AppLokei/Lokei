package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.req.FerramentaRequest;
import Lokei.aplication.adapters.dtos.res.FerramentaResponse;
import Lokei.aplication.domain.entities.Ferramenta;

public class FerramentaDTOMapper {
    public static FerramentaResponse toResponse(Ferramenta ferramenta) {
        return new FerramentaResponse(ferramenta.id(), ferramenta.nome(), ferramenta.categoria());
    }

    public static Ferramenta toFerramenta(FerramentaRequest request) {
        return new Ferramenta(null, request.nome(), request.categoria());
    }
}
