package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {

    @EntityGraph(attributePaths = {"anuncio", "anuncio.imagens", "anuncio.proprietario", "anuncio.proprietario.endereco", "locatario"})
    @Query("select a from Aluguel a where a.id = :id")
    Optional<Aluguel> findDetailedById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"anuncio", "anuncio.imagens", "anuncio.proprietario", "locatario"})
    List<Aluguel> findByLocatario_IdOrderByDataCriacaoDesc(Integer locatarioId);

    @EntityGraph(attributePaths = {"anuncio", "anuncio.imagens", "anuncio.proprietario", "locatario"})
    List<Aluguel> findByAnuncio_Proprietario_IdOrderByDataCriacaoDesc(Integer proprietarioId);

    List<Aluguel> findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(
            Integer anuncioId,
            Collection<statusAluguelEnum> statusAluguel
    );

    @Query("""
            select case when count(aluguel) > 0 then true else false end
            from Aluguel aluguel
            where aluguel.anuncio.id = :anuncioId
              and aluguel.statusAluguel in :status
              and aluguel.dataInicio <= :dataFim
              and aluguel.dataFim >= :dataInicio
            """)
    boolean existsReservaSobreposta(
            @Param("anuncioId") Integer anuncioId,
            @Param("status") Collection<statusAluguelEnum> status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    @Query("""
            select case when count(aluguel) > 0 then true else false end
            from Aluguel aluguel
            where aluguel.anuncio.id = :anuncioId
              and aluguel.id <> :aluguelId
              and aluguel.statusAluguel in :status
              and aluguel.dataInicio <= :dataFim
              and aluguel.dataFim >= :dataInicio
            """)
    boolean existsReservaSobrepostaExceto(
            @Param("anuncioId") Integer anuncioId,
            @Param("aluguelId") Integer aluguelId,
            @Param("status") Collection<statusAluguelEnum> status,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    boolean existsByAnuncio_IdAndStatusAluguel(Integer anuncioId, statusAluguelEnum statusAluguel);

    boolean existsByAnuncio_Id(Integer anuncioId);

    List<Aluguel> findByStatusAluguelIn(Collection<statusAluguelEnum> statusAluguel);
}
