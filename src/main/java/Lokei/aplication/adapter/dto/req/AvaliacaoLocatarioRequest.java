package Lokei.aplication.adapter.dto.req;

import java.io.Serializable;

public class AvaliacaoLocatarioRequest implements Serializable {

    private Long aluguelId;
    private Long avaliadorId;
    private Integer nota;
    private String comentario;

    public AvaliacaoLocatarioRequest() {}

    public AvaliacaoLocatarioRequest(Long aluguelId, Long avaliadorId, Integer nota, String comentario) {
        this.aluguelId = aluguelId;
        this.avaliadorId = avaliadorId;
        this.nota = nota;
        this.comentario = comentario;
    }

    public Long getAluguelId() { return aluguelId; }
    public void setAluguelId(Long aluguelId) { this.aluguelId = aluguelId; }
    public Long getAvaliadorId() { return avaliadorId; }
    public void setAvaliadorId(Long avaliadorId) { this.avaliadorId = avaliadorId; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
