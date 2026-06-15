package Lokei.aplication.application.usecases.usuario;

import Lokei.aplication.domain.exceptions.UsuarioException;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EditarUsuarioUseCase {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passEncoder;

    public String editar(Long id, UsuarioEntity usuario) {

        UsuarioEntity existente = usuarioRepo.findById(id)
                .orElseThrow(() -> new UsuarioException("Usuário não encontrado."));

        if (usuario.getCpf() != null && !usuario.getCpf().equals(existente.getCpf())) {
            throw new UsuarioException("O CPF não pode ser alterado após o cadastro.");
        }


        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new UsuarioException("O nome é obrigatório.");
        }


        if (usuario.getTelefone() == null || usuario.getTelefone().isBlank()) {
            throw new UsuarioException("O telefone é obrigatório.");
        }


        if (!usuario.getTelefone().matches("\\d{11}")) {
            throw new UsuarioException("Informe um telefone válido");
        }


        if (usuario.getEmail() != null && !usuario.getEmail().equals(existente.getEmail())) {
            boolean emailJaCadastrado = usuarioRepo.existsByEmail(usuario.getEmail());
            if (emailJaCadastrado) throw new UsuarioException("Este e-mail já está cadastrado.");
            existente.setEmail(usuario.getEmail());
        }

        existente.setNome(usuario.getNome());
        existente.setTelefone(usuario.getTelefone());

        usuarioRepo.save(existente);
        return "Perfil atualizado com sucesso.";
    }
}