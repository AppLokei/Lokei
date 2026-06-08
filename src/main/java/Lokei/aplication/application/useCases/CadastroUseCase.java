package Lokei.aplication.application.useCases;

import Lokei.aplication.infrastructure.exception.UsuarioException;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroUseCase {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public String cadastro(Usuario usuario){

        boolean emailJaCadastrado = usuarioRepo.existsByEmail(usuario.getEmail());
        boolean cpfJaCadastrado = usuarioRepo.existsByCpf(usuario.getCpf());
        if(emailJaCadastrado) throw new UsuarioException("Este e-mail já está cadastrado");
        if(cpfJaCadastrado) throw new UsuarioException("Este CPF já está associado a uma conta");

        usuario.validacaoDadosUsuario();
        usuarioRepo.save(usuario);
        return "Cadastro concluído com sucesso!";
    }

}
