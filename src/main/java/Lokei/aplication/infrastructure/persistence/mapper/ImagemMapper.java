package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.ImagemEntity;

public class ImagemMapper {
    public static Imagem toDomain(ImagemEntity entity){
        return new Imagem(
          entity.getId(),
          entity.getImagemUrl()
        );
    }

    public static ImagemEntity toEntity(Imagem imagem){
        ImagemEntity entity = new ImagemEntity();

        entity.setId(imagem.getId());
        entity.setImagemUrl(imagem.getImagemUrl());

        return entity;
    }
}
