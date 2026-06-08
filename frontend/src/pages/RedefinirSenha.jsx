import { useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";

import { Botao, CampoEntrada } from "../components/index.js";
import { api } from "../api/services.js";
import { useToast } from "../context/ToastContext.jsx";
import "./EsqueciSenha.css";

const RedefinirSenha = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const toast = useToast();
  const [token, setToken] = useState(searchParams.get("token") || "");
  const [novaSenha, setNovaSenha] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      await api.auth.resetPassword({ token, novaSenha });
      toast.success("Senha redefinida com sucesso.");
      navigate("/login");
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
            <strong className="esqueciSenhaBrandTitle">Redefina sua senha com o token recebido.</strong>
            <p className="esqueciSenhaBrandSubtitle">A nova senha precisa seguir a regra mínima do backend: 8 caracteres com letras e números.</p>
          </div>
        </section>
        <section className="esqueciSenhaFormPanel">
          <header className="esqueciSenhaHeader">
            <span className="esqueciSenhaEyebrow">Redefinição</span>
            <h1 className="esqueciSenhaTitle">Nova senha</h1>
            <p className="esqueciSenhaSubtitle">Cole o token e defina uma senha nova para voltar ao login.</p>
          </header>
          <form className="esqueciSenhaForm" onSubmit={handleSubmit}>
            <CampoEntrada rotulo="Token" value={token} onChange={(event) => setToken(event.target.value)} />
            <CampoEntrada rotulo="Nova senha" type="password" value={novaSenha} onChange={(event) => setNovaSenha(event.target.value)} />
            <Botao type="submit" disabled={loading}>{loading ? "Salvando..." : "Redefinir senha"}</Botao>
          </form>
          <Link className="esqueciSenhaLink" to="/login">Voltar para o login</Link>
        </section>
      </div>
    </div>
  );
};

export default RedefinirSenha;
