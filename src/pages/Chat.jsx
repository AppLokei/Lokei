import { useState, useRef, useEffect } from "react";

import BarraNavegacao from "../components/NavigationBar";
import ModalDenuncia from "../components/DenunciaModal";
import "./Chat.css";

/* ── Dados mockados ── */
const conversas = [
  {
    id: 1,
    nome: "Marina Souza",
    ferramenta: "Furadeira de Impacto",
    imagemFerramenta:
      "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=200&q=60",
    ultimaMensagem: "Pode retirar amanha as 10h!",
    hora: "14:32",
    naoLidas: 2,
  },
  {
    id: 2,
    nome: "Carlos Oliveira",
    ferramenta: "Serra Eletrica",
    imagemFerramenta:
      "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?auto=format&fit=crop&w=200&q=60",
    ultimaMensagem: "Obrigado, ferramenta devolvida!",
    hora: "Ontem",
    naoLidas: 0,
  },
  {
    id: 3,
    nome: "Renata Lima",
    ferramenta: "Parafusadeira",
    imagemFerramenta:
      "https://images.pexels.com/photos/3877525/pexels-photo-3877525.jpeg?auto=compress&cs=tinysrgb&w=200",
    ultimaMensagem: "Ainda esta disponivel?",
    hora: "Seg",
    naoLidas: 0,
  },
];

const mensagensMock = {
  1: [
    {
      id: 1,
      de: "eles",
      texto: "Ola! Vi que voce quer alugar a furadeira.",
      hora: "14:20",
    },
    {
      id: 2,
      de: "eu",
      texto: "Isso! Preciso para o fim de semana.",
      hora: "14:22",
    },
    {
      id: 3,
      de: "eles",
      texto: "Perfeito, esta disponivel. Diaria de R$ 45,00.",
      hora: "14:25",
    },
    {
      id: 4,
      de: "eu",
      texto: "Otimo! Posso retirar amanha?",
      hora: "14:28",
    },
    {
      id: 5,
      de: "eles",
      texto: "Pode retirar amanha as 10h!",
      hora: "14:32",
    },
  ],
  2: [
    {
      id: 1,
      de: "eu",
      texto: "Estou devolvendo a serra hoje.",
      hora: "09:10",
    },
    {
      id: 2,
      de: "eles",
      texto: "Ok, pode deixar no portao.",
      hora: "09:15",
    },
    {
      id: 3,
      de: "eu",
      texto: "Pronto, deixei la. Obrigado!",
      hora: "10:02",
    },
    {
      id: 4,
      de: "eles",
      texto: "Obrigado, ferramenta devolvida!",
      hora: "10:30",
    },
  ],
  3: [
    {
      id: 1,
      de: "eles",
      texto: "Oi! Vi o anuncio da parafusadeira.",
      hora: "11:00",
    },
    {
      id: 2,
      de: "eles",
      texto: "Ainda esta disponivel?",
      hora: "11:00",
    },
  ],
};

