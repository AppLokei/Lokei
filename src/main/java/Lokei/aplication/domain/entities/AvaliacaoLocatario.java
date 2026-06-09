package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.AvaliacaoInvalidaException;

import java.util.Date;

public class AvaliacaoLocatario {

    private Long id;
    private Integer nota;
    private String comentario;
    private Date dataCriacao;
    private Long aluguelId;
    private Long avaliadorId; // Locador
    private Long avaliadoId;  // Locatário

    public AvaliacaoLocatario(Long id, Integer nota, String comentario, Date dataCriacao, Long aluguelId, Long avaliadorId, Long avaliadoId) {
        if (nota == null || nota < 1 || nota > 5) {
            throw new AvaliacaoInvalidaException("A nota da avaliação deve estar entre 1 e 5.");
        }
        if (aluguelId == null) {
            throw new AvaliacaoInvalidaException("A avaliação deve estar vinculada a um aluguel.");
        }
        if (avaliadorId == null || avaliadoId == null) {
            throw new AvaliacaoInvalidaException("Avaliador e Avaliado devem ser informados.");
        }

        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao != null ? dataCriacao : new Date();
        this.aluguelId = aluguelId;
        this.avaliadorId = avaliadorId;
        this.avaliadoId = avaliadoId;
    }

    public Long getId() {
        return id;
    }

    public Integer getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Long getAluguelId() {
        return aluguelId;
    }

    public Long getAvaliadorId() {
        return avaliadorId;
    }

    public Long getAvaliadoId() {
        return avaliadoId;
    }
}
