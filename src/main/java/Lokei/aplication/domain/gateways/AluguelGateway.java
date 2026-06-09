package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Aluguel;
import org.springframework.data.domain.Page;

public interface AluguelGateway {
     boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId);

     Page<Aluguel> buscarAluguelPorUsuario(Long usuarioId, int pagina, int tamanho);
}
