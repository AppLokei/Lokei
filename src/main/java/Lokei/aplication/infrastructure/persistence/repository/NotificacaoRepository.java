package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.NotificacaoEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, Integer> {

    @EntityGraph(attributePaths = {"usuario"})
    List<NotificacaoEntity> findByUsuario_IdOrderByDataCriacaoDesc(Integer usuarioId);
}
