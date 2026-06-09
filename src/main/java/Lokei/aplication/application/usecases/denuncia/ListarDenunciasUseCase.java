package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import Lokei.aplication.domain.gateways.DenunciaGateway;

import java.util.List;

/**
 * Caso de uso: Lista denúncias por status (uso do Administrador).
 */
public class ListarDenunciasUseCase {

    private final DenunciaGateway denunciaGateway;

    public ListarDenunciasUseCase(DenunciaGateway denunciaGateway) {
        this.denunciaGateway = denunciaGateway;
    }

    public List<Denuncia> execute(StatusDenunciaEnum status) {
        return denunciaGateway.buscarDenunciasPorStatus(status);
    }
}
