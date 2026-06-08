import { useEffect, useState } from "react";

import { api } from "../api/services.js";
import {
  BarraNavegacao,
  ConfirmationModal,
  EmptyState,
  LoadingState,
  SiteFooter,
} from "../components/index.js";
import { dateTimeLabel, statusLabel } from "../lib/format.js";
import { useToast } from "../context/ToastContext.jsx";
import "./AdminDenuncias.css";

const AdminDenuncias = () => {
  const toast = useToast();
  const [loading, setLoading] = useState(true);
  const [denuncias, setDenuncias] = useState([]);
  const [modal, setModal] = useState({ open: false, aprovada: true, id: null, parecer: "" });

  useEffect(() => {
    api.admin.denuncias().then((items) => {
      setDenuncias(items);
      setLoading(false);
    }).catch((requestError) => toast.error(requestError.message));
  }, [toast]);

  const submitDecision = async () => {
    try {
      const updated = await api.admin.moderarDenuncia(modal.id, { aprovada: modal.aprovada, parecer: modal.parecer });
      setDenuncias((current) => current.map((item) => (item.id === updated.id ? updated : item)));
      toast.success("Denúncia moderada.");
      setModal({ open: false, aprovada: true, id: null, parecer: "" });
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  return (
    <div className="adminPage">
      <BarraNavegacao />
      <main className="adminShell">
        <header className="rentalsHeader">
          <h1>Painel administrativo de denúncias</h1>
          <p>Revise o motivo, o texto do denunciante e o resultado aplicado ao anúncio.</p>
        </header>

        {loading ? <LoadingState message="Carregando denúncias..." /> : null}
        {!loading && denuncias.length === 0 ? <EmptyState title="Sem denúncias pendentes" description="Quando denúncias forem registradas, elas aparecem aqui para moderação." /> : null}
        {!loading && denuncias.length > 0 ? (
          denuncias.map((denuncia) => (
            <section key={denuncia.id} className="formCard stack">
              <div className="inlineActions" style={{ justifyContent: "space-between", alignItems: "center" }}>
                <div>
                  <strong>{denuncia.tituloAnuncio}</strong>
                  <p className="cardTextMuted">{denuncia.denunciante} · {dateTimeLabel(denuncia.dataCriacao)}</p>
                </div>
                <span className="badge badge--info">{statusLabel(denuncia.status)}</span>
              </div>
              <div className="badgeRow">
                <span className="badge badge--warning">{denuncia.motivo}</span>
              </div>
              <p className="cardTextMuted">{denuncia.descricao}</p>
              {denuncia.parecerAdministrativo ? <div className="pageNotice">Parecer: {denuncia.parecerAdministrativo}</div> : null}
              <div className="inlineActions">
                <button type="button" className="button button--solidGreen" onClick={() => setModal({ open: true, aprovada: true, id: denuncia.id, parecer: "" })}>Aprovar denúncia</button>
                <button type="button" className="button button--secondary" onClick={() => setModal({ open: true, aprovada: false, id: denuncia.id, parecer: "" })}>Recusar denúncia</button>
              </div>
            </section>
          ))
        ) : null}
      </main>
      <SiteFooter />
      <ConfirmationModal
        aberto={modal.open}
        aoFechar={() => setModal({ open: false, aprovada: true, id: null, parecer: "" })}
        aoConfirmar={submitDecision}
        titulo={modal.aprovada ? "Aprovar denúncia" : "Recusar denúncia"}
        descricao={<label className="fieldLabel"><span>Parecer administrativo</span><textarea value={modal.parecer} onChange={(event) => setModal((current) => ({ ...current, parecer: event.target.value }))} /></label>}
        textoConfirmar="Salvar decisão"
        varianteConfirmar={modal.aprovada ? "primary" : "secondary"}
      />
    </div>
  );
};

export default AdminDenuncias;
