package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.ImagemEntity;

public class ImagemMapper {
    public static Imagem toDomain(ImagemEntity entity){
        return new Imagem(
                entity.getId(),
                entity.getImagemUrl(),
                entity.getPublicId()
        );
    }

    public static ImagemEntity toEntity(Imagem imagem, AnuncioEntity anuncio){
        ImagemEntity entity = new ImagemEntity();

        entity.setId(imagem.getId());
        entity.setImagemUrl(imagem.getImagemUrl());
        entity.setPublicId(imagem.getPublicId());
        entity.setAnuncio(anuncio);

        return entity;
    }
}
