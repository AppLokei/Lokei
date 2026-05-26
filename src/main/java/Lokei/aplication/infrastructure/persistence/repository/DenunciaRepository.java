package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<DenunciaEntity, Long> {
}
