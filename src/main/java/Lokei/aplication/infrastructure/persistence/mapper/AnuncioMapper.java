package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entity.ImagemEntity;

import java.util.ArrayList;
import java.util.List;

public class AnuncioMapper {
    public static Anuncio toDomain(AnuncioEntity entity) {
        Anuncio anuncio = new Anuncio(
                entity.getId(),
                entity.getDescricao(),
                entity.getValorDiario(),
                entity.getStatus(),
                FerramentaMapper.toDomain(entity.getFerramenta())
        );

        for (ImagemEntity imagemEntity : entity.getImagens()) {
            anuncio.adicionarImagem(ImagemMapper.toDomain(imagemEntity, anuncio));
        }

        return anuncio;
    }

    public static AnuncioEntity toEntity(Anuncio anuncio) {
        AnuncioEntity entity = new AnuncioEntity();

        entity.setId(anuncio.getId());
        entity.setDescricao(anuncio.getDescricao());
        entity.setValorDiario(anuncio.getValorDiario());
        entity.setDataCriacao(anuncio.getDataCriacao());
        entity.setStatus(anuncio.getStatus());
        entity.setFerramenta(FerramentaMapper.toEntity(anuncio.getFerramenta()));

        List<ImagemEntity> imagens = new ArrayList<>();
        for (Imagem imagem : anuncio.getImagens()) {
            imagens.add(ImagemMapper.toEntity(imagem, entity));
        }
        entity.setImagens(imagens);

        return entity;
    }
}
