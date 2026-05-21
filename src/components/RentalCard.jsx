import Botao from "./Button";
import "./RentalCard.css";

const configuracaoStatus = {
  concluido: { rotulo: "Concluido", semantica: "success" },
  aprovado: { rotulo: "Aprovado", semantica: "success" },
  analise: { rotulo: "Em Analise", semantica: "warning" },
  andamento: { rotulo: "Em Andamento", semantica: "warning" },
  pausado: { rotulo: "Pausado", semantica: "neutral" },
  cancelado: { rotulo: "Cancelado", semantica: "neutral" },
};

const CardAluguel = ({
  imagem,
  titulo,
  periodo,
  status,
  acao,
  acoesLocador,
}) => {
  const configuracao = configuracaoStatus[status] || {
    rotulo: status,
    semantica: "neutral",
  };

  return (
    <article className="rentalCard">
      <div
        className="rentalCardImage"
        style={imagem ? { backgroundImage: `url(${imagem})` } : undefined}
      />
      <div className="rentalCardBody">
        <div>
          <h3 className="rentalCardTitle">{titulo}</h3>
          <p className="rentalCardPeriod">{periodo}</p>
        </div>
        <div className={`rentalCardBadge rentalCardBadge--${configuracao.semantica}`}>
          {configuracao.rotulo}
        </div>
        {acao ? (
          <Botao type="button" variante={acao.variante} onClick={acao.aoClicar}>
            {acao.rotulo}
          </Botao>
        ) : null}
        {acoesLocador ? (
          <div className="rentalCardActions">
            {acoesLocador.map((acaoLocador) => (
              <Botao
                key={acaoLocador.rotulo}
                type="button"
                variante={acaoLocador.variante}
                onClick={acaoLocador.aoClicar}
              >
                {acaoLocador.rotulo}
              </Botao>
            ))}
          </div>
        ) : null}
      </div>
    </article>
  );
};

export default CardAluguel;
