package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AluguelRepository extends JpaRepository<AluguelEntity, Long> {

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END
        FROM AluguelEntity a
        WHERE a.anuncio.id = :anuncioId
        AND a.statusAluguel = 'EM_ANDAMENTO'
    """)
    boolean existsAluguelEmAndamento(Long anuncioId);
}
