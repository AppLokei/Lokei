package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnuncioEntityRepository extends JpaRepository<AnuncioEntity, Long> {

    List<AnuncioEntity> findByFerramenta_Categoria(CategoriaEnum categoria);
}
