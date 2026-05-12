package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.time.LocalDate;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {

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
}
