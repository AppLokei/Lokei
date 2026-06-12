package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Mensagem;
import Lokei.aplication.infrastructure.persistence.entities.MensagemEntity;

public class MensagemMapper {

    public static Mensagem toDomain(MensagemEntity entity) {
        return new Mensagem(
                entity.getId(),
                entity.getConteudo(),
                entity.getDataHoraEnvio(),
                entity.isLida(),
                entity.getRemetenteId(),
                entity.getChatId()
        );
    }

    public static MensagemEntity toEntity(Mensagem mensagem) {
        MensagemEntity entity = new MensagemEntity();
        entity.setId(mensagem.getId());
        entity.setConteudo(mensagem.getConteudo());
        entity.setDataHoraEnvio(mensagem.getDataHoraEnvio());
        entity.setLida(mensagem.isLida());
        entity.setRemetenteId(mensagem.getRemetenteId());
        entity.setChatId(mensagem.getChatId());
        return entity;
    }
}
