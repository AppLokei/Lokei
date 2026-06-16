package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.ConversaChat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ConversaChatRepository extends JpaRepository<ConversaChat, Integer> {

    @EntityGraph(attributePaths = {"locador", "locatario", "aluguel", "aluguel.anuncio", "aluguel.anuncio.imagens"})
    List<ConversaChat> findByLocador_IdOrLocatario_IdOrderByDataCriacaoDesc(Integer locadorId, Integer locatarioId);

    @EntityGraph(attributePaths = {"locador", "locatario", "aluguel", "aluguel.anuncio", "aluguel.anuncio.imagens", "mensagens", "mensagens.remetente"})
    @Query("select c from ConversaChat c where c.id = :id")
    Optional<ConversaChat> findDetailedById(@Param("id") Integer id);

    Optional<ConversaChat> findByAluguel_Id(Integer aluguelId);
}
