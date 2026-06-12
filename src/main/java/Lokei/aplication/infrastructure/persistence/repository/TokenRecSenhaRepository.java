package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.TokenRecSenha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRecSenhaRepository extends JpaRepository<TokenRecSenha, Integer> {

    Optional<TokenRecSenha> findByToken(String token);
}