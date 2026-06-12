package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.application.util.CpfOrEmailUtil;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import org.springframework.data.domain.Page;

public class BuscarAnuncioPorUsuarioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public BuscarAnuncioPorUsuarioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Page<Anuncio> buscarAnuncioPorUsuario(String identificador, int pagina, int tamanho) {
        String identificadorTratado = identificador.trim();

        UsuarioEntity usuario = switch (CpfOrEmailUtil.identify(identificadorTratado)) {
            case CPF -> usuarioGateway.buscarUsuarioPorCpf(identificadorTratado)
                    .orElseThrow(() -> new UsuarioNotFoundException(identificadorTratado));
            case EMAIL -> usuarioGateway.buscarUsuarioPorEmail(identificadorTratado)
                    .orElseThrow(() -> new UsuarioNotFoundException(identificadorTratado));
        };

        return anuncioGateway.buscarAnuncioPorUsuario(usuario.getId(), pagina, tamanho);
    }
}
