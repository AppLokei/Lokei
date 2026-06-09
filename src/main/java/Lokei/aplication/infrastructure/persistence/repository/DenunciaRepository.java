package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<DenunciaEntity, Long> {

    List<DenunciaEntity> findByAnuncioId(Long anuncioId);

    List<DenunciaEntity> findByStatus(StatusDenunciaEnum status);

    boolean existsByAnuncioIdAndDenuncianteIdAndStatus(Long anuncioId, Long denuncianteId, StatusDenunciaEnum status);
}
