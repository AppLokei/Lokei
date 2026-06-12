package Lokei.aplication.application.useCases;

import Lokei.aplication.infrastructure.exception.AluguelException;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
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


    public String avaliarAnuncio(Avaliacao avaliacao) {

        try {
            Integer aluguelId = avaliacao.getAluguel().getId();

            Aluguel aluguel = repoAluguel.findById(aluguelId)
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
