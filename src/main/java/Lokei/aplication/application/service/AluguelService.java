package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.aluguel.*;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoAvaliacaoEnum;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

@Service
public class AluguelService {

    private static final Set<statusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            statusAluguelEnum.EM_APROVACAO,
            statusAluguelEnum.CONFIRMADO,
            statusAluguelEnum.ATIVO
    );

    private final AluguelRepository aluguelRepository;
    private final UsuarioRepository usuarioRepository;
    private final AnuncioService anuncioService;
    private final NotificacaoService notificacaoService;
    private final AvaliacaoRepository avaliacaoRepository;

    public AluguelService(
            AluguelRepository aluguelRepository,
            UsuarioRepository usuarioRepository,
            AnuncioService anuncioService,
            NotificacaoService notificacaoService,
            AvaliacaoRepository avaliacaoRepository
    ) {
        this.aluguelRepository = aluguelRepository;
        this.usuarioRepository = usuarioRepository;
        this.anuncioService = anuncioService;
        this.notificacaoService = notificacaoService;
        this.avaliacaoRepository = avaliacaoRepository;
    }

    @Transactional
    public SolicitarAluguelResponse solicitar(Integer anuncioId, Integer usuarioId, SolicitarAluguelRequest request) {
        LocalDate dataInicio = parseData(request.dataInicio(), "Data de inicio invalida. Use o formato yyyy-MM-dd.");
        LocalDate dataFim = parseData(request.dataFim(), "Data de fim invalida. Use o formato yyyy-MM-dd.");
        validarDatas(dataInicio, dataFim);

        Usuario locatario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario locatario nao encontrado."));
        Anuncio anuncio = anuncioService.buscarAnuncioDetalhado(anuncioId);
        validarAnuncioDisponivel(anuncio, usuarioId);
        validarSobreposicao(anuncioId, null, dataInicio, dataFim);

        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        BigDecimal valorTotal = anuncio.getValorDiario().multiply(BigDecimal.valueOf(quantidadeDias));

        Aluguel aluguel = new Aluguel();
        aluguel.setAnuncio(anuncio);
        aluguel.setLocatario(locatario);
        aluguel.setDataInicio(dataInicio);
        aluguel.setDataFim(dataFim);
        aluguel.setStatusAluguel(statusAluguelEnum.EM_APROVACAO);
        aluguel.setValorTotal(valorTotal);
        Aluguel salvo = aluguelRepository.save(aluguel);

        notificacaoService.notificar(
                anuncio.getProprietario(),
                tipoNotificacaoEnum.RESERVA_SOLICITADA,
                "Nova solicitacao de aluguel",
                "Uma nova solicitacao foi criada para o anuncio " + anuncio.getTitulo() + "."
        );

        return new SolicitarAluguelResponse(
                salvo.getId(),
                anuncio.getId(),
                usuarioId,
                salvo.getStatusAluguel().name(),
                dataInicio.toString(),
                dataFim.toString(),
                quantidadeDias,
                anuncio.getValorDiario(),
                valorTotal,
                "Solicitacao de aluguel criada com sucesso."
        );
    }

    @Transactional
    public List<AluguelResumoResponse> listarDoLocatario(Integer usuarioId) {
        atualizarStatusAutomaticamente();
        return aluguelRepository.findByLocatario_IdOrderByDataCriacaoDesc(usuarioId).stream()
                .map(this::toResumo)
                .toList();
    }

    @Transactional
    public List<AluguelResumoResponse> listarDoLocador(Integer usuarioId) {
        atualizarStatusAutomaticamente();
        return aluguelRepository.findByAnuncio_Proprietario_IdOrderByDataCriacaoDesc(usuarioId).stream()
                .map(this::toResumo)
                .toList();
    }

    @Transactional
    public AluguelDetalheResponse detalhar(Integer aluguelId, Integer usuarioId, boolean admin) {
        atualizarStatusAutomaticamente();
        Aluguel aluguel = buscarDetalhado(aluguelId);
        boolean participante = aluguel.getLocatario().getId().equals(usuarioId)
                || aluguel.getAnuncio().getProprietario().getId().equals(usuarioId);
        if (!participante && !admin) {
            throw new AcessoNegadoException("Voce nao pode acessar este aluguel.");
        }

        boolean podeAvaliarAnuncio = aluguel.getStatusAluguel() == statusAluguelEnum.CONCLUIDO
                && aluguel.getLocatario().getId().equals(usuarioId)
                && !avaliacaoRepository.existsByAluguel_IdAndTipoAndAutor_Id(aluguel.getId(), tipoAvaliacaoEnum.ANUNCIO, usuarioId);

        boolean podeAvaliarContraparte = aluguel.getStatusAluguel() == statusAluguelEnum.CONCLUIDO
                && !avaliacaoRepository.existsByAluguel_IdAndTipoAndAutor_Id(
                aluguel.getId(),
                aluguel.getLocatario().getId().equals(usuarioId) ? tipoAvaliacaoEnum.LOCADOR : tipoAvaliacaoEnum.LOCATARIO,
                usuarioId
        );

        return new AluguelDetalheResponse(
                aluguel.getId(),
                aluguel.getAnuncio().getId(),
                aluguel.getAnuncio().getTitulo(),
                aluguel.getAnuncio().getDescricao(),
                imagemPrincipal(aluguel.getAnuncio()),
                aluguel.getLocatario().getNome(),
                aluguel.getAnuncio().getProprietario().getNome(),
                DataFormatUtils.formatarData(aluguel.getDataInicio()),
                DataFormatUtils.formatarData(aluguel.getDataFim()),
                aluguel.getValorTotal(),
                aluguel.getStatusAluguel().name(),
                cancelavelPor(aluguel, usuarioId),
                chatDisponivel(aluguel),
                podeAvaliarAnuncio,
                podeAvaliarContraparte,
                aluguel.getMotivoReprovacao(),
                aluguel.getMotivoCancelamento()
        );
    }

    @Transactional
    public AluguelDetalheResponse aprovar(Integer aluguelId, Integer usuarioId) {
        Aluguel aluguel = buscarDetalhado(aluguelId);
        validarProprietario(aluguel, usuarioId);
        if (aluguel.getStatusAluguel() != statusAluguelEnum.EM_APROVACAO) {
            throw new RegraDeNegocioException("Apenas solicitacoes em aprovacao podem ser aprovadas.");
        }
        validarSobreposicao(aluguel.getAnuncio().getId(), aluguel.getId(), aluguel.getDataInicio(), aluguel.getDataFim());
        aluguel.setStatusAluguel(statusAluguelEnum.CONFIRMADO);
        aluguel.setDataAprovacao(LocalDateTime.now());
        aluguel.setMotivoReprovacao(null);

        notificacaoService.notificar(
                aluguel.getLocatario(),
                tipoNotificacaoEnum.RESERVA_APROVADA,
                "Reserva aprovada",
                "Sua reserva para o anuncio " + aluguel.getAnuncio().getTitulo() + " foi aprovada."
        );
        return detalhar(aluguelId, usuarioId, false);
    }

    @Transactional
    public AluguelDetalheResponse confirmarEntrega(Integer aluguelId, Integer usuarioId) {
        Aluguel aluguel = buscarDetalhado(aluguelId);
        validarProprietario(aluguel, usuarioId);
        if (aluguel.getStatusAluguel() != statusAluguelEnum.CONFIRMADO) {
            throw new RegraDeNegocioException("Apenas alugueis confirmados podem ter a entrega confirmada.");
        }
        aluguel.setStatusAluguel(statusAluguelEnum.ATIVO);
        
        notificacaoService.notificar(
                aluguel.getLocatario(),
                tipoNotificacaoEnum.ALUGUEL_ATIVO,
                "Entrega confirmada",
                "O proprietário confirmou a entrega da ferramenta " + aluguel.getAnuncio().getTitulo() + ". O aluguel está agora ativo."
        );
        return detalhar(aluguelId, usuarioId, false);
    }

    @Transactional
    public AluguelDetalheResponse finalizar(Integer aluguelId, Integer usuarioId) {
        Aluguel aluguel = buscarDetalhado(aluguelId);
        validarProprietario(aluguel, usuarioId);
        if (aluguel.getStatusAluguel() != statusAluguelEnum.ATIVO) {
            throw new RegraDeNegocioException("Apenas alugueis ativos podem ser finalizados.");
        }
        aluguel.setStatusAluguel(statusAluguelEnum.CONCLUIDO);
        
        notificacaoService.notificar(
                aluguel.getLocatario(),
                tipoNotificacaoEnum.ALUGUEL_CONCLUIDO,
                "Aluguel finalizado",
                "O proprietário confirmou a devolução da ferramenta " + aluguel.getAnuncio().getTitulo() + ". O aluguel foi concluído."
        );
        return detalhar(aluguelId, usuarioId, false);
    }

    @Transactional
    public AluguelDetalheResponse reprovar(Integer aluguelId, Integer usuarioId, String motivo) {
        Aluguel aluguel = buscarDetalhado(aluguelId);
        validarProprietario(aluguel, usuarioId);
        if (aluguel.getStatusAluguel() != statusAluguelEnum.EM_APROVACAO) {
            throw new RegraDeNegocioException("Apenas solicitacoes em aprovacao podem ser reprovadas.");
        }
        aluguel.setStatusAluguel(statusAluguelEnum.REPROVADO);
        aluguel.setMotivoReprovacao(motivo.trim());

        notificacaoService.notificar(
                aluguel.getLocatario(),
                tipoNotificacaoEnum.RESERVA_REPROVADA,
                "Reserva reprovada",
                "Sua reserva para o anuncio " + aluguel.getAnuncio().getTitulo() + " foi reprovada."
        );
        return detalhar(aluguelId, usuarioId, false);
    }

    @Transactional
    public AluguelDetalheResponse cancelar(Integer aluguelId, Integer usuarioId, String motivo) {
        Aluguel aluguel = buscarDetalhado(aluguelId);
        if (!aluguel.getLocatario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Apenas o locatario pode cancelar esta reserva.");
        }
        if (!cancelavelPor(aluguel, usuarioId)) {
            throw new RegraDeNegocioException("Nao e possivel cancelar este aluguel no estado atual.");
        }

        if (aluguel.getStatusAluguel() == statusAluguelEnum.CONFIRMADO
                && LocalDateTime.now().plusHours(24).isAfter(aluguel.getDataInicio().atStartOfDay())) {
            throw new RegraDeNegocioException("O cancelamento exige no minimo 24 horas de antecedencia ao inicio do aluguel.");
        }

        aluguel.setStatusAluguel(statusAluguelEnum.CANCELADO);
        aluguel.setDataCancelamento(LocalDateTime.now());
        aluguel.setMotivoCancelamento(motivo.trim());

        notificacaoService.notificar(
                aluguel.getAnuncio().getProprietario(),
                tipoNotificacaoEnum.RESERVA_CANCELADA,
                "Reserva cancelada",
                "A reserva do anuncio " + aluguel.getAnuncio().getTitulo() + " foi cancelada pelo locatario."
        );
        return detalhar(aluguelId, usuarioId, false);
    }

    @Transactional
    @Scheduled(cron = "0 */30 * * * *")
    public void atualizarStatusAutomaticamente() {
        // Removido o avanço automático. 
        // Locação de ferramentas físicas exige confirmação manual de entrega e devolução.
    }

    @Transactional(readOnly = true)
    public Aluguel buscarDetalhado(Integer aluguelId) {
        return aluguelRepository.findDetailedById(aluguelId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel nao encontrado."));
    }

    public boolean chatDisponivel(Aluguel aluguel) {
        return aluguel.getStatusAluguel() == statusAluguelEnum.CONFIRMADO
                || aluguel.getStatusAluguel() == statusAluguelEnum.ATIVO
                || aluguel.getStatusAluguel() == statusAluguelEnum.CONCLUIDO;
    }

    private boolean cancelavelPor(Aluguel aluguel, Integer usuarioId) {
        if (!aluguel.getLocatario().getId().equals(usuarioId)) {
            return false;
        }
        return aluguel.getStatusAluguel() == statusAluguelEnum.EM_APROVACAO
                || aluguel.getStatusAluguel() == statusAluguelEnum.CONFIRMADO;
    }

    private void validarProprietario(Aluguel aluguel, Integer usuarioId) {
        if (!aluguel.getAnuncio().getProprietario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Apenas o locador pode gerenciar esta solicitacao.");
        }
    }

    private void validarAnuncioDisponivel(Anuncio anuncio, Integer usuarioId) {
        if (anuncio.getStatus() != statusAnuncioEnum.ATIVO) {
            throw new RegraDeNegocioException("Este anuncio nao esta mais disponivel no momento.");
        }
        if (anuncio.getProprietario() != null && anuncio.getProprietario().getId().equals(usuarioId)) {
            throw new RegraDeNegocioException("O proprietario nao pode alugar o proprio anuncio.");
        }
        if (anuncio.getValorDiario() == null || anuncio.getValorDiario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O anuncio informado nao possui valor diario valido para reserva.");
        }
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate hoje = LocalDate.now();
        if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
            throw new RegraDeNegocioException("Datas anteriores ao dia atual nao podem ser selecionadas.");
        }
        if (dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de fim deve ser igual ou posterior a data de inicio.");
        }
        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        if (quantidadeDias > 30) {
            throw new RegraDeNegocioException("O periodo maximo de aluguel e de 30 dias.");
        }
    }

    private void validarSobreposicao(Integer anuncioId, Integer aluguelId, LocalDate dataInicio, LocalDate dataFim) {
        boolean sobreposto = aluguelId == null
                ? aluguelRepository.existsReservaSobreposta(anuncioId, STATUS_QUE_BLOQUEIAM, dataInicio, dataFim)
                : aluguelRepository.existsReservaSobrepostaExceto(anuncioId, aluguelId, STATUS_QUE_BLOQUEIAM, dataInicio, dataFim);
        if (sobreposto) {
            throw new RegraDeNegocioException("Ja existe uma reserva para o periodo informado.");
        }
    }

    private LocalDate parseData(String data, String mensagemErro) {
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new RegraDeNegocioException(mensagemErro);
        }
    }

    private AluguelResumoResponse toResumo(Aluguel aluguel) {
        return new AluguelResumoResponse(
                aluguel.getId(),
                aluguel.getAnuncio().getId(),
                aluguel.getAnuncio().getTitulo(),
                imagemPrincipal(aluguel.getAnuncio()),
                aluguel.getLocatario().getNome(),
                aluguel.getAnuncio().getProprietario().getNome(),
                DataFormatUtils.formatarData(aluguel.getDataInicio()),
                DataFormatUtils.formatarData(aluguel.getDataFim()),
                aluguel.getValorTotal(),
                aluguel.getStatusAluguel().name()
        );
    }

    private String imagemPrincipal(Anuncio anuncio) {
        return anuncio.getImagens().stream()
                .sorted(java.util.Comparator.comparingInt(Imagem::getOrdem).thenComparing(Imagem::getId))
                .map(Imagem::getImagemUrl)
                .findFirst()
                .orElse(null);
    }
}
