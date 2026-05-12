package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entity.ImagemEntity;

public class ImagemMapper {
    public static Imagem toDomain(ImagemEntity entity, Anuncio anuncio){
        return new Imagem(
                entity.getId(),
                entity.getImagemUrl(),
                anuncio
        );
    }

    public static ImagemEntity toEntity(Imagem imagem, AnuncioEntity anuncio){
        ImagemEntity entity = new ImagemEntity();

        entity.setId(imagem.getId());
        entity.setImagemUrl(imagem.getImagemUrl());
        entity.setAnuncio(anuncio);

        return entity;
    }
}
