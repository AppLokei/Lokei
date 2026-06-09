package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.MotivoDenunciaEnum;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import Lokei.aplication.domain.exceptions.DenunciaInvalidaException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa uma denúncia de anúncio.
 * Regras de negócio RN-034 e RN-035.
 */
public class Denuncia {

    private final Long id;
    private final MotivoDenunciaEnum motivo;
    private final String descricao;
    private final LocalDateTime dataDenuncia;
    private StatusDenunciaEnum status;

    /** ID do anúncio denunciado (referência leve, sem acoplamento à entidade completa) */
    private final Long anuncioId;

    /** ID do usuário que realizou a denúncia */
    private final Long denuncianteId;

    public Denuncia(Long id, MotivoDenunciaEnum motivo, String descricao,
                    LocalDateTime dataDenuncia, StatusDenunciaEnum status,
                    Long anuncioId, Long denuncianteId) {
        validar(motivo, descricao, anuncioId, denuncianteId);

        this.id = id;
        this.motivo = motivo;
        this.descricao = descricao;
        this.dataDenuncia = dataDenuncia != null ? dataDenuncia : LocalDateTime.now();
        this.status = status != null ? status : StatusDenunciaEnum.PENDENTE;
        this.anuncioId = anuncioId;
        this.denuncianteId = denuncianteId;
    }

    /**
     * Aprova a denúncia. Conforme RN-035, o anúncio deverá ser desativado em seguida.
     */
    public void aprovar() {
        if (!StatusDenunciaEnum.PENDENTE.equals(this.status)) {
            throw new DenunciaInvalidaException("Apenas denúncias PENDENTES podem ser aprovadas.");
        }
        this.status = StatusDenunciaEnum.APROVADA;
    }

    /**
     * Recusa a denúncia sem alterar o status do anúncio.
     */
    public void recusar() {
        if (!StatusDenunciaEnum.PENDENTE.equals(this.status)) {
            throw new DenunciaInvalidaException("Apenas denúncias PENDENTES podem ser recusadas.");
        }
        this.status = StatusDenunciaEnum.RECUSADA;
    }

    private void validar(MotivoDenunciaEnum motivo, String descricao,
                         Long anuncioId, Long denuncianteId) {
        if (motivo == null) {
            throw new DenunciaInvalidaException("O motivo da denúncia é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new DenunciaInvalidaException("A descrição da denúncia é obrigatória.");
        }
        if (anuncioId == null) {
            throw new DenunciaInvalidaException("O anúncio denunciado é obrigatório.");
        }
        if (denuncianteId == null) {
            throw new DenunciaInvalidaException("O denunciante deve ser identificado.");
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public MotivoDenunciaEnum getMotivo() { return motivo; }
    public String getDescricao() { return descricao; }
    public LocalDateTime getDataDenuncia() { return dataDenuncia; }
    public StatusDenunciaEnum getStatus() { return status; }
    public Long getAnuncioId() { return anuncioId; }
    public Long getDenuncianteId() { return denuncianteId; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Denuncia denuncia = (Denuncia) o;
        return Objects.equals(id, denuncia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
