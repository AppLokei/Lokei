package Lokei.aplication.application.usecases.aluguel;

import Lokei.aplication.application.dto.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.SolicitarAluguelResponse;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;

@Service
public class SolicitarAluguelUseCase {

    private static final Set<StatusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            StatusAluguelEnum.EM_APROVACAO,
            StatusAluguelEnum.CONFIRMADO,
            StatusAluguelEnum.EM_ANDAMENTO
    );

    private final AluguelGateway aluguelGateway;
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public SolicitarAluguelUseCase(
            AluguelGateway aluguelGateway,
            AnuncioGateway anuncioGateway,
            UsuarioGateway usuarioGateway
    ) {
        this.aluguelGateway = aluguelGateway;
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public SolicitarAluguelResponse executar(Long anuncioId, SolicitarAluguelRequest request) {
        if (request == null || request.usuarioId() == null) {
            throw new RegraDeNegocioException("Usuário locatário é obrigatório para solicitar o aluguel.");
        }

        LocalDate dataInicio = parseData(request.dataInicio(), "Data de início inválida. Use o formato yyyy-MM-dd.");
        LocalDate dataFim = parseData(request.dataFim(), "Data de fim inválida. Use o formato yyyy-MM-dd.");

        validarDatas(dataInicio, dataFim);

        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        Usuario locatario = usuarioGateway.buscarUsuarioPorId(request.usuarioId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário locatário não encontrado."));

        validarAnuncioDisponivel(anuncio, request.usuarioId());

        Date dataInicioConvertida = converterData(dataInicio);
        Date dataFimConvertida = converterData(dataFim);

        validarSobreposicao(anuncioId, dataInicioConvertida, dataFimConvertida);

        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        BigDecimal valorTotal = anuncio.getValorDiario().multiply(BigDecimal.valueOf(quantidadeDias));

        Aluguel aluguel = new Aluguel(
                null,
                dataInicioConvertida,
                dataFimConvertida,
                valorTotal,
                StatusAluguelEnum.EM_APROVACAO,
                anuncio.getId(),
                locatario.getId()
        );

        Aluguel aluguelSalvo = aluguelGateway.salvar(aluguel, anuncio.getId(), locatario.getId());

        return new SolicitarAluguelResponse(
                aluguelSalvo.getId(),
                anuncio.getId(),
                locatario.getId(),
                aluguelSalvo.getStatus().name(),
                dataInicio.toString(),
                dataFim.toString(),
                quantidadeDias,
                anuncio.getValorDiario(),
                valorTotal,
                "Solicitação de aluguel criada com sucesso."
        );
    }

    private LocalDate parseData(String data, String mensagemErro) {
        try {
            return LocalDate.parse(data);
        } catch (DateTimeParseException | NullPointerException exception) {
            throw new RegraDeNegocioException(mensagemErro);
        }
    }

    private void validarDatas(LocalDate dataInicio, LocalDate dataFim) {
        LocalDate hoje = LocalDate.now();

        if (dataInicio.isBefore(hoje) || dataFim.isBefore(hoje)) {
            throw new RegraDeNegocioException("Datas anteriores ao dia atual não podem ser selecionadas.");
        }

        if (dataFim.isBefore(dataInicio)) {
            throw new RegraDeNegocioException("A data de fim deve ser igual ou posterior à data de início.");
        }

        long quantidadeDias = ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
        if (quantidadeDias > 30) {
            throw new RegraDeNegocioException("O período máximo de aluguel é de 30 dias.");
        }
    }

    private void validarAnuncioDisponivel(Anuncio anuncio, Long usuarioId) {
        if (anuncio.getStatus() != StatusAnuncioEnum.ATIVO) {
            throw new RegraDeNegocioException("Este anúncio não está mais disponível no momento.");
        }

        if (anuncio.getUsuarioId() != null && usuarioId.equals(anuncio.getUsuarioId())) {
            throw new RegraDeNegocioException("O proprietário não pode alugar o próprio anúncio.");
        }

        if (anuncio.getValorDiario() == null || anuncio.getValorDiario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RegraDeNegocioException("O anúncio informado não possui valor diário válido para reserva.");
        }
    }

    private void validarSobreposicao(Long anuncioId, Date dataInicio, Date dataFim) {
        boolean existeReservaSobreposta = aluguelGateway.existeReservaSobreposta(
                anuncioId,
                STATUS_QUE_BLOQUEIAM,
                dataInicio,
                dataFim
        );

        if (existeReservaSobreposta) {
            throw new RegraDeNegocioException("Já existe uma reserva para o período informado.");
        }
    }

    private Date converterData(LocalDate data) {
        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
