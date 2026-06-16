package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.TokenVerificacaoEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenVerificacaoEmailRepository extends JpaRepository<TokenVerificacaoEmail, Integer> {

    Optional<TokenVerificacaoEmail> findByToken(String token);
}
