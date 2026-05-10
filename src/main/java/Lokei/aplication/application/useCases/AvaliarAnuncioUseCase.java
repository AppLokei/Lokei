package Lokei.aplication.application.useCases;

import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliarAnuncioUseCase {

    @Autowired
    private AluguelRepository repoAluguel;
    @Autowired
    private AvaliacaoRepository repoAvaliacao;

    public String avaliarAnuncio(Avaliacao avaliacao){

        try{
            Integer aluguelId = avaliacao.getAluguel().getId();

            Aluguel aluguel = repoAluguel.findById(aluguelId)
                    .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

            statusAluguelEnum status = aluguel.getStatusAluguel();

            if(status == statusAluguelEnum.CONCLUIDO){
                repoAvaliacao.save(avaliacao);
                return "Avaliação realizada";
            }else if(status == statusAluguelEnum.ATIVO || status == statusAluguelEnum.CONFIRMADO){
                return "ainda não é possível avaliar este anúncio, seu periodo de reserva ainda não finalizou";
            }else if(status == statusAluguelEnum.EM_APROVACAO){
                return "não é possível avaliar esse anúncio, a reserva ainda não foi finalizada";

            }else{
                return "não é possível avaliar esse anúncio, a reserva não foi utilizada";
            }
        }catch (Exception e){
            return "Erro ao realizar avaliação: " + e.getMessage();
        }

    }

}
