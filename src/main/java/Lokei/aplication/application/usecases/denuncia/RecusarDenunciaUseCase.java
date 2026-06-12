package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.exceptions.DenunciaNotFoundException;
import Lokei.aplication.domain.gateways.DenunciaGateway;

/**
 * Caso de uso: Administrador recusa uma denúncia.
 * RN-035: O anúncio permanece no status atual.
 */
public class RecusarDenunciaUseCase {

    private final DenunciaGateway denunciaGateway;

    public RecusarDenunciaUseCase(DenunciaGateway denunciaGateway) {
        this.denunciaGateway = denunciaGateway;
    }

    /**
     * @param denunciaId ID da denúncia a ser recusada
     * @return           Denúncia com status RECUSADA
     */
    public Denuncia execute(Long denunciaId) {
        Denuncia denuncia = denunciaGateway.buscarDenunciaPorId(denunciaId)
                .orElseThrow(() -> new DenunciaNotFoundException(denunciaId));

        denuncia.recusar();
        return denunciaGateway.atualizarDenuncia(denuncia);
    }
}
