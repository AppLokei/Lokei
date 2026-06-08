package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Denuncia;
import Lokei.aplication.infrastructure.persistence.enums.statusDenunciaEnum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DenunciaRepository extends JpaRepository<Denuncia, Integer> {

    @EntityGraph(attributePaths = {"anuncio", "anuncio.proprietario", "denunciante", "administrador"})
    List<Denuncia> findByStatusOrderByDataCriacaoDesc(statusDenunciaEnum status);

    @EntityGraph(attributePaths = {"anuncio", "anuncio.proprietario", "denunciante", "administrador"})
    List<Denuncia> findAllByOrderByDataCriacaoDesc();
}
