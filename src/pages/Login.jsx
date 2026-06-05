import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import "./Login.css";

const Login = () => {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const [erros, setErros] = useState({});
  const navigate = useNavigate();

  const validar = () => {
    const proximosErros = {};
    if (!login.trim()) {
      proximosErros.login = "Informe seu e-mail.";
    } else if (!login.includes("@")) {
      proximosErros.login = "Digite um e-mail valido.";
    }

    if (!senha.trim()) {
      proximosErros.senha = "Digite sua senha.";
    }

    return proximosErros;
  };

  const handleSubmit = (evento) => {
    evento.preventDefault();
    const proximosErros = validar();
    setErros(proximosErros);

    if (Object.keys(proximosErros).length > 0) return;

    console.log({ login, senha });
    navigate("/");
  };

  return (
    <div className="loginPage">
      <div className="loginSplit">
        <section className="loginBrand">
          <div className="loginBrandInner">
            <img className="loginBrandLogo" src="/logo-transparent.png" alt="Lokei Logo" />
            <h1 className="loginBrandTitle">
              Alugue as ferramentas que voce precisa, quando precisar.
            </h1>
            <p className="loginBrandSubtitle">
              Tudo o que voce precisa para projetos rapidos, com diaria clara e retirada facil.
            </p>
          </div>
        </section>

        <section className="loginFormPanel">
          <header className="loginHeader">
            <p className="loginEyebrow">Acesse sua conta</p>
            <h2 className="loginTitle">Bem-vindo de volta</h2>
            <p className="loginSubtitle">Entre para continuar alugando.</p>
          </header>

          <form className="loginForm" onSubmit={handleSubmit}>
            <CampoEntrada
              rotulo="E-mail"
              name="login"
              placeholder="Digite seu email@dominio.com"
              value={login}
              onChange={(evento) => setLogin(evento.target.value)}
              erro={erros.login}
            />
            <p className="loginPrivacy">Seus dados sao protegidos e nunca compartilhados.</p>
            <CampoEntrada
              rotulo="Senha"
              name="senha"
              type={mostrarSenha ? "text" : "password"}
              placeholder="Digite sua senha"
              value={senha}
              onChange={(evento) => setSenha(evento.target.value)}
              erro={erros.senha}
              acao={
                <button
                  type="button"
                  className="inputActionButton"
                  onClick={() => setMostrarSenha((prev) => !prev)}
                >
                  {mostrarSenha ? "Ocultar" : "Mostrar"}
                </button>
              }
            />

            <Botao type="submit">Entrar</Botao>
            <Link className="loginForgot" to="/esqueci-senha">
              Esqueci minha senha
            </Link>
          </form>

          <div className="loginFooter">
            Ainda nao tem conta? <Link to="/cadastro">Cadastre-se</Link>
          </div>
        </section>
      </div>
    </div>
  );
};

export default Login;
