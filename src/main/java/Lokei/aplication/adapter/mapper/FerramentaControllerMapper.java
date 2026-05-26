package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.req.FerramentaRequest;
import Lokei.aplication.adapter.dto.res.FerramentaResponse;
import Lokei.aplication.domain.entities.Ferramenta;

public class FerramentaControllerMapper {
    public static FerramentaResponse toResponse(Ferramenta ferramenta) {
        return new FerramentaResponse(ferramenta.getId(), ferramenta.getNome(), ferramenta.getCategoria());
    }

    public static Ferramenta toFerramenta(FerramentaRequest request) {
        return new Ferramenta(null, request.nome(), request.categoria());
    }
}
