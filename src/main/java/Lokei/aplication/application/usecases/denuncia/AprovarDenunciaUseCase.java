package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.DenunciaNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.services.NotificacaoService;

/**
 * Caso de uso: Administrador aprova uma denúncia.
 * RN-035: Ao aprovar, o anúncio é desativado e o locador é notificado (mock).
 */
public class AprovarDenunciaUseCase {

    private final DenunciaGateway denunciaGateway;
    private final AnuncioGateway anuncioGateway;
    private final NotificacaoService notificacaoService;

    public AprovarDenunciaUseCase(DenunciaGateway denunciaGateway,
                                   AnuncioGateway anuncioGateway,
                                   NotificacaoService notificacaoService) {
        this.denunciaGateway = denunciaGateway;
        this.anuncioGateway = anuncioGateway;
        this.notificacaoService = notificacaoService;
    }

    /**
     * @param denunciaId ID da denúncia a ser aprovada
     * @return           Denúncia com status APROVADA
     */
    public Denuncia execute(Long denunciaId) {
        Denuncia denuncia = denunciaGateway.buscarDenunciaPorId(denunciaId)
                .orElseThrow(() -> new DenunciaNotFoundException(denunciaId));

        // Aprova a denúncia (valida internamente que está PENDENTE)
        denuncia.aprovar();
        Denuncia denunciaAprovada = denunciaGateway.atualizarDenuncia(denuncia);

        // RN-035: Desativa o anúncio vinculado
        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(denuncia.getAnuncioId())
                .orElseThrow(() -> new AnuncioNotFoundException(denuncia.getAnuncioId()));

        // Desativa sem verificar aluguel em andamento (ação administrativa)
        anuncio.desativarAnuncio(false);
        Anuncio anuncioDesativado = anuncioGateway.atualizarAnuncio(anuncio);

        // Mock de notificação ao locador
        notificacaoService.notificarAnuncioDesativadoPorDenuncia(
                anuncioDesativado.getUsuarioId(),
                anuncioDesativado.getId()
        );

        return denunciaAprovada;
    }
}
