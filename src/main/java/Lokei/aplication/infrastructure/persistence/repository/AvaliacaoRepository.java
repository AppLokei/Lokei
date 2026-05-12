package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer> {

    List<Avaliacao> findByAluguel_Anuncio_IdOrderByDataCriacaoDesc(Integer anuncioId);
}
