package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;

import java.util.List;
import java.util.Optional;

/**
 * Interface de porta de saída para persistência de Denúncias.
 * Segue o padrão de Gateway da Clean Architecture do projeto.
 */
public interface DenunciaGateway {
    Denuncia criarDenuncia(Denuncia denuncia);
    Denuncia atualizarDenuncia(Denuncia denuncia);
    Optional<Denuncia> buscarDenunciaPorId(Long id);
    List<Denuncia> buscarDenunciasPorAnuncio(Long anuncioId);
    List<Denuncia> buscarDenunciasPorStatus(StatusDenunciaEnum status);
    boolean existeDenunciaPendentePorAnuncioEDenunciante(Long anuncioId, Long denuncianteId);
}
