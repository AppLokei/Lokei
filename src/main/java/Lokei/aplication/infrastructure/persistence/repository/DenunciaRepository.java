package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.entities.DenunciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<DenunciaEntity, Long> {

    List<DenunciaEntity> findByAnuncio_Id(Long anuncioId);

    List<DenunciaEntity> findByStatus(StatusDenunciaEnum status);

    boolean existsByAnuncio_IdAndDenunciante_IdAndStatus(Long anuncioId, Long denuncianteId, StatusDenunciaEnum status);
}