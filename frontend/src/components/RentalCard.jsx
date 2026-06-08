import { Link } from "react-router-dom";

import { dateLabel, money, statusLabel } from "../lib/format.js";
import { resolveApiPath } from "../lib/url.js";
import "./RentalCard.css";

const variantByStatus = {
  CONCLUIDO: "success",
  CONFIRMADO: "warning",
  EM_APROVACAO: "warning",
  ATIVO: "warning",
  CANCELADO: "neutral",
  REPROVADO: "neutral",
};

const RentalCard = ({ aluguel, actions = [] }) => (
  <article className="rentalCard">
    <div
      className="rentalCardImage"
      style={aluguel.imagemPrincipalUrl ? { backgroundImage: `url(${resolveApiPath(aluguel.imagemPrincipalUrl)})` } : undefined}
    />
    <div className="rentalCardBody">
      <div className="rentalCardInfo">
        <h3 className="rentalCardTitle">{aluguel.tituloAnuncio}</h3>
        <p className="rentalCardPeriod">
          {dateLabel(aluguel.dataInicio)} até {dateLabel(aluguel.dataFim)} · {money(aluguel.valorTotal)}
        </p>
      </div>
      <div className={`rentalCardBadge rentalCardBadge--${variantByStatus[aluguel.status] || "neutral"}`}>
        {statusLabel(aluguel.status)}
      </div>
      <div className="rentalCardActions">
        <Link className="button button--secondary" to={`/meus-alugueis?detalhe=${aluguel.id}`}>
          Ver detalhe
        </Link>
        {actions.map((action) => (
          <button key={action.label} type="button" className={`button button--${action.variant || "primary"}`} onClick={action.onClick}>
            {action.label}
          </button>
        ))}
      </div>
    </div>
  </article>
);

export default RentalCard;
