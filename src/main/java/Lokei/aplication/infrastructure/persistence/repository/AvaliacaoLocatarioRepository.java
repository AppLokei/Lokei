package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoLocatarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AvaliacaoLocatarioRepository extends JpaRepository<AvaliacaoLocatarioEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AvaliacaoLocatarioEntity a WHERE a.aluguel.id = :aluguelId AND a.avaliador.id = :avaliadorId")
    boolean existsByAluguelIdAndAvaliadorId(Long aluguelId, Long avaliadorId);

    Page<AvaliacaoLocatarioEntity> findByAvaliadoId(Long avaliadoId, Pageable pageable);
}
