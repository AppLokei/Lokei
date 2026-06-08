import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

import { api } from "../api/services.js";
import { Botao, LoadingState } from "../components/index.js";
import { useToast } from "../context/ToastContext.jsx";
import { resolveApiPath } from "../lib/url.js";
import "./Avaliacao.css";

const Avaliacao = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const toast = useToast();
  const aluguelId = searchParams.get("aluguelId");
  const tipo = searchParams.get("tipo") || "anuncio";
  const [detalhe, setDetalhe] = useState(null);
  const [loading, setLoading] = useState(true);
  const [nota, setNota] = useState(0);
  const [comentario, setComentario] = useState("");
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!aluguelId) return;
    api.alugueis.detalhar(aluguelId).then((data) => {
      setDetalhe(data);
      setLoading(false);
    }).catch((requestError) => toast.error(requestError.message));
  }, [aluguelId, toast]);

  const submit = async (event) => {
    event.preventDefault();
    setSubmitting(true);
    try {
      if (tipo === "perfil") {
        await api.avaliacoes.perfil({ aluguelId: Number(aluguelId), nota, comentario });
      } else {
        await api.avaliacoes.anuncio({ aluguelId: Number(aluguelId), nota, comentario });
      }
      toast.success("Avaliação enviada.");
      navigate(`/meus-alugueis?detalhe=${aluguelId}`);
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading || !detalhe) return <LoadingState message="Carregando dados da avaliação..." />;

  return (
    <div className="avaliacaoPage" onClick={() => navigate(-1)}>
      <main className="avaliacaoDrawer" onClick={(event) => event.stopPropagation()}>
        <header className="avaliacaoHeader">
          <span className="avaliacaoBadge">{tipo === "perfil" ? "Contraparte" : "Anúncio"}</span>
          <h1>Registrar avaliação</h1>
          <p>As avaliações só são liberadas após a conclusão do aluguel.</p>
        </header>
        <div className="avaliacaoResumo">
          {detalhe.imagemPrincipalUrl ? <img className="avaliacaoResumoImage" src={resolveApiPath(detalhe.imagemPrincipalUrl)} alt={detalhe.tituloAnuncio} /> : null}
          <div className="avaliacaoResumoInfo">
            <strong>{detalhe.tituloAnuncio}</strong>
            <span>{tipo === "perfil" ? `Avaliar ${detalhe.proprietario}` : "Avaliar a ferramenta"}</span>
          </div>
        </div>
        <form className="avaliacaoForm" onSubmit={submit}>
          <div className="avaliacaoField">
            <span className="avaliacaoLabel">Nota</span>
            <div className="avaliacaoStars">
              {[1, 2, 3, 4, 5].map((value) => (
                <button key={value} type="button" className="avaliacaoStar" onClick={() => setNota(value)}>
                  <svg className={`starIcon${nota >= value ? " starIcon--active" : ""}`} viewBox="0 0 24 24" aria-hidden="true">
                    <path d="m12 3.6 2.6 5.3 5.8.8-4.2 4.1 1 5.8L12 16.9l-5.2 2.7 1-5.8L3.6 9.7l5.8-.8Z" />
                  </svg>
                </button>
              ))}
            </div>
          </div>
          <label className="avaliacaoField">
            <span className="avaliacaoLabel">Comentário</span>
            <textarea className="avaliacaoTextarea" value={comentario} onChange={(event) => setComentario(event.target.value)} />
          </label>
          <Botao type="submit" disabled={submitting || nota < 1}>{submitting ? "Enviando..." : "Enviar avaliação"}</Botao>
        </form>
      </main>
    </div>
  );
};

export default Avaliacao;
