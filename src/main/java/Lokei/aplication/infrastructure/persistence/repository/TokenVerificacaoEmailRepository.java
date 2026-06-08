package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.TokenVerificacaoEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenVerificacaoEmailRepository extends JpaRepository<TokenVerificacaoEmail, Integer> {

    Optional<TokenVerificacaoEmail> findByToken(String token);
}
