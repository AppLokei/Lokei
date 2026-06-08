package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.anuncio.DisponibilidadeResponse;
import Lokei.aplication.application.dto.anuncio.PeriodoReservadoResponse;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.application.service.AnuncioService;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ConsultarDisponibilidadeAnuncioUseCase {

    private static final Set<statusAluguelEnum> STATUS_QUE_BLOQUEIAM = Set.of(
            statusAluguelEnum.EM_APROVACAO,
            statusAluguelEnum.CONFIRMADO,
            statusAluguelEnum.ATIVO
    );

    private final AnuncioService anuncioService;
    private final AluguelRepository aluguelRepository;

    public ConsultarDisponibilidadeAnuncioUseCase(AnuncioService anuncioService, AluguelRepository aluguelRepository) {
        this.anuncioService = anuncioService;
        this.aluguelRepository = aluguelRepository;
    }

    public DisponibilidadeResponse executar(Integer anuncioId) {
        Anuncio anuncio = anuncioService.buscarAnuncioDetalhado(anuncioId);
        List<PeriodoReservadoResponse> periodos = aluguelRepository
                .findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(anuncioId, STATUS_QUE_BLOQUEIAM)
                .stream()
                .map(aluguel -> new PeriodoReservadoResponse(
                        aluguel.getId(),
                        DataFormatUtils.formatarData(aluguel.getDataInicio()),
                        DataFormatUtils.formatarData(aluguel.getDataFim()),
                        aluguel.getStatusAluguel().name()
                ))
                .toList();

        return new DisponibilidadeResponse(
                anuncioId,
                anuncio.getStatus().name(),
                anuncio.getStatus() == statusAnuncioEnum.ATIVO,
                periodos
        );
    }
}
