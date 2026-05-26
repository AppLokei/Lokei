package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AnuncioRepository extends JpaRepository<AnuncioEntity, Long>, JpaSpecificationExecutor<AnuncioEntity> {

}
