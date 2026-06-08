import { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";

import { CampoEntrada, Botao } from "../components/index.js";
import { useAuth } from "../context/AuthContext.jsx";
import { useToast } from "../context/ToastContext.jsx";
import "./Login.css";

const Login = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const toast = useToast();
  const { login, loading } = useAuth();
  const [form, setForm] = useState({ email: "", senha: "" });
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      await login(form);
      toast.success("Sessão iniciada com sucesso.");
      navigate(location.state?.from || "/");
    } catch (requestError) {
      setError(requestError.message);
      toast.error(requestError.message);
    }
  };

  return (
    <div className="loginPage">
      <div className="loginSplit">
        <section className="loginBrand">
          <div className="loginBrandInner">
            <img src="/logo.png" alt="Lokei" className="loginBrandLogo" />
            <strong className="loginBrandTitle">Acesse sua conta e acompanhe anúncios, reservas e reputação.</strong>
            <p className="loginBrandSubtitle">O backend já está preparado com autenticação JWT, histórico de reservas, chat e notificações.</p>
          </div>
        </section>
        <section className="loginFormPanel">
          <header className="loginHeader">
            <span className="loginEyebrow">Entrar</span>
            <h1 className="loginTitle">Bem-vindo de volta</h1>
            <p className="loginSubtitle">Use seu e-mail e senha para continuar.</p>
          </header>
          <form className="loginForm" onSubmit={handleSubmit}>
            <CampoEntrada rotulo="E-mail" type="email" value={form.email} onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))} />
            <CampoEntrada rotulo="Senha" type="password" value={form.senha} onChange={(event) => setForm((current) => ({ ...current, senha: event.target.value }))} erro={error} />
            <Link className="loginForgot" to="/esqueci-senha">Esqueci minha senha</Link>
            <Botao type="submit" disabled={loading}>{loading ? "Entrando..." : "Entrar"}</Botao>
          </form>
          <div className="loginFooter">
            Ainda não tem conta? <Link to="/cadastro">Criar cadastro</Link>
          </div>
        </section>
      </div>
    </div>
  );
};

export default Login;
