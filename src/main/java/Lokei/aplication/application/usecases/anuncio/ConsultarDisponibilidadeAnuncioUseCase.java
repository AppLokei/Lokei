package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.application.dto.DisponibilidadeResponse;
import Lokei.aplication.application.dto.PeriodoReservadoResponse;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class ConsultarDisponibilidadeAnuncioUseCase {

    static final Set<StatusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            StatusAluguelEnum.EM_APROVACAO,
            StatusAluguelEnum.CONFIRMADO,
            StatusAluguelEnum.EM_ANDAMENTO
    );

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final AnuncioGateway anuncioGateway;
    private final AluguelGateway aluguelGateway;

    public ConsultarDisponibilidadeAnuncioUseCase(
            AnuncioGateway anuncioGateway,
            AluguelGateway aluguelGateway
    ) {
        this.anuncioGateway = anuncioGateway;
        this.aluguelGateway = aluguelGateway;
    }

    public DisponibilidadeResponse executar(Long anuncioId) {
        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        List<PeriodoReservadoResponse> periodosReservados = aluguelGateway
                .buscarReservasPorAnuncioEStatus(anuncioId, STATUS_QUE_BLOQUEIAM)
                .stream()
                .map(this::mapearPeriodo)
                .toList();

        return new DisponibilidadeResponse(
                anuncioId,
                anuncio.getStatus() == null ? null : anuncio.getStatus().name(),
                anuncio.getStatus() == StatusAnuncioEnum.ATIVO,
                periodosReservados
        );
    }

    private PeriodoReservadoResponse mapearPeriodo(Aluguel aluguel) {
        return new PeriodoReservadoResponse(
                aluguel.getId(),
                formatarData(aluguel.getDataInicio()),
                formatarData(aluguel.getDataFim()),
                aluguel.getStatus() == null ? null : aluguel.getStatus().name()
        );
    }

    private String formatarData(Date data) {
        if (data == null) {
            return null;
        }

        LocalDate localDate = data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.format(DATE_FORMATTER);
    }
}
