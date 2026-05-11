package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.AluguelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AluguelEntityRepository extends JpaRepository<AluguelEntity, Integer> {
}
