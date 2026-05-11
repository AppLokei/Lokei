package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.DenunciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaEntityRepository extends JpaRepository<DenunciaEntity, Long> {
}
