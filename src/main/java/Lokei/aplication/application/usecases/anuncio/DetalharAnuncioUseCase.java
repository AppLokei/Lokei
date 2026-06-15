package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.application.dto.AnuncioDetalheResponse;
import Lokei.aplication.application.usecases.avaliacao.ListarAvaliacaoAnuncioUseCase;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class DetalharAnuncioUseCase {

    private static final String ACAO_LOGIN = "FAZER_LOGIN";
    private static final String ACAO_EDITAR = "EDITAR_ANUNCIO";
    private static final String ACAO_RESERVAR = "SOLICITAR_ALUGUEL";
    private static final String ACAO_INDISPONIVEL = "INDISPONIVEL";
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;
    private final ListarAvaliacaoAnuncioUseCase listarAvaliacaoAnuncioUseCase;

    public DetalharAnuncioUseCase(
            AnuncioGateway anuncioGateway,
            UsuarioGateway usuarioGateway,
            ListarAvaliacaoAnuncioUseCase listarAvaliacaoAnuncioUseCase
    ) {
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
        this.listarAvaliacaoAnuncioUseCase = listarAvaliacaoAnuncioUseCase;
    }

    public AnuncioDetalheResponse executar(Long anuncioId, Long usuarioId) {
        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        List<AvaliacaoEntity> avaliacoes = listarAvaliacaoAnuncioUseCase.listarAvaliacoes(anuncioId);

        List<AnuncioDetalheResponse.AvaliacaoResumo> avaliacoesResponse = avaliacoes.stream()
                .sorted(Comparator.comparing(AvaliacaoEntity::getDataCriacao, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(avaliacao -> new AnuncioDetalheResponse.AvaliacaoResumo(
                        avaliacao.getId() == null ? null : avaliacao.getId().longValue(),
                        avaliacao.getNota(),
                        avaliacao.getComentario(),
                        formatarDataHora(avaliacao.getDataCriacao())
                ))
                .toList();

        BigDecimal notaMedia = avaliacoes.stream()
                .map(AvaliacaoEntity::getNota)
                .filter(nota -> nota != null)
                .map(BigDecimal::valueOf)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (!avaliacoes.isEmpty()) {
            notaMedia = notaMedia.divide(BigDecimal.valueOf(avaliacoes.size()), 2, RoundingMode.HALF_UP);
        } else {
            notaMedia = BigDecimal.ZERO;
        }

        return new AnuncioDetalheResponse(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getFerramenta() == null || anuncio.getFerramenta().getCategoria() == null
                        ? null : anuncio.getFerramenta().getCategoria().name(),
                anuncio.getStatus() == null ? null : anuncio.getStatus().name(),
                montarProprietario(anuncio.getUsuarioId()),
                anuncio.getImagens().stream()
                        .sorted(Comparator.comparing(Imagem::getId, Comparator.nullsLast(Comparator.naturalOrder())))
                        .map(Imagem::getImagemUrl)
                        .toList(),
                avaliacoesResponse,
                notaMedia,
                avaliacoesResponse.size(),
                determinarAcaoPrimaria(anuncio, usuarioId),
                podeReservar(anuncio, usuarioId)
        );
    }

    private AnuncioDetalheResponse.ProprietarioResumo montarProprietario(Long proprietarioId) {
        if (proprietarioId == null) {
            return null;
        }

        Usuario proprietario = usuarioGateway.buscarUsuarioPorId(proprietarioId).orElse(null);
        if (proprietario == null) {
            return null;
        }

        return new AnuncioDetalheResponse.ProprietarioResumo(
                proprietario.getId(),
                proprietario.getNome(),
                mascararEmail(proprietario.getEmail()),
                mascararTelefone(proprietario.getTelefone())
        );
    }

    private String determinarAcaoPrimaria(Anuncio anuncio, Long usuarioId) {
        if (usuarioId == null) {
            return ACAO_LOGIN;
        }

        if (anuncio.getUsuarioId() != null && usuarioId.equals(anuncio.getUsuarioId())) {
            return ACAO_EDITAR;
        }

        if (anuncio.getStatus() != StatusAnuncioEnum.ATIVO) {
            return ACAO_INDISPONIVEL;
        }

        return ACAO_RESERVAR;
    }

    private boolean podeReservar(Anuncio anuncio, Long usuarioId) {
        return ACAO_RESERVAR.equals(determinarAcaoPrimaria(anuncio, usuarioId));
    }

    private String mascararEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }

        String[] partes = email.split("@", 2);
        String inicio = partes[0].isEmpty() ? "*" : partes[0].substring(0, 1);
        return inicio + "***@" + partes[1];
    }

    private String mascararTelefone(String telefone) {
        if (telefone == null || telefone.length() < 4) {
            return telefone;
        }

        return "***-***-" + telefone.substring(telefone.length() - 4);
    }

    private String formatarDataHora(Date data) {
        if (data == null) {
            return null;
        }

        LocalDateTime dataHora = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return dataHora.format(DATE_TIME_FORMAT);
    }
}
