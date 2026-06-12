package Lokei.aplication.application.usecases.aluguel;

import Lokei.aplication.application.util.CpfOrEmailUtil;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import org.springframework.data.domain.Page;

public class BuscarAluguelPorUsuarioUseCase {

    private final AluguelGateway aluguelGateway;
    private final UsuarioGateway usuarioGateway;

    public BuscarAluguelPorUsuarioUseCase(AluguelGateway aluguelGateway, UsuarioGateway usuarioGateway) {
        this.aluguelGateway = aluguelGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Page<Aluguel> buscarAluguelPorUsuario(String identificador, int pagina, int tamanho) {
        String identificadorTratado = identificador.trim();

        UsuarioEntity usuario = switch (CpfOrEmailUtil.identify(identificadorTratado)) {
            case CPF -> usuarioGateway.buscarUsuarioPorCpf(identificadorTratado)
                    .orElseThrow(() -> new UsuarioNotFoundException(identificadorTratado));
            case EMAIL -> usuarioGateway.buscarUsuarioPorEmail(identificadorTratado)
                    .orElseThrow(() -> new UsuarioNotFoundException(identificadorTratado));
        };

        return aluguelGateway.buscarAluguelPorUsuario(usuario.getId(), pagina, tamanho);
    }
}

