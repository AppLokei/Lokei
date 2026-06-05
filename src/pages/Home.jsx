import { useState, useRef, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import CardFerramenta from "../components/ToolCard";
import BarraNavegacao from "../components/NavigationBar";
import "./Home.css";

/* ── Dados mockados ── */
const ferramentas = [
  {
    id: 1,
    titulo: "Furadeira de Impacto",
    localizacao: "Sao Paulo, SP",
    preco: "R$ 45,00/dia",
    valorDiario: 45,
    categoria: "eletricas",
    imagem:
      "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 2,
    titulo: "Serra Eletrica",
    localizacao: "Campinas, SP",
    preco: "R$ 60,00/dia",
    valorDiario: 60,
    categoria: "eletricas",
    imagem:
      "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 3,
    titulo: "Parafusadeira",
    localizacao: "Santos, SP",
    preco: "R$ 35,00/dia",
    valorDiario: 35,
    categoria: "eletricas",
    imagem:
      "https://images.pexels.com/photos/3877525/pexels-photo-3877525.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
  {
    id: 4,
    titulo: "Lixadeira Orbital",
    localizacao: "Osasco, SP",
    preco: "R$ 42,00/dia",
    valorDiario: 42,
    categoria: "manuais",
    imagem:
      "https://images.pexels.com/photos/5710754/pexels-photo-5710754.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
  {
    id: 5,
    titulo: "Martelo Demolidor",
    localizacao: "Guarulhos, SP",
    preco: "R$ 90,00/dia",
    valorDiario: 90,
    categoria: "manuais",
    imagem:
      "https://images.unsplash.com/photo-1586864387789-628af9feed72?auto=format&fit=crop&w=800&q=80",
  },
];

const notificacoes = [
  {
    id: 1,
    texto: "Sua reserva da Furadeira foi aprovada!",
    hora: "Agora",
    tipo: "success",
  },
  {
    id: 2,
    texto: "Nova mensagem de Mariana Silva",
    hora: "5 min",
    tipo: "message",
  },
  {
    id: 3,
    texto: "A reserva da Serra Eletrica foi cancelada",
    hora: "1h",
    tipo: "alert",
  },
];

const categorias = [
  { id: "eletricas", nome: "Eletricas", icon: "⚡" },
  { id: "manuais", nome: "Manuais", icon: "🛠️" },
  { id: "jardinagem", nome: "Jardinagem", icon: "🌿" },
  { id: "pintura", nome: "Pintura", icon: "🎨" },
];

const Home = () => {
  const navigate = useNavigate();
  /* ── Notificações ── */
  const [notificacoesAbertas, setNotificacoesAbertas] = useState(false);
  const notificacoesRef = useRef(null);

  /* ── Filtros ── */
  const [filtrosAbertos, setFiltrosAbertos] = useState(false);
  const [categoria, setCategoria] = useState("");
  const [valorMin, setValorMin] = useState("");
  const [valorMax, setValorMax] = useState("");
  const [filtrosAtivos, setFiltrosAtivos] = useState({
    categoria: "",
    valorMin: "",
    valorMax: "",
  });

  /* Fecha dropdown ao clicar fora */
  useEffect(() => {
    const handleClickOutside = (evento) => {
      if (notificacoesRef.current && !notificacoesRef.current.contains(evento.target)) {
        setNotificacoesAbertas(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const handleAplicarFiltros = () => {
    setFiltrosAtivos({ categoria, valorMin, valorMax });
    setFiltrosAbertos(false);
  };

  const handleLimparFiltros = () => {
    setCategoria("");
    setValorMin("");
    setValorMax("");
    setFiltrosAtivos({ categoria: "", valorMin: "", valorMax: "" });
  };

  /* ── Filtragem dos cards ── */
  const itensFiltrados = ferramentas.filter((ferramenta) => {
    if (filtrosAtivos.categoria && ferramenta.categoria !== filtrosAtivos.categoria) {
      return false;
    }
    if (filtrosAtivos.valorMin && ferramenta.valorDiario < Number(filtrosAtivos.valorMin)) {
      return false;
    }
    if (filtrosAtivos.valorMax && ferramenta.valorDiario > Number(filtrosAtivos.valorMax)) {
      return false;
    }
    return true;
  });

  return (
    <div className="homePage">
      <div className="homeContent">
        <header className="homeSearchHeader">
          <div className="homeSearchField">
            <CampoEntrada rotulo="Buscar" placeholder="Buscar ferramentas..." />
          </div>
          <div className="homeSearchActions">
            <div className="homeNotifWrapper" ref={notificacoesRef}>
              <button
                className="homeIconButton"
                type="button"
                aria-label="Notificacoes"
                onClick={() => setNotificacoesAbertas((prev) => !prev)}
              >
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M12 3a5 5 0 0 0-5 5v3.2l-1.5 2.6A1 1 0 0 0 6.4 15h11.2a1 1 0 0 0 .9-1.2L17 11.2V8a5 5 0 0 0-5-5zm0 18a2 2 0 0 0 2-2h-4a2 2 0 0 0 2 2z" />
                </svg>
                <span className="homeNotifBadge">{notificacoes.length}</span>
              </button>

              {notificacoesAbertas ? (
                <div className="homeNotifDropdown">
                  <div className="homeNotifHeader">
                    <strong>Notificacoes</strong>
                    <span>{notificacoes.length} novas</span>
                  </div>
                  <ul className="homeNotifList">
                    {notificacoes.map((notificacao) => (
                      <li key={notificacao.id} className="homeNotifItem">
                        <span className={`homeNotifIcon homeNotifIcon--${notificacao.tipo}`}>
                          {notificacao.tipo === "success" && (
                            <svg viewBox="0 0 24 24"><path d="M20 6 9 17l-5-5" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" /></svg>
                          )}
                          {notificacao.tipo === "message" && (
                            <svg viewBox="0 0 24 24"><path d="M4 6a3 3 0 0 1 3-3h10a3 3 0 0 1 3 3v7a3 3 0 0 1-3 3H9l-5 4v-4a3 3 0 0 1-3-3z" /></svg>
                          )}
                          {notificacao.tipo === "alert" && (
                            <svg viewBox="0 0 24 24"><path d="M12 9v4m0 4h.01M10.3 3.2 1.8 18a2 2 0 0 0 1.7 3h16.9a2 2 0 0 0 1.7-3L13.7 3.2a2 2 0 0 0-3.4 0z" /></svg>
                          )}
                        </span>
                        <div className="homeNotifText">
                          <p>{notificacao.texto}</p>
                          <span>{notificacao.hora}</span>
                        </div>
                      </li>
                    ))}
                  </ul>
                </div>
              ) : null}
            </div>
            <button
              className="homeFilterTrigger"
              type="button"
              onClick={() => setFiltrosAbertos(true)}
            >
              Filtrar
            </button>
          </div>
        </header>

        <section className="homeCategories">
          <div className="homeSectionHeader">
            <h2 className="homeSectionTitle">Categorias</h2>
            <Link className="homeSectionLink" to="/anuncios">
              Ver todos
            </Link>
          </div>
          <div className="homeCategoryRow">
            {categorias.map((item) => (
              <button
                key={item.id}
                className={`homeCategoryCard${categoria === item.id ? " is-active" : ""}`}
                type="button"
                onClick={() => setCategoria(item.id)}
              >
                <span className="homeCategoryIcon" aria-hidden="true">
                  {item.icon}
                </span>
                <span className="homeCategoryLabel">{item.nome}</span>
              </button>
            ))}
          </div>
        </section>

        <section className="homeSection">
          <div className="homeSectionHeader">
            <h2 className="homeSectionTitle">Novidades</h2>
            <Link className="homeSectionLink" to="/anuncios">
              Ver todos
            </Link>
          </div>
          <div className="homeGrid">
            {itensFiltrados.length > 0 ? (
              itensFiltrados.map((ferramenta) => (
                <CardFerramenta
                  key={ferramenta.id}
                  titulo={ferramenta.titulo}
                  local={ferramenta.localizacao}
                  preco={ferramenta.preco}
                  valorDiario={ferramenta.valorDiario}
                  imagem={ferramenta.imagem}
                  hrefCta={`/anuncios/${ferramenta.id}`}
                />
              ))
            ) : (
              <p className="homeEmpty">
                Nenhuma ferramenta encontrada com esses filtros.
              </p>
            )}
          </div>
        </section>

        <section className="homeSection">
          <div className="homeSectionHeader">
            <h2 className="homeSectionTitle">Promocoes</h2>
            <Link className="homeSectionLink" to="/anuncios">
              Ver todos
            </Link>
          </div>
          <div className="homeGrid">
            {itensFiltrados.length > 0 ? (
              itensFiltrados.map((ferramenta) => (
                <CardFerramenta
                  key={`promo-${ferramenta.id}`}
                  titulo={ferramenta.titulo}
                  local={ferramenta.localizacao}
                  preco={ferramenta.preco}
                  valorDiario={ferramenta.valorDiario}
                  imagem={ferramenta.imagem}
                  hrefCta={`/anuncios/${ferramenta.id}`}
                />
              ))
            ) : (
              <p className="homeEmpty">
                Nenhuma ferramenta encontrada com esses filtros.
              </p>
            )}
          </div>
        </section>
      </div>

      {/* ── Modal de Filtros ── */}
      {filtrosAbertos ? (
        <div className="homeFilterOverlay" onClick={() => setFiltrosAbertos(false)}>
          <div
            className="homeFilterModal"
            onClick={(e) => e.stopPropagation()}
            role="dialog"
            aria-modal="true"
            aria-label="Filtros"
          >
            <header className="homeFilterHeader">
              <h2>Filtros</h2>
              <button
                className="homeFilterClose"
                type="button"
                onClick={() => setFiltrosAbertos(false)}
                aria-label="Fechar"
              >
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M18 6 6 18M6 6l12 12" strokeWidth="2" strokeLinecap="round" />
                </svg>
              </button>
            </header>

            <div className="homeFilterBody">
              <label className="homeFilterField">
                <span className="homeFilterLabel">Categoria</span>
                <select
                  className="homeFilterSelect"
                  value={categoria}
                onChange={(evento) => setCategoria(evento.target.value)}
              >
                  <option value="">Todas</option>
                  <option value="eletricas">Ferramentas Eletricas</option>
                  <option value="manuais">Ferramentas Manuais</option>
                  <option value="jardinagem">Jardinagem</option>
                </select>
              </label>

              <div className="homeFilterField">
                <span className="homeFilterLabel">Faixa de Valor Diario (R$)</span>
                <div className="homeFilterRange">
                  <input
                    className="homeFilterInput"
                    type="number"
                    placeholder="Min"
                    min="0"
                    value={valorMin}
                    onChange={(evento) => setValorMin(evento.target.value)}
                  />
                  <span className="homeFilterRangeSep">ate</span>
                  <input
                    className="homeFilterInput"
                    type="number"
                    placeholder="Max"
                    min="0"
                    value={valorMax}
                    onChange={(evento) => setValorMax(evento.target.value)}
                  />
                </div>
              </div>
            </div>

            <footer className="homeFilterFooter">
              <button
                className="homeFilterClear"
                type="button"
                onClick={handleLimparFiltros}
              >
                Limpar Filtros
              </button>
              <Botao type="button" onClick={handleAplicarFiltros}>
                Aplicar Filtros
              </Botao>
            </footer>
          </div>
        </div>
      ) : null}

      <BarraNavegacao />
    </div>
  );
};

export default Home;
