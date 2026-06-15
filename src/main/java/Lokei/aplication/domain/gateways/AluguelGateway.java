package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AluguelGateway {
     Optional<Aluguel> buscarPorId(Long id);
     boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId);

     Page<Aluguel> buscarAluguelPorUsuario(Long usuarioId, int pagina, int tamanho);

     List<Aluguel> buscarReservasPorAnuncioEStatus(Long anuncioId, Collection<StatusAluguelEnum> status);

     boolean existeReservaSobreposta(Long anuncioId, Collection<StatusAluguelEnum> status, Date dataInicio, Date dataFim);

     Aluguel salvar(Aluguel aluguel, Long anuncioId, Long locatarioId);

     Aluguel atualizarStatus(Long id, StatusAluguelEnum novoStatus);
}
