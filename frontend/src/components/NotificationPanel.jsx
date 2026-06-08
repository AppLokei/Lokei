import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import { api } from "../api/services.js";
import { dateTimeLabel } from "../lib/format.js";
import { useToast } from "../context/ToastContext.jsx";
import { EmptyState, LoadingState } from "./PageStates.jsx";

const NotificationPanel = ({ open, onClose }) => {
  const [loading, setLoading] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const toast = useToast();

  useEffect(() => {
    if (!open) return;
    let active = true;
    setLoading(true);
    api.notificacoes
      .listar()
      .then((items) => {
        if (active) setNotifications(items);
      })
      .catch((error) => {
        if (active) toast.error(error.message);
      })
      .finally(() => {
        if (active) setLoading(false);
      });
    return () => {
      active = false;
    };
  }, [open, toast]);

  const markAsRead = async (id) => {
    try {
      await api.notificacoes.marcarLida(id);
      setNotifications((current) => current.map((item) => (item.id === id ? { ...item, lida: true } : item)));
    } catch (error) {
      toast.error(error.message);
    }
  };

  if (!open) return null;

  return (
    <div className="navPanelOverlay" onClick={onClose}>
      <aside className="navPanel" onClick={(event) => event.stopPropagation()}>
        <div className="navPanelHeader">
          <div>
            <strong>Notificações</strong>
            <span>Eventos recentes da sua conta.</span>
          </div>
          <button type="button" className="navPanelClose" onClick={onClose} aria-label="Fechar notificações">
            ×
          </button>
        </div>
        <div className="navPanelBody">
          {loading ? <LoadingState message="Carregando notificações..." /> : null}
          {!loading && notifications.length === 0 ? (
            <EmptyState title="Sem notificações" description="Quando algo importante acontecer, vai aparecer aqui." />
          ) : null}
          {!loading && notifications.length > 0 ? (
            <div className="navNotificationList">
              {notifications.map((item) => (
                <article key={item.id} className={`navNotificationItem${item.lida ? " is-read" : ""}`}>
                  <div className="navNotificationMeta">
                    <strong>{item.titulo}</strong>
                    <span>{dateTimeLabel(item.dataCriacao)}</span>
                  </div>
                  <p>{item.mensagem}</p>
                  {!item.lida ? (
                    <button type="button" className="navInlineAction" onClick={() => markAsRead(item.id)}>
                      Marcar como lida
                    </button>
                  ) : null}
                </article>
              ))}
            </div>
          ) : null}
        </div>
        <div className="navPanelFooter">
          <Link to="/chat" onClick={onClose} className="navInlineLink">
            Abrir conversas
          </Link>
        </div>
      </aside>
    </div>
  );
};

export default NotificationPanel;
