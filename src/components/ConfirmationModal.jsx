import Botao from "./Button";
import "./ConfirmationModal.css";

const ConfirmationModal = ({
  aberto,
  aoFechar,
  aoConfirmar,
  titulo,
  descricao,
  textoConfirmar = "Confirmar",
  textoCancelar = "Voltar",
  varianteConfirmar = "primary",
}) => {
  if (!aberto) return null;

  const handleBackdropClick = (evento) => {
    if (evento.target.classList.contains("confirmOverlay")) {
      aoFechar();
    }
  };

  return (
    <div className="confirmOverlay" onClick={handleBackdropClick}>
      <div
        className="confirmCard"
        onClick={(evento) => evento.stopPropagation()}
        role="dialog"
        aria-modal="true"
        aria-labelledby="confirmTitle"
      >
        <header className="confirmHeader">
          <h3 id="confirmTitle">{titulo}</h3>
        </header>

        <div className="confirmBody">
          {typeof descricao === "string" ? <p>{descricao}</p> : descricao}
        </div>

        <footer className="confirmFooter">
          <Botao type="button" variante="secondary" onClick={aoFechar}>
            {textoCancelar}
          </Botao>
          <Botao type="button" variante={varianteConfirmar} onClick={aoConfirmar}>
            {textoConfirmar}
          </Botao>
        </footer>
      </div>
    </div>
  );
};

export default ConfirmationModal;
