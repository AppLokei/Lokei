package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.FerramentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FerramentaRepository extends JpaRepository<FerramentaEntity, Long> {
}
