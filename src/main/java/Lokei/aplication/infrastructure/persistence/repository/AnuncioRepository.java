package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long>, JpaSpecificationExecutor<AnuncioEntity> {

    @Query("SELECT a FROM AnuncioEntity a WHERE a.usuario.id = :usuarioId")
    Page<AnuncioEntity> findByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<List<AnuncioEntity>> findAllById(Long id);
}
