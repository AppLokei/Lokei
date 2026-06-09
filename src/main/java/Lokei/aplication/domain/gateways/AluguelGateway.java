package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Aluguel;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface AluguelGateway {
     Optional<Aluguel> buscarPorId(Long id);
     boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId);

     Page<Aluguel> buscarAluguelPorUsuario(Long usuarioId, int pagina, int tamanho);
}
