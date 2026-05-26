package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.DenunciaInvalidoException;

import java.time.LocalDate;

public class Denuncia {
    private Long id;
    private String motivo;
    private String descricao;
    private final LocalDate dataDenuncia;

    private final Long usuarioId;
    private final Long anuncioId;

    public Denuncia(Long id, String motivo, String descricao, Long usuarioId, Long anuncioId) {
        if (motivo.isBlank() || descricao.isBlank()) {
            throw new DenunciaInvalidoException("Campos obrigatorios não preenchidos");
        }

        this.id = id;
        this.motivo = motivo;
        this.descricao = descricao;
        this.dataDenuncia = LocalDate.now();
        this.usuarioId = usuarioId;
        this.anuncioId = anuncioId;
    }

    public Long getId() {
        return id;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataDenuncia() {
        return dataDenuncia;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Long getAnuncioId() {
        return anuncioId;
    }
}
