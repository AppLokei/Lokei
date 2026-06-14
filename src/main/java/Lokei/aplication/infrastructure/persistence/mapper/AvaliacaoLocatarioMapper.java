package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.AvaliacaoLocatario;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoLocatarioEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

public class AvaliacaoLocatarioMapper {

    private AvaliacaoLocatarioMapper() {}

    public static AvaliacaoLocatario toDomain(AvaliacaoLocatarioEntity entity) {
        if (entity == null) return null;
        return new AvaliacaoLocatario(
                entity.getId(),
                entity.getNota(),
                entity.getComentario(),
                entity.getDataCriacao(),
                entity.getAluguel() != null ? entity.getAluguel().getId().longValue() : null,
                entity.getAvaliador() != null ? entity.getAvaliador().getId() : null,
                entity.getAvaliado() != null ? entity.getAvaliado().getId() : null
        );
    }

    public static AvaliacaoLocatarioEntity toEntity(AvaliacaoLocatario domain) {
        if (domain == null) return null;

        AvaliacaoLocatarioEntity entity = new AvaliacaoLocatarioEntity();
        entity.setId(domain.getId());
        entity.setNota(domain.getNota());
        entity.setComentario(domain.getComentario());
        entity.setDataCriacao(domain.getDataCriacao());

        if (domain.getAluguelId() != null) {
            AluguelEntity aluguel = new AluguelEntity();
            aluguel.setId(domain.getAluguelId());
            entity.setAluguel(aluguel);
        }

        if (domain.getAvaliadorId() != null) {
            UsuarioEntity avaliador = new UsuarioEntity();
            avaliador.setId(domain.getAvaliadorId());
            entity.setAvaliador(avaliador);
        }

        if (domain.getAvaliadoId() != null) {
            UsuarioEntity avaliado = new UsuarioEntity();
            avaliado.setId(domain.getAvaliadoId());
            entity.setAvaliado(avaliado);
        }

        return entity;
    }
}
