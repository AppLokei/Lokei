import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { api } from "../api/services.js";
import {
  BarraNavegacao,
  EmptyState,
  ErrorState,
  LoadingState,
  SiteFooter,
} from "../components/index.js";
import { dateTimeLabel, initials } from "../lib/format.js";
import { useToast } from "../context/ToastContext.jsx";
import { useAuth } from "../context/AuthContext.jsx";
import "./Perfil.css";

const Perfil = () => {
  const toast = useToast();
  const { logout, isAdmin } = useAuth();
  const [loading, setLoading] = useState(true);
  const [perfil, setPerfil] = useState(null);
  const [avaliacoes, setAvaliacoes] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    setLoading(true);
    api.profile
      .get()
      .then(async (data) => {
        if (!active) return;
        setPerfil(data);
        const reviews = await api.avaliacoes.listarUsuario(data.id);
        if (active) setAvaliacoes(reviews);
      })
      .catch((requestError) => {
        if (active) {
          setError(requestError.message);
          toast.error(requestError.message);
        }
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, [toast]);

  return (
    <div className="perfilPage">
      <BarraNavegacao />
      <main className="perfilShell">
        {loading ? <LoadingState message="Carregando perfil..." /> : null}
        {!loading && error ? <ErrorState message={error} /> : null}
        {!loading && perfil ? (
          <>
            <section className="perfilHero">
              <div className="perfilIdentity">
                <div className="perfilAvatar"><span className="perfilAvatarInitials">{initials(perfil.nome)}</span></div>
                <div className="perfilIdentityText">
                  <div className="perfilNameRow">
                    <strong className="perfilName">{perfil.nome}</strong>
                    <span className="badge badge--info">{perfil.papel}</span>
                  </div>
                  <div className="perfilMeta">
                    <span>{perfil.email}</span>
                    <span>{perfil.telefone}</span>
                    <span>{perfil.endereco?.cidade} · {perfil.endereco?.estado}</span>
                  </div>
                </div>
              </div>
              <div className="inlineActions">
                <Link className="button button--primary" to="/perfil/editar">Editar perfil</Link>
                <Link className="button button--secondary" to="/meus-anuncios">Meus anúncios</Link>
                <Link className="button button--secondary" to="/reservas-recebidas">Reservas recebidas</Link>
                {isAdmin ? <Link className="button button--secondary" to="/admin/denuncias">Painel admin</Link> : null}
                <button type="button" className="button button--outlineRed" onClick={logout}>Sair</button>
              </div>
            </section>

            <section className="formCard stack">
              <div className="sectionTitle">Dados cadastrados</div>
              <div className="formRow">
                <div className="fieldLabel"><span>CPF</span><input value={perfil.cpf} disabled readOnly /></div>
                <div className="fieldLabel"><span>Telefone</span><input value={perfil.telefone} disabled readOnly /></div>
              </div>
              <div className="formRow">
                <div className="fieldLabel"><span>Logradouro</span><input value={perfil.endereco?.logradouro || ""} disabled readOnly /></div>
                <div className="fieldLabel"><span>Número</span><input value={perfil.endereco?.numero || ""} disabled readOnly /></div>
              </div>
              <div className="formRow">
                <div className="fieldLabel"><span>Bairro</span><input value={perfil.endereco?.bairro || ""} disabled readOnly /></div>
                <div className="fieldLabel"><span>CEP</span><input value={perfil.endereco?.cep || ""} disabled readOnly /></div>
              </div>
            </section>

            <section className="formCard stack">
              <div className="sectionTitle">Avaliações recebidas</div>
              {avaliacoes.length === 0 ? (
                <EmptyState title="Sem avaliações ainda" description="A reputação aparece aqui quando reservas concluídas forem avaliadas." />
              ) : (
                avaliacoes.map((avaliacao) => (
                  <article key={avaliacao.id} className="detailReview">
                    <div className="detailReviewHeader">
                      <strong>{avaliacao.autor}</strong>
                      <span>{dateTimeLabel(avaliacao.dataCriacao)}</span>
                    </div>
                    <p className="cardTextMuted">Nota {avaliacao.nota}/5 · {avaliacao.comentario}</p>
                  </article>
                ))
              )}
            </section>
          </>
        ) : null}
      </main>
      <SiteFooter />
    </div>
  );
};

export default Perfil;
