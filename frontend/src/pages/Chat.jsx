import { useEffect, useMemo, useRef, useState } from "react";
import { useSearchParams } from "react-router-dom";

import { api } from "../api/services.js";
import { BarraNavegacao, EmptyState, LoadingState } from "../components/index.js";
import { dateTimeLabel } from "../lib/format.js";
import { resolveApiPath } from "../lib/url.js";
import { useToast } from "../context/ToastContext.jsx";
import { useAuth } from "../context/AuthContext.jsx";
import "./Chat.css";

const Chat = () => {
  const toast = useToast();
  const { user } = useAuth();
  const bottomRef = useRef(null);
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(true);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [draft, setDraft] = useState("");
  const [conversas, setConversas] = useState([]);
  const [conversaAtiva, setConversaAtiva] = useState(null);

  const chatId = searchParams.get("chatId");

  useEffect(() => {
    let active = true;
    api.chats.listar().then((items) => {
      if (!active) return;
      setConversas(items);
      const current = items.find((item) => String(item.id) === chatId) || items[0] || null;
      setConversaAtiva(current);
      setLoading(false);
      if (current && !chatId) {
        setSearchParams({ chatId: current.id });
      }
    }).catch((requestError) => toast.error(requestError.message));
    return () => {
      active = false;
    };
  }, [chatId, setSearchParams, toast]);

  useEffect(() => {
    if (!chatId) return;
    api.chats.detalhar(chatId).then(setConversaAtiva).catch((requestError) => toast.error(requestError.message));
  }, [chatId, toast]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [conversaAtiva?.mensagens?.length]);

  const selectConversation = (id) => {
    setSearchParams({ chatId: id });
    setSidebarOpen(false);
  };

  const activeMessages = useMemo(() => conversaAtiva?.mensagens || [], [conversaAtiva]);

  const sendMessage = async () => {
    if (!draft.trim() || !conversaAtiva) return;
    try {
      const sent = await api.chats.enviarMensagem(conversaAtiva.id, { conteudo: draft.trim() });
      setConversaAtiva((current) => ({ ...current, mensagens: [...(current?.mensagens || []), sent] }));
      setDraft("");
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  if (loading) return <LoadingState message="Carregando conversas..." />;

  return (
    <div className="chatPage">
      <BarraNavegacao />
      <div className="chatLayout">
        <aside className={`chatSidebar${sidebarOpen ? " chatSidebar--open" : ""}`}>
          <div className="chatSidebarHeader"><h2>Conversas</h2></div>
          <div className="chatConversationList">
            {conversas.map((conversa) => (
              <button key={conversa.id} type="button" className={`chatConversationItem${conversaAtiva?.id === conversa.id ? " chatConversationItem--active" : ""}`} onClick={() => selectConversation(conversa.id)}>
                <img className="chatConversationImage" src={resolveApiPath(conversa.imagemPrincipalUrl)} alt={conversa.tituloAnuncio} />
                <div className="chatConversationInfo">
                  <div className="chatConversationTop">
                    <strong className="chatConversationName">{conversa.tituloAnuncio}</strong>
                    <span className="chatConversationTime">#{conversa.aluguelId}</span>
                  </div>
                  <span className="chatConversationTool">{conversa.locador} ↔ {conversa.locatario}</span>
                  <p className="chatConversationPreview">{conversa.mensagens?.at(-1)?.conteudo || "Sem mensagens ainda"}</p>
                </div>
              </button>
            ))}
          </div>
        </aside>

        {sidebarOpen ? <div className="chatSidebarOverlay" onClick={() => setSidebarOpen(false)} /> : null}

        <main className="chatMain">
          {conversaAtiva ? (
            <>
              <header className="chatConvHeader">
                <button type="button" className="chatMenuBtn" onClick={() => setSidebarOpen(true)} aria-label="Abrir conversas">☰</button>
                <div className="chatConvHeaderInfo">
                  <img className="chatConvHeaderImage" src={resolveApiPath(conversaAtiva.imagemPrincipalUrl)} alt={conversaAtiva.tituloAnuncio} />
                  <div>
                    <strong>{conversaAtiva.tituloAnuncio}</strong>
                    <span>{conversaAtiva.locador} · {conversaAtiva.locatario}</span>
                  </div>
                </div>
              </header>

              <section className="chatMessages">
                {activeMessages.map((mensagem) => (
                  <article key={mensagem.id} className={`chatBubble${mensagem.remetenteId === user?.id ? " chatBubble--sent" : " chatBubble--received"}`}>
                    <p>{mensagem.conteudo}</p>
                    <span className="chatBubbleTime">{dateTimeLabel(mensagem.dataCriacao)}</span>
                  </article>
                ))}
                <div ref={bottomRef} />
              </section>

              <footer className="chatInputBar">
                <div className="chatInputBarInner">
                  <input className="chatInput" value={draft} onChange={(event) => setDraft(event.target.value)} onKeyDown={(event) => {
                    if (event.key === "Enter" && !event.shiftKey) {
                      event.preventDefault();
                      sendMessage();
                    }
                  }} placeholder="Escreva sua mensagem..." />
                  <button type="button" className="chatSendBtn" onClick={sendMessage} aria-label="Enviar mensagem">
                    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M5 12h11M13 6l6 6-6 6" /></svg>
                  </button>
                </div>
              </footer>
            </>
          ) : (
            <div className="chatEmpty"><EmptyState title="Nenhum chat disponível" description="O chat é liberado apenas após a reserva ser confirmada." /></div>
          )}
        </main>
      </div>
    </div>
  );
};

export default Chat;
