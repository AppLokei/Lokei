package Lokei.aplication.adapter.dto.res;

import Lokei.aplication.domain.entities.AvaliacaoLocatario;

import java.io.Serializable;
import java.util.Date;

public class AvaliacaoLocatarioResponse implements Serializable {

    private Long id;
    private Long aluguelId;
    private Long avaliadorId;
    private Long avaliadoId;
    private Integer nota;
    private String comentario;
    private Date dataCriacao;

    public AvaliacaoLocatarioResponse(AvaliacaoLocatario domain) {
        this.id = domain.getId();
        this.aluguelId = domain.getAluguelId();
        this.avaliadorId = domain.getAvaliadorId();
        this.avaliadoId = domain.getAvaliadoId();
        this.nota = domain.getNota();
        this.comentario = domain.getComentario();
        this.dataCriacao = domain.getDataCriacao();
    }

    public Long getId() { return id; }
    public Long getAluguelId() { return aluguelId; }
    public Long getAvaliadorId() { return avaliadorId; }
    public Long getAvaliadoId() { return avaliadoId; }
    public Integer getNota() { return nota; }
    public String getComentario() { return comentario; }
    public Date getDataCriacao() { return dataCriacao; }
}