const Chat = () => {
  const [conversaAtivaId, setConversaAtivaId] = useState(1);
  const [sidebarAberta, setSidebarAberta] = useState(false);
  const [mensagemRascunho, setMensagemRascunho] = useState("");
  const [mensagensPorConversa, setMensagensPorConversa] = useState(mensagensMock);
  const [denunciaAberta, setDenunciaAberta] = useState(false);
  const mensagensFimRef = useRef(null);

  const conversaAtiva = conversas.find((c) => c.id === conversaAtivaId);
  const mensagensAtivas = mensagensPorConversa[conversaAtivaId] || [];

  useEffect(() => {
    mensagensFimRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [mensagensAtivas.length]);

  const handleSend = () => {
    const texto = mensagemRascunho.trim();
    if (!texto) return;

    const novaMensagem = {
      id: Date.now(),
      de: "eu",
      texto,
      hora: new Date().toLocaleTimeString("pt-BR", {
        hour: "2-digit",
        minute: "2-digit",
      }),
    };

    setMensagensPorConversa((prev) => ({
      ...prev,
      [conversaAtivaId]: [...(prev[conversaAtivaId] || []), novaMensagem],
    }));
    setMensagemRascunho("");
  };

  const handleKeyDown = (evento) => {
    if (evento.key === "Enter" && !evento.shiftKey) {
      evento.preventDefault();
      handleSend();
    }
  };

  const selecionarConversa = (id) => {
    setConversaAtivaId(id);
    setSidebarAberta(false);
  };

  return (
    <div className="chatPage">
      <BarraNavegacao />

      <div className="chatLayout">
        {/* ── Sidebar: lista de conversas ── */}
        <aside className={`chatSidebar${sidebarAberta ? " chatSidebar--open" : ""}`}>
          <div className="chatSidebarHeader">
            <h2>Conversas</h2>
          </div>

          <div className="chatConversationList">
            {conversas.map((conv) => (
              <button
                key={conv.id}
                type="button"
                className={`chatConversationItem${conv.id === conversaAtivaId ? " chatConversationItem--active" : ""}`}
                onClick={() => selecionarConversa(conv.id)}
              >
                <img
                  src={conv.imagemFerramenta}
                  alt={conv.ferramenta}
                  className="chatConversationImage"
                />
                <div className="chatConversationInfo">
                  <div className="chatConversationTop">
                    <strong className="chatConversationName">{conv.nome}</strong>
                    <span className="chatConversationTime">{conv.hora}</span>
                  </div>
                  <span className="chatConversationTool">{conv.ferramenta}</span>
                  <p className="chatConversationPreview">{conv.ultimaMensagem}</p>
                </div>
                {conv.naoLidas > 0 ? (
                  <span className="chatConversationBadge">{conv.naoLidas}</span>
                ) : null}
              </button>
            ))}
          </div>
        </aside>

        {/* Overlay mobile para fechar sidebar */}
        {sidebarAberta ? (
          <div
            className="chatSidebarOverlay"
            onClick={() => setSidebarAberta(false)}
          />
        ) : null}

        {/* ── Area principal: conversa ativa ── */}
        <main className="chatMain">
          {/* Header da conversa */}
          <header className="chatConvHeader">
            <button
              type="button"
              className="chatMenuBtn"
              onClick={() => setSidebarAberta(true)}
              aria-label="Abrir conversas"
            >
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="M4 6h16M4 12h16M4 18h16" strokeWidth="2" strokeLinecap="round" />
              </svg>
            </button>

            {conversaAtiva ? (
              <div className="chatConvHeaderInfo">
                <img
                  src={conversaAtiva.imagemFerramenta}
                  alt={conversaAtiva.ferramenta}
                  className="chatConvHeaderImage"
                />
                <div>
                  <strong>{conversaAtiva.nome}</strong>
                  <span>{conversaAtiva.ferramenta}</span>
                  <em className="chatConvStatus">Em Andamento</em>
                </div>
              </div>
            ) : null}

            <button
              type="button"
              className="chatReportBtn"
              onClick={() => setDenunciaAberta(true)}
              aria-label="Denunciar"
            >
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z" />
                <line x1="12" y1="9" x2="12" y2="13" />
                <line x1="12" y1="17" x2="12.01" y2="17" />
              </svg>
            </button>
          </header>

          {/* Mensagens */}
          <div className="chatMessages">
            {mensagensAtivas.map((msg) => (
              <div
                key={msg.id}
                className={`chatBubble chatBubble--${msg.de === "eu" ? "sent" : "received"}`}
              >
                <p>{msg.texto}</p>
                <span className="chatBubbleTime">{msg.hora}</span>
              </div>
            ))}
            <div ref={mensagensFimRef} />
          </div>

          {/* Input de mensagem */}
          <footer className="chatInputBar">
            <div className="chatInputBarInner">
              <input
                type="text"
                className="chatInput"
                placeholder="Digite uma mensagem..."
                value={mensagemRascunho}
                onChange={(evento) => setMensagemRascunho(evento.target.value)}
                onKeyDown={handleKeyDown}
              />
              <button
                type="button"
                className="chatSendBtn"
                onClick={handleSend}
                disabled={!mensagemRascunho.trim()}
                aria-label="Enviar mensagem"
              >
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M22 2 11 13M22 2l-7 20-4-9-9-4z" />
                </svg>
              </button>
            </div>
          </footer>
        </main>
      </div>

      <ModalDenuncia
        aberto={denunciaAberta}
        aoFechar={() => setDenunciaAberta(false)}
      />
    </div>
  );
};

export default Chat;
