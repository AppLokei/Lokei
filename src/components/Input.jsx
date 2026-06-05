import "./Input.css";

const CampoEntrada = ({ rotulo, erro, acao, ...props }) => (
  <label className="inputField">
    <span className="inputLabel">{rotulo}</span>
    <span className="inputControlWrapper">
      <input
        className={`inputControl${erro ? " inputControl--error" : ""}`}
        {...props}
      />
      {acao ? <span className="inputAction">{acao}</span> : null}
    </span>
    {erro ? <span className="inputError">{erro}</span> : null}
  </label>
);

export default CampoEntrada;
