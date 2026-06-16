package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.denuncia.CriarDenunciaRequest;
import Lokei.aplication.application.dto.denuncia.DenunciaResponse;
import Lokei.aplication.application.dto.denuncia.ModerarDenunciaRequest;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Denuncia;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.motivoDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.enums.papelUsuarioEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.DenunciaRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class DenunciaService {

    private final DenunciaRepository denunciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnuncioService anuncioService;
    private final NotificacaoService notificacaoService;

    public DenunciaService(
            DenunciaRepository denunciaRepository,
            UsuarioRepository usuarioRepository,
            AnuncioService anuncioService,
            NotificacaoService notificacaoService
    ) {
        this.denunciaRepository = denunciaRepository;
        this.usuarioRepository = usuarioRepository;
        this.anuncioService = anuncioService;
        this.notificacaoService = notificacaoService;
    }

    @Transactional
    public DenunciaResponse criar(Integer usuarioId, Integer anuncioId, CriarDenunciaRequest request) {
        Usuario denunciante = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
        Anuncio anuncio = anuncioService.buscarAnuncioDetalhado(anuncioId);

        Denuncia denuncia = new Denuncia();
        denuncia.setAnuncio(anuncio);
        denuncia.setDenunciante(denunciante);
        denuncia.setMotivo(parseMotivo(request.motivo()));
        denuncia.setDescricao(request.descricao().trim());
        denuncia.setStatus(statusDenunciaEnum.EM_ANALISE);
        denuncia.setImagens(request.imagens() == null ? List.of() : request.imagens());
        Denuncia salva = denunciaRepository.save(denuncia);

        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<DenunciaResponse> listar(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
        if (usuario.getPapel() != papelUsuarioEnum.ADMIN) {
            throw new AcessoNegadoException("Apenas administradores podem listar denuncias.");
        }
        return denunciaRepository.findAllByOrderByDataCriacaoDesc().stream().map(this::toResponse).toList();
    }

    @Transactional
    public DenunciaResponse moderar(Integer usuarioId, Integer denunciaId, ModerarDenunciaRequest request) {
        Usuario administrador = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
        if (administrador.getPapel() != papelUsuarioEnum.ADMIN) {
            throw new AcessoNegadoException("Apenas administradores podem moderar denuncias.");
        }

        Denuncia denuncia = denunciaRepository.findById(denunciaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Denuncia nao encontrada."));

        denuncia.setAdministrador(administrador);
        denuncia.setParecerAdministrativo(request.parecer().trim());
        denuncia.setDataDecisao(LocalDateTime.now());
        denuncia.setStatus(Boolean.TRUE.equals(request.aprovada()) ? statusDenunciaEnum.APROVADA : statusDenunciaEnum.RECUSADA);

        if (denuncia.getStatus() == statusDenunciaEnum.APROVADA) {
            anuncioService.desativarPorDenuncia(denuncia.getAnuncio());
            notificacaoService.notificar(
                    denuncia.getAnuncio().getProprietario(),
                    tipoNotificacaoEnum.DENUNCIA_MODERADA,
                    "Anuncio desativado por denuncia",
                    "Seu anuncio foi desativado apos analise da denuncia: " + denuncia.getMotivo().name() + "."
            );
        }
        notificacaoService.notificar(
                denuncia.getDenunciante(),
                tipoNotificacaoEnum.DENUNCIA_MODERADA,
                "Denuncia analisada",
                Boolean.TRUE.equals(request.aprovada())
                        ? "Sua denuncia foi aprovada e o anuncio foi desativado."
                        : "Sua denuncia nao foi aprovada apos analise administrativa."
        );
        return toResponse(denuncia);
    }

    private motivoDenunciaEnum parseMotivo(String motivo) {
        try {
            return motivoDenunciaEnum.valueOf(motivo.trim().toUpperCase(Locale.ROOT));
        } catch (Exception exception) {
            throw new RegraDeNegocioException("Motivo de denuncia invalido.");
        }
    }

    private DenunciaResponse toResponse(Denuncia denuncia) {
        return new DenunciaResponse(
                denuncia.getId(),
                denuncia.getAnuncio().getId(),
                denuncia.getAnuncio().getTitulo(),
                denuncia.getMotivo().name(),
                denuncia.getDescricao(),
                denuncia.getStatus().name(),
                denuncia.getDenunciante() != null ? denuncia.getDenunciante().getNome() : null,
                denuncia.getAdministrador() != null ? denuncia.getAdministrador().getNome() : null,
                denuncia.getParecerAdministrativo(),
                denuncia.getImagens() == null ? List.of() : List.copyOf(denuncia.getImagens()),
                DataFormatUtils.formatarDataHora(denuncia.getDataCriacao()),
                DataFormatUtils.formatarDataHora(denuncia.getDataDecisao())
        );
    }
}
