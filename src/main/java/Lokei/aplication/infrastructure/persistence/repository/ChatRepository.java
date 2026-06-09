package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatEntity, Long> {

    Optional<ChatEntity> findByAnuncioIdAndLocatarioId(Long anuncioId, Long locatarioId);

    @Query("SELECT c FROM ChatEntity c WHERE c.locatario.id = :usuarioId OR c.locador.id = :usuarioId")
    List<ChatEntity> findAllByUsuarioId(@Param("usuarioId") Long usuarioId);
}
