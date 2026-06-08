import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";

import { api } from "../api/services.js";
import {
  BarraNavegacao,
  CardAluguel,
  ConfirmationModal,
  EmptyState,
  LoadingState,
  SiteFooter,
} from "../components/index.js";
import { dateLabel, money, statusLabel } from "../lib/format.js";
import { categoryLabel } from "../lib/format.js";
import { resolveApiPath } from "../lib/url.js";
import { useToast } from "../context/ToastContext.jsx";
import "./MeusAlugueis.css";

const tabMap = {
  alugando: { label: "Minhas reservas" },
  recebidos: { label: "Reservas recebidas" },
  anuncios: { label: "Meus anúncios" },
};

const MeusAlugueis = () => {
  const toast = useToast();
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(true);
  const [meus, setMeus] = useState([]);
  const [recebidos, setRecebidos] = useState([]);
  const [anuncios, setAnuncios] = useState([]);
  const [detalhe, setDetalhe] = useState(null);
  const [modal, setModal] = useState({ open: false, mode: "", aluguelId: null, motivo: "" });

  const aba = searchParams.get("aba") || "alugando";
  const detalheId = searchParams.get("detalhe");

  useEffect(() => {
    let active = true;
    setLoading(true);
    Promise.all([api.alugueis.meus(), api.alugueis.recebidos(), api.anuncios.meus()])
      .then(([tenant, owner, ownAds]) => {
        if (!active) return;
        setMeus(tenant);
        setRecebidos(owner);
        setAnuncios(ownAds);
      })
      .catch((requestError) => toast.error(requestError.message))
      .finally(() => active && setLoading(false));
    return () => {
      active = false;
    };
  }, [toast]);

  useEffect(() => {
    if (!detalheId) {
      setDetalhe(null);
      return;
    }
    api.alugueis.detalhar(detalheId).then(setDetalhe).catch((requestError) => toast.error(requestError.message));
  }, [detalheId, toast]);

  const rentals = useMemo(() => (aba === "recebidos" ? recebidos : meus), [aba, meus, recebidos]);

  const openDetail = (id) => {
    const next = new URLSearchParams(searchParams);
    next.set("detalhe", id);
    setSearchParams(next);
  };

  const closeDetail = () => {
    const next = new URLSearchParams(searchParams);
    next.delete("detalhe");
    setSearchParams(next);
  };

  const performRentalAction = async () => {
    try {
      if (modal.mode === "cancelar") {
        await api.alugueis.cancelar(modal.aluguelId, { motivo: modal.motivo });
      }
      if (modal.mode === "reprovar") {
        await api.alugueis.reprovar(modal.aluguelId, { motivo: modal.motivo });
      }
      if (modal.mode === "aprovar") {
        await api.alugueis.aprovar(modal.aluguelId);
      }
      toast.success("Operação concluída.");
      window.location.reload();
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  const performAdAction = async (action, id) => {
    try {
      if (action === "pausar") await api.anuncios.pausar(id);
      if (action === "reativar") await api.anuncios.reativar(id);
      if (action === "excluir") await api.anuncios.excluir(id);
      toast.success("Ação aplicada no anúncio.");
      setAnuncios((current) => current.filter((item) => (action === "excluir" ? item.id !== id : true)));
      if (action !== "excluir") {
        const updated = await api.anuncios.meus();
        setAnuncios(updated);
      }
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  const goToChat = async (aluguelId) => {
    try {
      const chat = await api.alugueis.abrirChat(aluguelId);
      navigate(`/chat?chatId=${chat.id}`);
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  return (
    <div className="rentalsPage">
      <BarraNavegacao />
      <main className="rentalsContainer">
        <header className="rentalsHeader">
          <h1>Operação da conta</h1>
          <p>Acompanhe reservas como locatário, aprovações como locador e o ciclo de vida dos seus anúncios.</p>
        </header>

        <div className="rentalsTabs">
          {Object.entries(tabMap).map(([key, value]) => (
            <button key={key} type="button" className={`rentalsTab${aba === key ? " active" : ""}`} onClick={() => setSearchParams({ aba: key })}>
              {value.label}
            </button>
          ))}
        </div>

        {loading ? <LoadingState message="Carregando dados operacionais..." /> : null}

        {!loading && aba !== "anuncios" ? (
          <div className="stack">
            {rentals.length === 0 ? (
              <EmptyState title="Sem itens nesta aba" description="Assim que houver reservas neste fluxo, elas aparecem aqui." />
            ) : (
              rentals.map((aluguel) => {
                const actions = [];
                if (aba === "alugando" && aluguel.status === "EM_APROVACAO") {
                  actions.push({ label: "Cancelar", variant: "outlineRed", onClick: () => setModal({ open: true, mode: "cancelar", aluguelId: aluguel.id, motivo: "" }) });
                }
                if (aba === "recebidos" && aluguel.status === "EM_APROVACAO") {
                  actions.push({ label: "Aprovar", variant: "solidGreen", onClick: async () => { await api.alugueis.aprovar(aluguel.id); toast.success("Reserva aprovada."); window.location.reload(); } });
                  actions.push({ label: "Reprovar", variant: "outlineRed", onClick: () => setModal({ open: true, mode: "reprovar", aluguelId: aluguel.id, motivo: "" }) });
                }
                return (
                  <div key={aluguel.id} onClick={() => openDetail(aluguel.id)}>
                    <CardAluguel aluguel={aluguel} actions={actions} />
                  </div>
                );
              })
            )}
          </div>
        ) : null}

        {!loading && aba === "anuncios" ? (
          <div className="stack">
            {anuncios.length === 0 ? (
              <EmptyState title="Nenhum anúncio publicado" description="Use a tela de criação para colocar sua primeira ferramenta no catálogo." />
            ) : (
              anuncios.map((anuncio) => (
                <section key={anuncio.id} className="formCard stack">
                  <div className="inlineActions" style={{ justifyContent: "space-between", alignItems: "center" }}>
                    <div>
                      <strong>{anuncio.titulo}</strong>
                      <p className="cardTextMuted">{categoryLabel(anuncio.categoria)} · {money(anuncio.valorDiario)}</p>
                    </div>
                    <span className="badge badge--info">{statusLabel(anuncio.status)}</span>
                  </div>
                  {anuncio.imagemPrincipalUrl ? <img src={resolveApiPath(anuncio.imagemPrincipalUrl)} alt={anuncio.titulo} style={{ borderRadius: 16, height: 220, objectFit: "cover", width: "100%" }} /> : null}
                  <p className="cardTextMuted">{anuncio.descricaoCurta}</p>
                  <div className="inlineActions">
                    <Link className="button button--secondary" to={`/anuncios/${anuncio.id}`}>Ver detalhe</Link>
                    <Link className="button button--secondary" to={`/anuncios/${anuncio.id}/editar`}>Editar</Link>
                    {anuncio.status === "ATIVO" ? <button type="button" className="button button--secondary" onClick={() => performAdAction("pausar", anuncio.id)}>Pausar</button> : null}
                    {anuncio.status === "PAUSADO" ? <button type="button" className="button button--secondary" onClick={() => performAdAction("reativar", anuncio.id)}>Reativar</button> : null}
                    <button type="button" className="button button--outlineRed" onClick={() => performAdAction("excluir", anuncio.id)}>Excluir</button>
                  </div>
                </section>
              ))
            )}
          </div>
        ) : null}

        {detalhe ? (
          <section className="formCard stack">
            <div className="inlineActions" style={{ justifyContent: "space-between", alignItems: "center" }}>
              <div>
                <strong>{detalhe.tituloAnuncio}</strong>
                <p className="cardTextMuted">{statusLabel(detalhe.status)} · {money(detalhe.valorTotal)}</p>
              </div>
              <button type="button" className="button button--secondary" onClick={closeDetail}>Fechar detalhe</button>
            </div>
            <div className="formRow">
              <div className="fieldLabel"><span>Período</span><input disabled value={`${dateLabel(detalhe.dataInicio)} até ${dateLabel(detalhe.dataFim)}`} /></div>
              <div className="fieldLabel"><span>Valor total</span><input disabled value={money(detalhe.valorTotal)} /></div>
            </div>
            <p className="cardTextMuted">Locatário: {detalhe.locatario} · Proprietário: {detalhe.proprietario}</p>
            <p className="cardTextMuted">{detalhe.descricaoAnuncio}</p>
            {detalhe.motivoReprovacao ? <div className="pageNotice">Motivo da reprovação: {detalhe.motivoReprovacao}</div> : null}
            {detalhe.motivoCancelamento ? <div className="pageNotice">Motivo do cancelamento: {detalhe.motivoCancelamento}</div> : null}
            <div className="inlineActions">
              {detalhe.cancelavel ? <button type="button" className="button button--outlineRed" onClick={() => setModal({ open: true, mode: "cancelar", aluguelId: detalhe.id, motivo: "" })}>Cancelar reserva</button> : null}
              {detalhe.chatDisponivel ? <button type="button" className="button button--secondary" onClick={() => goToChat(detalhe.id)}>Abrir chat</button> : null}
              {detalhe.podeAvaliarAnuncio ? <button type="button" className="button button--primary" onClick={() => navigate(`/avaliar?tipo=anuncio&aluguelId=${detalhe.id}`)}>Avaliar anúncio</button> : null}
              {detalhe.podeAvaliarContraparte ? <button type="button" className="button button--secondary" onClick={() => navigate(`/avaliar?tipo=perfil&aluguelId=${detalhe.id}`)}>Avaliar contraparte</button> : null}
            </div>
          </section>
        ) : null}
      </main>
      <SiteFooter />
      <ConfirmationModal
        aberto={modal.open}
        aoFechar={() => setModal({ open: false, mode: "", aluguelId: null, motivo: "" })}
        aoConfirmar={performRentalAction}
        titulo={modal.mode === "reprovar" ? "Reprovar solicitação" : "Cancelar reserva"}
        descricao={<label className="fieldLabel"><span>Motivo</span><textarea value={modal.motivo} onChange={(event) => setModal((current) => ({ ...current, motivo: event.target.value }))} /></label>}
        textoConfirmar="Confirmar ação"
        varianteConfirmar={modal.mode === "reprovar" || modal.mode === "cancelar" ? "outlineRed" : "primary"}
      />
    </div>
  );
};

export default MeusAlugueis;
