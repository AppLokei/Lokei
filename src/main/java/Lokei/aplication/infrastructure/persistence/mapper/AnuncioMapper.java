package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.ImagemEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

import java.util.ArrayList;
import java.util.List;

public class AnuncioMapper {
    public static Anuncio toDomain(AnuncioEntity entity) {

        List<Imagem> imagens = new ArrayList<>();
        for (ImagemEntity imagemEntity : entity.getImagens()) {
            imagens.add(ImagemMapper.toDomain(imagemEntity));
        }

        return new Anuncio(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getValorDiario(),
                entity.getStatus(),
                FerramentaMapper.toDomain(entity.getFerramenta()),
                imagens,
                entity.getUsuario().getId()
        );
    }

    public static AnuncioEntity toEntity(Anuncio anuncio) {
        AnuncioEntity entity = new AnuncioEntity();

        entity.setId(anuncio.getId());
        entity.setTitulo(anuncio.getTitulo());
        entity.setDescricao(anuncio.getDescricao());
        entity.setValorDiario(anuncio.getValorDiario());
        entity.setDataCriacao(anuncio.getDataCriacao());
        entity.setStatus(anuncio.getStatus());
        entity.setFerramenta(FerramentaMapper.toEntity(anuncio.getFerramenta()));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(anuncio.getUsuarioId());
        entity.setUsuario(usuario);

        List<ImagemEntity> imagens = new ArrayList<>();
        for (Imagem imagem : anuncio.getImagens()) {
            imagens.add(ImagemMapper.toEntity(imagem, entity));
        }
        entity.setImagens(imagens);

        return entity;
    }
}
