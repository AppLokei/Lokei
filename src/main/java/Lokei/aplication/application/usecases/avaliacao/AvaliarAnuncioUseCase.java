package Lokei.aplication.application.usecases.avaliacao;

import Lokei.aplication.domain.exceptions.AluguelException;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AvaliarAnuncioUseCase {

    @Autowired
    private AluguelRepository repoAluguel;
    @Autowired
    private AvaliacaoRepository repoAvaliacao;


    public String avaliarAnuncio(AvaliacaoEntity avaliacao) {

        try {
            Long aluguelId = avaliacao.getAluguel().getId();

            AluguelEntity aluguel = repoAluguel.findById(aluguelId)
                    .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

            avaliacao.statusavaliacao(aluguel.getStatusAluguel());
            avaliacao.setAluguel(aluguel);
            repoAvaliacao.save(avaliacao);
            return "Avaliação realizada";

        } catch (AluguelException e) {
            return e.getMessage();
        } catch (DataIntegrityViolationException e) {
            return "Erro ao realizar avaliação: essa reserva já possui avaliação";
        } catch (Exception e) {
            return "Erro ao realizar avaliação: " + e.getMessage();
        }


    }
}
