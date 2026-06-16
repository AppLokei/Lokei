package Lokei.aplication.application.service;

import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.papelUsuarioEnum;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "test"})
public class AdminBootstrapService implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminBootstrapService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.findByEmailIgnoreCase("admin@lokei.local").isPresent()) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setNome("Administrador Lokei");
        admin.setEmail("admin@lokei.local");
        admin.setCpf("39053344705");
        admin.setTelefone("61999999999");
        admin.setSenhaHash(passwordEncoder.encode("Admin1234"));
        admin.setPapel(papelUsuarioEnum.ADMIN);
        admin.setTermosAceitos(true);
        admin.setCpfValidado(true);
        admin.setAtivo(true);
        admin.setEmailVerificado(true);
        usuarioRepository.save(admin);
    }
}
