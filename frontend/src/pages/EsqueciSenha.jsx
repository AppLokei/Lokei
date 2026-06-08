import { useState } from "react";
import { Link } from "react-router-dom";

import { Botao, CampoEntrada } from "../components/index.js";
import { api } from "../api/services.js";
import { useToast } from "../context/ToastContext.jsx";
import "./EsqueciSenha.css";

const EsqueciSenha = () => {
  const toast = useToast();
  const [email, setEmail] = useState("");
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      await api.auth.forgotPassword({ email });
      setSuccess(true);
      toast.success("Instruções de recuperação registradas.");
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="esqueciSenhaPage">
      <div className="esqueciSenhaSplit">
        <section className="esqueciSenhaBrand">
          <div className="esqueciSenhaBrandInner">
            <img src="/logo.png" alt="Lokei" className="esqueciSenhaBrandLogo" />
            <strong className="esqueciSenhaBrandTitle">Recupere o acesso com um token de redefinição.</strong>
            <p className="esqueciSenhaBrandSubtitle">No ambiente atual, o token de recuperação também aparece nas notificações da conta.</p>
          </div>
        </section>
        <section className="esqueciSenhaFormPanel">
          {!success ? (
            <>
              <header className="esqueciSenhaHeader">
                <span className="esqueciSenhaEyebrow">Recuperação</span>
                <h1 className="esqueciSenhaTitle">Esqueci minha senha</h1>
                <p className="esqueciSenhaSubtitle">Informe o e-mail cadastrado para gerar um token de redefinição.</p>
              </header>
              <form className="esqueciSenhaForm" onSubmit={handleSubmit}>
                <CampoEntrada rotulo="E-mail" type="email" value={email} onChange={(event) => setEmail(event.target.value)} />
                <Botao type="submit" disabled={loading}>{loading ? "Enviando..." : "Gerar token"}</Botao>
              </form>
            </>
          ) : (
            <div className="esqueciSenhaSuccess">
              <div className="esqueciSenhaSuccessIcon">✓</div>
              <h2>Solicitação registrada</h2>
              <p>Use o token recebido para acessar a tela de redefinição.</p>
            </div>
          )}
          <Link className="esqueciSenhaLink" to="/redefinir-senha">Já tenho um token</Link>
          <Link className="esqueciSenhaLink" to="/login">Voltar para o login</Link>
        </section>
      </div>
    </div>
  );
};

export default EsqueciSenha;
