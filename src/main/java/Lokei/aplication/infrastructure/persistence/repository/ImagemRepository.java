package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entity.ImagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemRepository extends JpaRepository<ImagemEntity, Long> {

    List<ImagemEntity> findImagemEntitiesByAnuncio(AnuncioEntity anuncio);
    void deleteImagemEntitiesByAnuncioId(Long anuncioId);
    void deleteImagemEntitiesByIdAndAnuncioId(Long imagemId, Long anuncioId);
}


