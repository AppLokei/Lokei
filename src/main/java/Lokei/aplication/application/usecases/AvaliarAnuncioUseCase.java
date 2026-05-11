package Lokei.aplication.application.usecases;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.entity.AluguelEntity;
import Lokei.aplication.infrastructure.persistence.entity.AvaliacaoEntity;
import Lokei.aplication.infrastructure.persistence.repository.AluguelEntityRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliarAnuncioUseCase {

    @Autowired
    private AluguelEntityRepository repoAluguel;
    @Autowired
    private AvaliacaoEntityRepository repoAvaliacao;

    public String avaliarAnuncio(AvaliacaoEntity avaliacaoEntity){

        try{
            Integer aluguelId = avaliacaoEntity.getAluguel().getId();

            AluguelEntity aluguelEntity = repoAluguel.findById(aluguelId)
                    .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));

            StatusAluguelEnum status = aluguelEntity.getStatusAluguel();

            if(status == StatusAluguelEnum.CONCLUIDO){
                repoAvaliacao.save(avaliacaoEntity);
                return "Avaliação realizada";
            }else if(status == StatusAluguelEnum.ATIVO || status == StatusAluguelEnum.CONFIRMADO){
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
