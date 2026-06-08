package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Integer id);
}