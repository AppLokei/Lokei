import { Link } from "react-router-dom";

import "./ToolCard.css";

const CardFerramenta = ({
  imagem,
  titulo,
  local,
  preco,
  valorDiario,
  rotuloCta = "Ver detalhes",
  hrefCta,
  aoClicarCta,
}) => {
  const priceValue =
    typeof valorDiario === "number"
      ? valorDiario.toFixed(2).replace(".", ",")
      : null;
  const parsedPrice = preco
    ? {
        currency: "R$",
        value: preco.replace("R$", "").split("/")[0].trim(),
        unit: preco.includes("/") ? preco.split("/")[1].trim() : "dia",
      }
    : null;
  const price = priceValue
    ? { currency: "R$", value: priceValue, unit: "dia" }
    : parsedPrice;

  return (
    <article className="toolCard">
      <div
        className="toolCardImage"
        style={imagem ? { backgroundImage: `url(${imagem})` } : undefined}
      />
      <div className="toolCardBody">
        <div className="toolCardHeader">
          <h3 className="toolCardTitle">{titulo}</h3>
          <p className="toolCardLocation">{local}</p>
        </div>
        {price ? (
          <div className="toolCardPrice" aria-label={`R$ ${price.value} por ${price.unit}`}>
            <span className="toolCardPriceCurrency">{price.currency}</span>
            <span className="toolCardPriceValue">{price.value}</span>
            <span className="toolCardPriceUnit">/{price.unit}</span>
          </div>
        ) : null}
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
};

export default CardFerramenta;
