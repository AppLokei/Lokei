package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.req.CriarFerramentaRequest;
import Lokei.aplication.adapters.dtos.res.CriarFerramentaResponse;
import Lokei.aplication.domain.entities.Ferramenta;

public class FerramentaDTOMapper {
    public static CriarFerramentaResponse toResponse(Ferramenta ferramenta) {
        return new CriarFerramentaResponse(ferramenta.id(), ferramenta.nome(), ferramenta.categoria());
    }

    public static Ferramenta toFerramenta(CriarFerramentaRequest request) {
        return new Ferramenta(null, request.nome(), request.categoria());
    }
}
