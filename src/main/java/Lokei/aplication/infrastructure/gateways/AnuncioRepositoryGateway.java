package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AnuncioMapper;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.specification.AnuncioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class AnuncioRepositoryGateway implements AnuncioGateway {

    private final AnuncioRepository anuncioRepository;

    public AnuncioRepositoryGateway(AnuncioRepository anuncioRepository) {
        this.anuncioRepository = anuncioRepository;
    }

    @Override
    public Anuncio criarAnuncio(Anuncio anuncio) {
        AnuncioEntity entity = AnuncioMapper.toEntity(anuncio);
        AnuncioEntity salvo = anuncioRepository.save(entity);
        return AnuncioMapper.toDomain(salvo);
    }

    @Override
    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        AnuncioEntity entity = AnuncioMapper.toEntity(anuncio);
        AnuncioEntity salvo = anuncioRepository.save(entity);
        return AnuncioMapper.toDomain(salvo);
    }

    @Override
    public Optional<Anuncio> buscarAnuncioPorId(Long id) {
        Optional<AnuncioEntity> entity = anuncioRepository.findById(id);
        return entity.map(AnuncioMapper::toDomain);
    }

    @Override
    public Page<Anuncio> buscarAnunciosComFiltro(String titulo, CategoriaEnum categoria, BigDecimal valorMin, BigDecimal valorMax, int pagina, int tamanho) {

        Specification<AnuncioEntity> spec = Specification
                .where(AnuncioSpecification.nomeContem(titulo))
                .and(AnuncioSpecification.categoriaIgual(categoria))
                .and(AnuncioSpecification.valorEntre(valorMin, valorMax));

        Pageable pageable = PageRequest.of(pagina, tamanho);

        return anuncioRepository.findAll(spec, pageable).map(AnuncioMapper::toDomain);

    }

    @Override
    public List<Anuncio> buscarAnuncioPorUsuario(Long usuarioId) {
        return List.of();
    }
}
