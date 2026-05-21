import { Link } from "react-router-dom";

import "./ToolCard.css";

const CardFerramenta = ({
  imagem,
  titulo,
  local,
  preco,
  rotuloCta = "Ver detalhes",
  hrefCta,
  aoClicarCta,
}) => (
  <article className="toolCard">
    <div
      className="toolCardImage"
      style={imagem ? { backgroundImage: `url(${imagem})` } : undefined}
    />
    <div className="toolCardBody">
      <h3 className="toolCardTitle">{titulo}</h3>
      <p className="toolCardLocation">{local}</p>
      <span className="toolCardPrice">{preco}</span>
    </div>
    {hrefCta ? (
      <Link className="toolCardCta" to={hrefCta} aria-label={`${rotuloCta} - ${titulo}`}>
        {rotuloCta}
      </Link>
    ) : (
      <button
        type="button"
        className="toolCardCta"
        onClick={aoClicarCta}
        aria-label={`${rotuloCta} - ${titulo}`}
      >
        {rotuloCta}
      </button>
    )}
  </article>
);

export default CardFerramenta;
