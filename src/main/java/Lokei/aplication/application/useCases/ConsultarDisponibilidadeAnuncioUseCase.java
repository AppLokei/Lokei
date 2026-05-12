package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.DisponibilidadeResponse;
import Lokei.aplication.application.dto.PeriodoReservadoResponse;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class ConsultarDisponibilidadeAnuncioUseCase {

    private static final Set<statusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            statusAluguelEnum.EM_APROVACAO,
            statusAluguelEnum.CONFIRMADO,
            statusAluguelEnum.ATIVO
    );

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private final AnuncioRepository anuncioRepository;
    private final AluguelRepository aluguelRepository;

    public ConsultarDisponibilidadeAnuncioUseCase(
            AnuncioRepository anuncioRepository,
            AluguelRepository aluguelRepository
    ) {
        this.anuncioRepository = anuncioRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public DisponibilidadeResponse executar(Integer anuncioId) {
        Anuncio anuncio = anuncioRepository.findById(anuncioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Anúncio não encontrado."));

        List<PeriodoReservadoResponse> periodosReservados = aluguelRepository
                .findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(anuncioId, STATUS_QUE_BLOQUEIAM)
                .stream()
                .map(this::mapearPeriodo)
                .toList();

        return new DisponibilidadeResponse(
                anuncioId,
                anuncio.getStatus() == null ? null : anuncio.getStatus().name(),
                anuncio.getStatus() == statusAnuncioEnum.ATIVO,
                periodosReservados
        );
    }

    private PeriodoReservadoResponse mapearPeriodo(Aluguel aluguel) {
        return new PeriodoReservadoResponse(
                aluguel.getId(),
                aluguel.getDataInicio().format(DATE_FORMATTER),
                aluguel.getDataFim().format(DATE_FORMATTER),
                aluguel.getStatusAluguel().name()
        );
    }
}
