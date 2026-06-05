import { useState } from "react";
import { Link } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import "./EsqueciSenha.css";

const EsqueciSenha = () => {
  const [email, setEmail] = useState("");
  const [erros, setErros] = useState({});
  const [enviado, setEnviado] = useState(false);

  const validar = () => {
    const proximosErros = {};
    if (!email.trim()) {
      proximosErros.email = "Informe seu e-mail cadastrado.";
    } else if (!email.includes("@")) {
      proximosErros.email = "Digite um e-mail válido.";
    }
    return proximosErros;
  };

  const handleSubmit = (evento) => {
    evento.preventDefault();
    const proximosErros = validar();
    setErros(proximosErros);

    if (Object.keys(proximosErros).length > 0) return;

    console.log("Recuperar senha para:", email);
    setEnviado(true);
  };

  return (
    <div className="esqueciSenhaPage">
      <div className="esqueciSenhaSplit">
        <section className="esqueciSenhaBrand">
          <div className="esqueciSenhaBrandInner">
            <img className="esqueciSenhaBrandLogo" src="/logo-transparent.png" alt="Lokei Logo" />
            <h1 className="esqueciSenhaBrandTitle">
              Alugue as ferramentas que você precisa, quando precisar.
            </h1>
            <p className="esqueciSenhaBrandSubtitle">
              Tudo o que você precisa para projetos rápidos, com diária clara e retirada fácil.
            </p>
          </div>
        </section>

        <section className="esqueciSenhaFormPanel">
          {enviado ? (
            <div className="esqueciSenhaSuccess">
              <div className="esqueciSenhaSuccessIcon">✓</div>
              <h2>E-mail enviado</h2>
              <p>
                Enviamos um link de redefinição para <strong>{email}</strong>. Se este e-mail estiver cadastrado, você receberá o link em breve.
              </p>
              <Link className="esqueciSenhaLink" style={{ marginTop: "12px" }} to="/login">
                Voltar para o Login
              </Link>
            </div>
          ) : (
            <>
              <header className="esqueciSenhaHeader">
                <p className="esqueciSenhaEyebrow">Recuperação de conta</p>
                <h2 className="esqueciSenhaTitle">Recuperar senha</h2>
                <p className="esqueciSenhaSubtitle">
                  Insira o seu e-mail cadastrado e enviaremos as instruções para redefinir sua senha.
                </p>
              </header>

              <form className="esqueciSenhaForm" onSubmit={handleSubmit}>
                <CampoEntrada
                  rotulo="E-mail cadastrado"
                  name="email"
                  placeholder="Digite seu email@dominio.com"
                  value={email}
                  onChange={(evento) => setEmail(evento.target.value)}
                  erro={erros.email}
                />

                <Botao type="submit">Enviar link de recuperação</Botao>
                <Link className="esqueciSenhaLink" to="/login">
                  Voltar ao Login
                </Link>
              </form>
            </>
          )}
        </section>
      </div>
    </div>
  );
};

export default EsqueciSenha;
