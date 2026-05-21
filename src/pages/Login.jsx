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
    const temEmail = login.includes("@");

    if (!login.trim()) {
      proximosErros.login = "Informe seu e-mail ou CPF.";
    } else if (temEmail && !login.includes("@")) {
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
      <div className="loginCard">
        <header className="loginHeader">
          <div className="loginLogo">
            Lo<span className="loginHighlight">kei</span>
          </div>
          <div>
            <h1 className="loginTitle">Bem-vindo de volta</h1>
            <p className="loginSubtitle">
              Acesse sua conta para continuar alugando.
            </p>
          </div>
        </header>

        <form className="loginForm" onSubmit={handleSubmit}>
          <CampoEntrada
            rotulo="E-mail ou CPF"
            name="login"
            placeholder="email@dominio.com"
            value={login}
            onChange={(evento) => setLogin(evento.target.value)}
            erro={erros.login}
          />
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

          <Link className="loginForgot" to="/">
            Esqueci minha senha
          </Link>

          <Botao type="submit">Entrar</Botao>

        </form>

        <div className="loginFooter">
          Ainda nao tem conta? <Link to="/cadastro">Cadastre-se</Link>
        </div>
      </div>
    </div>
  );
};

export default Login;
