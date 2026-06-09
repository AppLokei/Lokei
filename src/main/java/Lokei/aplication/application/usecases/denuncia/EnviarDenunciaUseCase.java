package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.DenunciaInvalidaException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.domain.enums.MotivoDenunciaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

/**
 * Caso de uso: Usuário envia uma denúncia sobre um anúncio.
 * RN-034: motivo deve ser um dos valores válidos do enum.
 */
public class EnviarDenunciaUseCase {

    private final DenunciaGateway denunciaGateway;
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public EnviarDenunciaUseCase(DenunciaGateway denunciaGateway,
                                  AnuncioGateway anuncioGateway,
                                  UsuarioGateway usuarioGateway) {
        this.denunciaGateway = denunciaGateway;
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * @param anuncioId      ID do anúncio denunciado
     * @param denuncianteId  ID do usuário que está denunciando
     * @param motivo         Motivo da denúncia (validado via enum RN-034)
     * @param descricao      Descrição detalhada
     * @return               Denúncia criada
     */
    public Denuncia execute(Long anuncioId, Long denuncianteId,
                            MotivoDenunciaEnum motivo, String descricao) {

        // Valida existência do anúncio
        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(anuncioId)
                .orElseThrow(() -> new AnuncioNotFoundException(anuncioId));

        // Anúncio desativado não pode ser denunciado
        if (StatusAnuncioEnum.DESATIVADO.equals(anuncio.getStatus())) {
            throw new DenunciaInvalidaException("Não é possível denunciar um anúncio já desativado.");
        }

        // Valida existência do denunciante
        usuarioGateway.buscarUsuarioPorId(denuncianteId)
                .orElseThrow(() -> new UsuarioNotFoundException(denuncianteId));

        // O dono do anúncio não pode denunciar o próprio anúncio
        if (anuncio.getUsuarioId().equals(denuncianteId)) {
            throw new DenunciaInvalidaException("O proprietário do anúncio não pode denunciá-lo.");
        }

        // Verifica denúncia duplicada pendente (evita spam)
        if (denunciaGateway.existeDenunciaPendentePorAnuncioEDenunciante(anuncioId, denuncianteId)) {
            throw new DenunciaInvalidaException("Você já possui uma denúncia pendente para este anúncio.");
        }

        Denuncia novaDenuncia = new Denuncia(null, motivo, descricao, null, null, anuncioId, denuncianteId);
        return denunciaGateway.criarDenuncia(novaDenuncia);
    }
}
