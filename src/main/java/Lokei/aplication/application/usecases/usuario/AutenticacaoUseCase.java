package Lokei.aplication.application.usecases.usuario;


import Lokei.aplication.adapter.dto.req.EsqueciSenhaRequest;
import Lokei.aplication.adapter.dto.req.LoginRequest;
import Lokei.aplication.adapter.dto.req.RedefinirSenhaRequest;
import Lokei.aplication.adapter.dto.res.AuthResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponseLog;
import Lokei.aplication.adapter.dto.support.UsuarioMapperSupport;
import Lokei.aplication.domain.enums.tipoNotificacaoEnum;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.infrastructure.config.SecurityProperties;
import Lokei.aplication.infrastructure.persistence.entities.TokenRecSenha;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.repository.TokenRecSenhaRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.config.security.JwtService;
import Lokei.aplication.infrastructure.config.security.UsuarioAutenticado;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
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
public class AutenticacaoUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CpfValidationUseCase cpfValidationService;
    private final TokenRecSenhaRepository tokenRecuperacaoSenhaRepository;
    private final SecurityProperties securityProperties;
    private final NotificacaoUseCase notificacaoService;

    public AutenticacaoUseCase(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            CpfValidationUseCase cpfValidationService,
            TokenRecSenhaRepository tokenRecuperacaoSenhaRepository,
            SecurityProperties securityProperties,
            NotificacaoUseCase notificacaoService
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

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizarEmail(request.email()), request.senha())
        );
        UsuarioAutenticado principal = (UsuarioAutenticado) authentication.getPrincipal();
        return new AuthResponse(jwtService.gerarToken(principal), "Bearer", UsuarioMapperSupport.toSessaoResponse(principal.getUsuario()));
    }

    public MensagemResponseLog logout() {
        return new MensagemResponseLog("Logout realizado com sucesso.");
    }

    @Transactional
    public MensagemResponseLog esqueciSenha(EsqueciSenhaRequest request) {
        usuarioRepository.findByEmailIgnoreCase(normalizarEmail(request.email())).ifPresent(usuario -> {
            TokenRecSenha token = new TokenRecSenha();
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
        return new MensagemResponseLog("Se o e-mail existir, uma instrucao de recuperacao foi registrada.");
    }

    private void validarSenha(String senha) {

        if (senha == null
                || senha.length() < 8
                || !senha.matches(".*[A-Za-z].*")
                || !senha.matches(".*\\d.*")) {

            throw new RegraDeNegocioException(
                    "A senha deve conter no minimo 8 caracteres, incluindo letras e numeros."
            );
        }
    }

    @Transactional
    public MensagemResponseLog redefinirSenha(RedefinirSenhaRequest request) {
        validarSenha(request.novaSenha());
        TokenRecSenha token = tokenRecuperacaoSenhaRepository.findByToken(request.token())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Token de recuperacao nao encontrado."));

        if (token.isUsado() || token.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Token de recuperacao expirado ou ja utilizado.");
        }

        UsuarioEntity usuario = token.getUsuario();
        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        token.setUsado(true);
        return new MensagemResponseLog("Senha redefinida com sucesso.");
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}

