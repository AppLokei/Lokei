package Lokei.aplication.application.support;

import Lokei.aplication.application.dto.auth.UsuarioSessaoResponse;
import Lokei.aplication.application.dto.profile.EnderecoRequest;
import Lokei.aplication.application.dto.profile.EnderecoResponse;
import Lokei.aplication.application.dto.profile.PerfilResponse;
import Lokei.aplication.infrastructure.persistence.entity.Endereco;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;

public final class UsuarioMapperSupport {

    private UsuarioMapperSupport() {
    }

    public static Endereco toEndereco(EnderecoRequest request) {
        if (request == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setLogradouro(request.logradouro());
        endereco.setBairro(request.bairro());
        endereco.setNumero(request.numero());
        endereco.setComplemento(request.complemento());
        endereco.setCidade(request.cidade());
        endereco.setEstado(request.estado().toUpperCase());
        endereco.setCep(request.cep());
        return endereco;
    }

    public static void copyEndereco(Endereco destino, EnderecoRequest request) {
        destino.setLogradouro(request.logradouro());
        destino.setBairro(request.bairro());
        destino.setNumero(request.numero());
        destino.setComplemento(request.complemento());
        destino.setCidade(request.cidade());
        destino.setEstado(request.estado().toUpperCase());
        destino.setCep(request.cep());
    }

    public static EnderecoResponse toEnderecoResponse(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponse(
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    public static PerfilResponse toPerfilResponse(Usuario usuario) {
        return new PerfilResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefone(),
                usuario.getPapel().name(),
                toEnderecoResponse(usuario.getEndereco())
        );
    }

    public static UsuarioSessaoResponse toSessaoResponse(Usuario usuario) {
        return new UsuarioSessaoResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPapel().name());
    }
}
