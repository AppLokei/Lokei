package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Mensagem;
import Lokei.aplication.domain.gateways.MensagemGateway;
import Lokei.aplication.infrastructure.persistence.entities.MensagemEntity;
import Lokei.aplication.infrastructure.persistence.mapper.MensagemMapper;
import Lokei.aplication.infrastructure.persistence.repository.MensagemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MensagemRepositoryGateway implements MensagemGateway {

    private final MensagemRepository mensagemRepository;

    public MensagemRepositoryGateway(MensagemRepository mensagemRepository) {
        this.mensagemRepository = mensagemRepository;
    }

    @Override
    public Mensagem enviarMensagem(Mensagem mensagem) {
        MensagemEntity entity = MensagemMapper.toEntity(mensagem);
        MensagemEntity salvo = mensagemRepository.save(entity);
        return MensagemMapper.toDomain(salvo);
    }

    @Override
    public Optional<Mensagem> buscarMensagemPorId(Long id) {
        return mensagemRepository.findById(id).map(MensagemMapper::toDomain);
    }

    @Override
    public List<Mensagem> buscarMensagensPorChat(Long chatId) {
        return mensagemRepository.findByChat_IdOrderByDataHoraEnvioAsc(chatId)
                .stream().map(MensagemMapper::toDomain).toList();
    }

    @Override
    public void marcarMensagensComoLidas(Long chatId, Long destinatarioId) {
        mensagemRepository.marcarComoLidas(chatId, destinatarioId);
    }
}
