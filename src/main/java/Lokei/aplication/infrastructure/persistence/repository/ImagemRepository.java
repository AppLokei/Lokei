package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.ImagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImagemRepository extends JpaRepository<ImagemEntity, Long> {

    List<ImagemEntity> findImagemEntitiesByAnuncio(AnuncioEntity anuncio);
    @Modifying
    @Query("DELETE FROM ImagemEntity i WHERE i.id = :id")
    void deletarPorId(@Param("id") Long id);
}


