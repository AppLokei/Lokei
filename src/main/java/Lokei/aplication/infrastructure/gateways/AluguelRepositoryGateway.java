package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.infrastructure.persistence.mapper.AluguelMapper;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import org.springframework.data.domain.Page;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class AluguelRepositoryGateway implements AluguelGateway {

    private final AluguelRepository aluguelRepository;

    public AluguelRepositoryGateway(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    @Override
    public Optional<Aluguel> buscarPorId(Long id) {
        return aluguelRepository.findById(id).map(AluguelMapper::toDomain);
    }

    @Override
    public boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId) {
        return aluguelRepository.existsAluguelEmAndamento(anuncioId);
    }

    @Override
    public Page<Aluguel> buscarAluguelPorUsuario(Long usuarioId, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return aluguelRepository.findByUsuarioId(usuarioId, pageable).map(AluguelMapper::toDomain);
    }
}
