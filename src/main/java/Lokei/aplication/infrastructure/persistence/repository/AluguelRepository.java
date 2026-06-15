package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface AluguelRepository extends JpaRepository<AluguelEntity, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM AluguelEntity a
        WHERE a.anuncio.id = :anuncioId
        AND a.statusAluguel = 'EM_ANDAMENTO'
    """)
    boolean existsAluguelEmAndamento(Long anuncioId);

    @Query("SELECT a FROM AluguelEntity a WHERE a.anuncio.usuario.id = :usuarioId")
    Page<AluguelEntity> findByUsuarioId(Long usuarioId, Pageable pageable);

    List<AluguelEntity> findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(
            Long anuncioId,
            Collection<StatusAluguelEnum> statusAluguel
    );

    @Query("""
            select case when count(aluguel) > 0 then true else false end
            from AluguelEntity aluguel
            where aluguel.anuncio.id = :anuncioId
              and aluguel.statusAluguel in :status
              and aluguel.dataInicio <= :dataFim
              and aluguel.dataFim >= :dataInicio
            """)
    boolean existsReservaSobreposta(
            @Param("anuncioId") Long anuncioId,
            @Param("status") Collection<StatusAluguelEnum> status,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );
}
