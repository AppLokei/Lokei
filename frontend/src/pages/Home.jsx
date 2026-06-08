import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { api } from "../api/services.js";
import {
  BarraNavegacao,
  CardFerramenta,
  ErrorState,
  LoadingState,
  SiteFooter,
} from "../components/index.js";
import { CATEGORIAS } from "../lib/constants.js";
import { useToast } from "../context/ToastContext.jsx";
import "./Home.css";

const Home = () => {
  const navigate = useNavigate();
  const toast = useToast();
  const [termo, setTermo] = useState("");
  const [loading, setLoading] = useState(true);
  const [anuncios, setAnuncios] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;
    setLoading(true);
    api.anuncios
      .principais(8)
      .then((items) => {
        if (active) setAnuncios(items);
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

  const handleSearch = (event) => {
    event.preventDefault();
    navigate(`/anuncios${termo ? `?q=${encodeURIComponent(termo)}` : ""}`);
  };

  return (
    <div className="homePage">
      <BarraNavegacao />
      <main className="homeContent">
        <header className="homeSearchHeader">
          <div className="stack">
            <span className="anunciarBadge">Aluguel inteligente</span>
            <h1 className="heroTitle">Encontre ferramentas prontas para uso sem comprar no impulso.</h1>
            <p className="heroLead">
              A Lokei conecta locadores e locatários em um catálogo local com disponibilidade real, reputação e acompanhamento da reserva.
            </p>
          </div>
          <form className="homeSearchField" onSubmit={handleSearch}>
            <label className="inputField">
              <span className="inputLabel">O que você precisa hoje?</span>
              <input className="inputControl" value={termo} onChange={(event) => setTermo(event.target.value)} placeholder="Ex: furadeira, escada, lixadeira..." />
            </label>
          </form>
          <div className="homeSearchActions">
            <button type="button" className="homeFilterTrigger" onClick={() => navigate(`/anuncios${termo ? `?q=${encodeURIComponent(termo)}` : ""}`)}>
              Buscar no catálogo
            </button>
          </div>
        </header>

        <section className="homeSection homeCategories">
          <div className="homeSectionHeader">
            <h2 className="homeSectionTitle">Categorias mais buscadas</h2>
          </div>
          <div className="homeCategoryRow">
            {CATEGORIAS.slice(0, 8).map((categoria) => (
              <button key={categoria.value} type="button" className="homeCategoryCard" onClick={() => navigate(`/anuncios?categoria=${categoria.value}`)}>
                <span className="homeCategoryIcon">●</span>
                <span className="homeCategoryLabel">{categoria.label}</span>
              </button>
            ))}
          </div>
        </section>

        <section className="homeSection">
          <div className="homeSectionHeader">
            <h2 className="homeSectionTitle">Principais anúncios</h2>
            <button type="button" className="homeSectionLink" onClick={() => navigate("/anuncios")}>Ver catálogo completo</button>
          </div>
          {loading ? <LoadingState message="Carregando vitrine..." /> : null}
          {!loading && error ? <ErrorState message={error} /> : null}
          {!loading && !error ? (
            <div className="homeGrid">
              {anuncios.map((anuncio) => (
                <CardFerramenta key={anuncio.id} anuncio={anuncio} />
              ))}
            </div>
          ) : null}
        </section>
      </main>
      <SiteFooter />
    </div>
  );
};

export default Home;
