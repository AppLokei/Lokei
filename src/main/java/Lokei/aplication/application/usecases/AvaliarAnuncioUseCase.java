package Lokei.aplication.application.usecases;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.entities.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
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

    public String avaliarAnuncio(AvaliacaoEntity avaliacaoEntity){

        try{
            Integer aluguelId = avaliacaoEntity.getAluguel().getId();

            AluguelEntity aluguelEntity = repoAluguel.findById(Long.valueOf(aluguelId))
                    .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

            StatusAluguelEnum status = aluguelEntity.getStatusAluguel();

            if(status == StatusAluguelEnum.CONCLUIDO){
                repoAvaliacao.save(avaliacaoEntity);
                return "Avaliação realizada";
            }else if(status == StatusAluguelEnum.EM_ANDAMENTO || status == StatusAluguelEnum.CONFIRMADO){
                return "ainda não é possível avaliar este anúncio, seu periodo de reserva ainda não finalizou";
            }else if(status == StatusAluguelEnum.EM_APROVACAO){
                return "não é possível avaliar esse anúncio, a reserva ainda não foi finalizada";

            }else{
                return "não é possível avaliar esse anúncio, a reserva não foi utilizada";
            }
        }catch (Exception e){
            return "Erro ao realizar avaliação: " + e.getMessage();
        }

    }

}
