package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Notificacao;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    @EntityGraph(attributePaths = {"usuario"})
    List<Notificacao> findByUsuario_IdOrderByDataCriacaoDesc(Integer usuarioId);
}
