package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AnuncioMapper;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioEntityRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AnuncioRepositoryGateway implements AnuncioGateway {

    private final AnuncioEntityRepository anuncioEntityRepository;

    public AnuncioRepositoryGateway(AnuncioEntityRepository anuncioEntityRepository) {
        this.anuncioEntityRepository = anuncioEntityRepository;
    }

    @Override
    public Anuncio criarAnuncio(Anuncio anuncio) {
        AnuncioEntity entity = AnuncioMapper.toEntity(anuncio);
        AnuncioEntity salvo = anuncioEntityRepository.save(entity);
        return AnuncioMapper.toDomain(salvo);
    }

    @Override
    public Anuncio atualizarAnuncio(Anuncio anuncio) {
        AnuncioEntity entity = AnuncioMapper.toEntity(anuncio);
        AnuncioEntity salvo = anuncioEntityRepository.save(entity);
        return AnuncioMapper.toDomain(salvo);
    }

    @Override
    public void excluirAnuncio(Long id) {
        anuncioEntityRepository.deleteById(id);
    }

    @Override
    public List<Anuncio> buscarTodosAnuncios() {
        List<AnuncioEntity> entities = anuncioEntityRepository.findAll();
        List<Anuncio> anuncios = new ArrayList<>();
        for (AnuncioEntity entity : entities) {
            anuncios.add(AnuncioMapper.toDomain(entity));
        }
        return anuncios;
    }

    @Override
    public Optional<Anuncio> buscarAnuncioPorId(Long id) {
        Optional<AnuncioEntity> entity = anuncioEntityRepository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(AnuncioMapper.toDomain(entity.get()));
    }

    @Override
    public List<Anuncio> buscarAnuncioPorCategoria(CategoriaEnum categoria) {
        List<AnuncioEntity> entities = anuncioEntityRepository.findByFerramenta_Categoria(categoria);
        List<Anuncio> anuncios = new ArrayList<>();
        for (AnuncioEntity entity : entities) {
            anuncios.add(AnuncioMapper.toDomain(entity));
        }

        return anuncios;
    }
}
