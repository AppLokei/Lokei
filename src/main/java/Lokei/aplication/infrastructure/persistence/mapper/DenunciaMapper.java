package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;

public class DenunciaMapper {

    public static Denuncia toDomain(DenunciaEntity entity) {
        return new Denuncia(
                entity.getId(),
                entity.getMotivo(),
                entity.getDescricao(),
                entity.getDataDenuncia(),
                entity.getStatus(),
                entity.getAnuncioId(),
                entity.getDenuncianteId()
        );
    }

    public static DenunciaEntity toEntity(Denuncia denuncia) {
        DenunciaEntity entity = new DenunciaEntity();
        entity.setId(denuncia.getId());
        entity.setMotivo(denuncia.getMotivo());
        entity.setDescricao(denuncia.getDescricao());
        entity.setDataDenuncia(denuncia.getDataDenuncia());
        entity.setStatus(denuncia.getStatus());
        entity.setAnuncioId(denuncia.getAnuncioId());
        entity.setDenuncianteId(denuncia.getDenuncianteId());
        return entity;
    }
}
