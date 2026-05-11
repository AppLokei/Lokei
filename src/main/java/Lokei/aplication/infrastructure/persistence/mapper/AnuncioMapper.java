package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;

public class AnuncioMapper {
    public static Anuncio toDomain(AnuncioEntity entity) {
        return new Anuncio(
                entity.getId(),
                entity.getTitulo(),
                entity.getDescricao(),
                entity.getValorDiario(),
                entity.getStatus(),
                entity.getCategoria()
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
        entity.setCategoria(anuncio.getCategoria());

        return entity;
    }
}
