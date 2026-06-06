package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("SELECT u FROM UsuarioEntity u WHERE u.email = :email")
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.cpf = :cpf")
    Optional<UsuarioEntity> findByCpf(String cpf);
}
