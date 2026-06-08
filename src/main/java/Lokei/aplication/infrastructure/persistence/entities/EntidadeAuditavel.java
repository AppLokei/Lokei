package Lokei.aplication.infrastructure.persistence.entities;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class EntidadeAuditavel {

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void prePersist() {
        LocalDateTime agora = LocalDateTime.now();
        this.dataCriacao = agora;
        this.dataAtualizacao = agora;
    }

    @PreUpdate
    protected void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}