package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.res.DenunciaResponse;
import Lokei.aplication.domain.entities.Denuncia;

public class DenunciaControllerMapper {

    public static DenunciaResponse toResponse(Denuncia denuncia) {
        return new DenunciaResponse(
                denuncia.getId(),
                denuncia.getMotivo(),
                denuncia.getDescricao(),
                denuncia.getDataDenuncia(),
                denuncia.getStatus(),
                denuncia.getAnuncioId(),
                denuncia.getDenuncianteId()
        );
    }
}
