import { Link } from "react-router-dom";

import { money } from "../lib/format.js";
import { resolveApiPath } from "../lib/url.js";
import "./ToolCard.css";

const ToolCard = ({ anuncio }) => (
  <article className="toolCard">
    <div
      className="toolCardImage"
      style={anuncio.imagemPrincipalUrl ? { backgroundImage: `url(${resolveApiPath(anuncio.imagemPrincipalUrl)})` } : undefined}
      aria-hidden="true"
    />
    <div className="toolCardBody">
      <div className="toolCardHeader">
        <h3 className="toolCardTitle">{anuncio.titulo}</h3>
        <p className="toolCardLocation">{anuncio.cidade || "Cidade não informada"}</p>
      </div>
      <div className="toolCardPrice" aria-label={`${money(anuncio.valorDiario)} por dia`}>
        <span className="toolCardPriceCurrency">R$</span>
        <span className="toolCardPriceValue">{Number(anuncio.valorDiario || 0).toFixed(2).replace(".", ",")}</span>
        <span className="toolCardPriceUnit">/dia</span>
      </div>
      <p className="cardTextMuted">{anuncio.descricaoCurta}</p>
    </div>
    <Link className="toolCardCta" to={`/anuncios/${anuncio.id}`} aria-label={`Ver detalhes de ${anuncio.titulo}`}>
      Ver detalhes
    </Link>
  </article>
);

export default ToolCard;
