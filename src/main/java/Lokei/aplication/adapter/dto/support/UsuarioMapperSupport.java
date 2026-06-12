package Lokei.aplication.adapter.dto.support;


import Lokei.aplication.adapter.dto.req.EnderecoRequest;
import Lokei.aplication.adapter.dto.res.EnderecoResponse;
import Lokei.aplication.adapter.dto.res.PerfilResponse;
import Lokei.aplication.adapter.dto.res.UsuarioSessaoResponse;
import Lokei.aplication.infrastructure.persistence.entities.EnderecoEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;


public final class UsuarioMapperSupport {

    private UsuarioMapperSupport() {
    }

    public static EnderecoEntity toEndereco(EnderecoRequest request) {
        if (request == null) {
            return null;
        }
        EnderecoEntity endereco = new EnderecoEntity();
        endereco.setLogradouro(request.logradouro());
        endereco.setBairro(request.bairro());
        endereco.setCidade(request.cidade());
        endereco.setEstado(request.estado().toUpperCase());
        endereco.setCep(request.cep());
        return endereco;
    }

    public static void copyEndereco(EnderecoEntity destino, EnderecoRequest request) {
        destino.setLogradouro(request.logradouro());
        destino.setBairro(request.bairro());
        destino.setNumero(request.numero());
        destino.setCidade(request.cidade());
        destino.setEstado(request.estado().toUpperCase());
        destino.setCep(request.cep());
    }
    public static EnderecoResponse toEnderecoResponse(EnderecoEntity endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponse(
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getNumero(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public static PerfilResponse toPerfilResponse(UsuarioEntity usuario) {
        return new PerfilResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefone(),
                toEnderecoResponse(usuario.getEndereco())
        );
    }

    public static UsuarioSessaoResponse toSessaoResponse(UsuarioEntity usuario) {
        return new UsuarioSessaoResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
