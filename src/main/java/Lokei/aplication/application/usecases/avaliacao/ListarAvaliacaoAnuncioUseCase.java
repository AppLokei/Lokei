package Lokei.aplication.application.usecases.avaliacao;

import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ListarAvaliacaoAnuncioUseCase {

    @Autowired
    public AvaliacaoRepository avaliacaoRepository;

    @Autowired
    public AnuncioRepository anuncioRepository;

    public List<AvaliacaoEntity> listarAvaliacoes (Long id){
        AnuncioEntity anuncio = anuncioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Anúncio não encontrado"));

        return anuncio.getAlugueis()
                .stream()
                .map(AluguelEntity::getAvaliacao)
                .filter(Objects::nonNull)
                .toList();
    }
}
