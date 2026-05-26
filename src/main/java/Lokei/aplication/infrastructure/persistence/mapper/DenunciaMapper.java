package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

public class DenunciaMapper {
    public static Denuncia toDomain(DenunciaEntity entity) {
        return new Denuncia(
                entity.getId(),
                entity.getMotivo(),
                entity.getDescricao(),
                entity.getUsuario().getId(),
                entity.getAnuncio().getId()
        );
    }

    public static DenunciaEntity toEntity(Denuncia denuncia) {
        DenunciaEntity entity = new DenunciaEntity();

        entity.setId(denuncia.getId());
        entity.setMotivo(denuncia.getMotivo());
        entity.setDescricao(denuncia.getDescricao());
        entity.setDataDenuncia(denuncia.getDataDenuncia());

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(denuncia.getUsuarioId());

        AnuncioEntity anuncio = new AnuncioEntity();
        anuncio.setId(denuncia.getAnuncioId());

        entity.setUsuario(usuario);
        entity.setAnuncio(anuncio);

        return entity;
    }

}
