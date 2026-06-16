package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.enums.tipoAvaliacaoEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    @EntityGraph(attributePaths = {"autor"})
    List<Avaliacao> findByAnuncio_IdAndTipoOrderByDataCriacaoDesc(Integer anuncioId, tipoAvaliacaoEnum tipo);

    @EntityGraph(attributePaths = {"autor"})
    List<Avaliacao> findByAlvoUsuario_IdAndTipoOrderByDataCriacaoDesc(Integer usuarioId, tipoAvaliacaoEnum tipo);

    boolean existsByAluguel_IdAndTipoAndAutor_Id(Integer aluguelId, tipoAvaliacaoEnum tipo, Integer autorId);
}
