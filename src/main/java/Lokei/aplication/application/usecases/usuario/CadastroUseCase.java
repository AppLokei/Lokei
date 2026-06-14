package Lokei.aplication.application.usecases.usuario;


import Lokei.aplication.adapter.dto.res.CpfResponse;
import Lokei.aplication.domain.exceptions.UsuarioException;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CadastroUseCase {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Autowired
    private PasswordEncoder passEncoder;

    @Value("${tokenCpf}")
    private String token;

    public String validarStatusCpf(String cpf, String data) {

        try {
            String url = "http://ws.hubdodesenvolvedor.com.br/v2/cpf/"
                    + "?cpf=" + cpf
                    + "&data=" + data
                    + "&token=" + token;

            RestTemplate restTemplate = new RestTemplate();

            CpfResponse response = restTemplate.getForObject(
                    url,
                    CpfResponse.class);

            return response.getResult().getSituacao_cadastral();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao consultar CPF ", e);

        }
    }


    public String cadastro(UsuarioEntity usuario, String data) {

        boolean emailJaCadastrado = usuarioRepo.existsByEmail(usuario.getEmail());
        boolean cpfJaCadastrado = usuarioRepo.existsByCpf(usuario.getCpf());
        if (emailJaCadastrado) throw new UsuarioException("Este e-mail já está cadastrado");
        if (cpfJaCadastrado) throw new UsuarioException("Este CPF já está associado a uma conta");

        usuario.validacaoDadosUsuario();
        String situacaoCadastral = validarStatusCpf(usuario.getCpf(), data);

        if (!"REGULAR".equals(situacaoCadastral)) {
            return "Esse cpf não se encontra em situação regular.";
        }

        usuario.setSenha(
                passEncoder.encode(usuario.getSenha()));

        usuarioRepo.save(usuario);
        return "Cadastro concluído com sucesso!";
    }

}