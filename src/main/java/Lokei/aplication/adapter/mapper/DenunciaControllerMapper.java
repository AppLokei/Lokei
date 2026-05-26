package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.req.DenunciaRequest;
import Lokei.aplication.adapter.dto.res.DenunciaResponse;
import Lokei.aplication.domain.entities.Denuncia;

public class DenunciaControllerMapper {
    public static Denuncia toDenuncia(DenunciaRequest request, Long usuarioId, Long anuncioId) {
        return new Denuncia(
                null,
                request.motivo(),
                request.descricao(),
                usuarioId,
                anuncioId
        );
    }

    public static DenunciaResponse toResponse(Denuncia denuncia) {
        return new DenunciaResponse(
                denuncia.getUsuarioId(),
                denuncia.getMotivo(),
                denuncia.getDescricao(),
                denuncia.getDataDenuncia()
        );
    }
}
