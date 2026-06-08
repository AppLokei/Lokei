package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.auth.*;
import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.support.UsuarioMapperSupport;
import Lokei.aplication.infrastructure.config.SecurityProperties;
import Lokei.aplication.infrastructure.persistence.entity.TokenRecuperacaoSenha;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.papelUsuarioEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.TokenRecuperacaoSenhaRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.security.JwtService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticado;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class AutenticacaoService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CpfValidationService cpfValidationService;
    private final TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;
    private final SecurityProperties securityProperties;
    private final NotificacaoService notificacaoService;

    public AutenticacaoService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CpfValidationService cpfValidationService,
            TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository,
            SecurityProperties securityProperties,
            NotificacaoService notificacaoService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cpfValidationService = cpfValidationService;
        this.tokenRecuperacaoSenhaRepository = tokenRecuperacaoSenhaRepository;
        this.securityProperties = securityProperties;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public AuthResponse cadastrar(CadastroUsuarioRequest request) {
        if (!Boolean.TRUE.equals(request.aceitouTermos())) {
            throw new RegraDeNegocioException("O cadastro so pode ser concluido apos o aceite dos termos.");
        }
        validarSenha(request.senha());

        String email = normalizarEmail(request.email());
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new RegraDeNegocioException("Este e-mail ja esta cadastrado.");
        }
        if (usuarioRepository.existsByCpf(request.cpf())) {
            throw new RegraDeNegocioException("Este CPF ja esta associado a uma conta.");
        }
        if (!cpfValidationService.cpfRegular(request.cpf())) {
            throw new RegraDeNegocioException("CPF invalido para cadastro.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome().trim());
        usuario.setEmail(email);
        usuario.setCpf(request.cpf());
        usuario.setTelefone(request.telefone());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPapel(parsePapel(request.papel()));
        usuario.setTermosAceitos(true);
        usuario.setCpfValidado(true);
        usuario.setAtivo(true);
        usuario.setEmailVerificado(true);
        usuario.setEndereco(UsuarioMapperSupport.toEndereco(request.endereco()));

        Usuario salvo = usuarioRepository.save(usuario);
        UsuarioAutenticado principal = new UsuarioAutenticado(salvo);
        return new AuthResponse(jwtService.gerarToken(principal), "Bearer", UsuarioMapperSupport.toSessaoResponse(salvo));
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizarEmail(request.email()), request.senha())
        );
        UsuarioAutenticado principal = (UsuarioAutenticado) authentication.getPrincipal();
        return new AuthResponse(jwtService.gerarToken(principal), "Bearer", UsuarioMapperSupport.toSessaoResponse(principal.getUsuario()));
    }

    public MensagemResponse logout() {
        return new MensagemResponse("Logout realizado com sucesso.");
    }

    @Transactional
    public MensagemResponse esqueciSenha(EsqueciSenhaRequest request) {
        usuarioRepository.findByEmailIgnoreCase(normalizarEmail(request.email())).ifPresent(usuario -> {
            TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
            token.setUsuario(usuario);
            token.setToken(UUID.randomUUID().toString());
            token.setExpiracao(LocalDateTime.now().plusMinutes(securityProperties.passwordReset().expirationMinutes()));
            token.setUsado(false);
            tokenRecuperacaoSenhaRepository.save(token);
            notificacaoService.notificar(
                    usuario,
                    tipoNotificacaoEnum.RECUPERACAO_SENHA,
                    "Recuperacao de senha",
                    "Use o token " + token.getToken() + " para redefinir sua senha."
            );
        });
        return new MensagemResponse("Se o e-mail existir, uma instrucao de recuperacao foi registrada.");
    }

    @Transactional
    public MensagemResponse redefinirSenha(RedefinirSenhaRequest request) {
        validarSenha(request.novaSenha());
        TokenRecuperacaoSenha token = tokenRecuperacaoSenhaRepository.findByToken(request.token())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Token de recuperacao nao encontrado."));

        if (token.isUsado() || token.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Token de recuperacao expirado ou ja utilizado.");
        }

        Usuario usuario = token.getUsuario();
        usuario.setSenhaHash(passwordEncoder.encode(request.novaSenha()));
        token.setUsado(true);
        return new MensagemResponse("Senha redefinida com sucesso.");
    }

    private void validarSenha(String senha) {
        if (senha == null || senha.length() < 8 || !senha.matches(".*[A-Za-z].*") || !senha.matches(".*\\d.*")) {
            throw new RegraDeNegocioException("A senha deve conter no minimo 8 caracteres, incluindo letras e numeros.");
        }
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private papelUsuarioEnum parsePapel(String papel) {
        if (papel == null || papel.isBlank()) {
            return papelUsuarioEnum.LOCATARIO;
        }
        try {
            papelUsuarioEnum papelNormalizado = papelUsuarioEnum.valueOf(papel.trim().toUpperCase(Locale.ROOT));
            if (papelNormalizado == papelUsuarioEnum.ADMIN) {
                throw new RegraDeNegocioException("Nao e permitido criar usuarios administradores por este endpoint.");
            }
            return papelNormalizado;
        } catch (IllegalArgumentException exception) {
            throw new RegraDeNegocioException("Papel de usuario invalido.");
        }
    }
}
