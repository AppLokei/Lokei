package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.MensagemChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensagemChatRepository extends JpaRepository<MensagemChat, Integer> {
}
