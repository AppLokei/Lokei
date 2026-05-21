import "./Button.css";

const Botao = ({ children, variante = "primary", ...props }) => (
  <button className={`button button--${variante}`} {...props}>
    {children}
  </button>
);

export default Botao;
