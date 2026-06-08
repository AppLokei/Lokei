package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.dto.profile.*;
import Lokei.aplication.application.support.UsuarioMapperSupport;
import Lokei.aplication.infrastructure.config.SecurityProperties;
import Lokei.aplication.infrastructure.persistence.entity.Endereco;
import Lokei.aplication.infrastructure.persistence.entity.TokenVerificacaoEmail;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.TokenVerificacaoEmailRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final TokenVerificacaoEmailRepository tokenVerificacaoEmailRepository;
    private final SecurityProperties securityProperties;
    private final NotificacaoService notificacaoService;
    private final CepService cepService;

    public PerfilService(
            UsuarioRepository usuarioRepository,
            TokenVerificacaoEmailRepository tokenVerificacaoEmailRepository,
            SecurityProperties securityProperties,
            NotificacaoService notificacaoService,
            CepService cepService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.tokenVerificacaoEmailRepository = tokenVerificacaoEmailRepository;
        this.securityProperties = securityProperties;
        this.notificacaoService = notificacaoService;
        this.cepService = cepService;
    }

    @Transactional(readOnly = true)
    public PerfilResponse consultarPerfil(Integer usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        return UsuarioMapperSupport.toPerfilResponse(usuario);
    }

    @Transactional
    public PerfilResponse atualizarPerfil(Integer usuarioId, AtualizarPerfilRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        usuario.setNome(request.nome().trim());
        usuario.setTelefone(request.telefone());

        if (request.endereco() != null) {
            Endereco endereco = usuario.getEndereco();
            if (endereco == null) {
                usuario.setEndereco(UsuarioMapperSupport.toEndereco(request.endereco()));
            } else {
                UsuarioMapperSupport.copyEndereco(endereco, request.endereco());
            }
        }

        return UsuarioMapperSupport.toPerfilResponse(usuario);
    }

    @Transactional
    public MensagemResponse solicitarAlteracaoEmail(Integer usuarioId, SolicitarAlteracaoEmailRequest request) {
        Usuario usuario = buscarUsuario(usuarioId);
        String novoEmail = request.novoEmail().trim().toLowerCase(Locale.ROOT);

        if (usuario.getEmail().equalsIgnoreCase(novoEmail)) {
            throw new RegraDeNegocioException("O novo e-mail deve ser diferente do atual.");
        }
        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(novoEmail, usuarioId)) {
            throw new RegraDeNegocioException("Este e-mail ja esta cadastrado.");
        }

        TokenVerificacaoEmail token = new TokenVerificacaoEmail();
        token.setUsuario(usuario);
        token.setNovoEmail(novoEmail);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiracao(LocalDateTime.now().plusMinutes(securityProperties.passwordReset().expirationMinutes()));
        token.setUsado(false);
        tokenVerificacaoEmailRepository.save(token);

        usuario.setEmailPendente(novoEmail);
        notificacaoService.notificar(
                usuario,
                tipoNotificacaoEnum.ALTERACAO_EMAIL,
                "Confirmacao de alteracao de e-mail",
                "Use o token " + token.getToken() + " para confirmar o novo e-mail."
        );
        return new MensagemResponse("Solicitacao de alteracao de e-mail registrada.");
    }

    @Transactional
    public MensagemResponse confirmarAlteracaoEmail(Integer usuarioId, ConfirmarAlteracaoEmailRequest request) {
        TokenVerificacaoEmail token = tokenVerificacaoEmailRepository.findByToken(request.token())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Token de verificacao nao encontrado."));

        if (token.isUsado() || token.getExpiracao().isBefore(LocalDateTime.now())) {
            throw new RegraDeNegocioException("Token de verificacao expirado ou ja utilizado.");
        }
        if (!token.getUsuario().getId().equals(usuarioId)) {
            throw new RegraDeNegocioException("Este token nao pertence ao usuario autenticado.");
        }
        if (usuarioRepository.existsByEmailIgnoreCaseAndIdNot(token.getNovoEmail(), usuarioId)) {
            throw new RegraDeNegocioException("Este e-mail ja esta cadastrado.");
        }

        Usuario usuario = token.getUsuario();
        usuario.setEmail(token.getNovoEmail());
        usuario.setEmailPendente(null);
        token.setUsado(true);
        return new MensagemResponse("E-mail atualizado com sucesso.");
    }

    public ConsultaCepResponse consultarCep(String cep) {
        return cepService.consultar(cep);
    }

    private Usuario buscarUsuario(Integer usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
    }
}
