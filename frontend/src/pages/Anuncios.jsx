import { useEffect, useMemo, useState } from "react";
import { useSearchParams } from "react-router-dom";

import { api } from "../api/services.js";
import {
  BarraNavegacao,
  CardFerramenta,
  EmptyState,
  ErrorState,
  LoadingState,
  SiteFooter,
} from "../components/index.js";
import { CATEGORIAS, SORT_OPTIONS } from "../lib/constants.js";
import { useToast } from "../context/ToastContext.jsx";
import "./Anuncios.css";

const Anuncios = () => {
  const toast = useToast();
  const [searchParams, setSearchParams] = useSearchParams();
  const [loading, setLoading] = useState(true);
  const [resultado, setResultado] = useState({ itens: [], totalPaginas: 0, pagina: 0, totalItens: 0 });
  const [error, setError] = useState("");

  const filtros = useMemo(
    () => ({
      q: searchParams.get("q") || "",
      categoria: searchParams.get("categoria") || "",
      cidade: searchParams.get("cidade") || "",
      precoMin: searchParams.get("precoMin") || "",
      precoMax: searchParams.get("precoMax") || "",
      sort: searchParams.get("sort") || "recente",
      page: Number(searchParams.get("page") || 0),
    }),
    [searchParams]
  );

  useEffect(() => {
    let active = true;
    setLoading(true);
    setError("");
    api.anuncios
      .listar({ ...filtros, size: 12 })
      .then((data) => {
        if (active) setResultado(data);
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
  }, [filtros, toast]);

  const updateFilters = (partial) => {
    const next = new URLSearchParams(searchParams);
    Object.entries(partial).forEach(([key, value]) => {
      if (value === "" || value === null || value === undefined) {
        next.delete(key);
      } else {
        next.set(key, String(value));
      }
    });
    if (partial.page === undefined) next.set("page", "0");
    setSearchParams(next);
  };

  return (
    <div className="anunciosPage">
      <BarraNavegacao />
      <main className="anunciosContainer">
        <header className="anunciosHeader">
          <h1>Catálogo de ferramentas</h1>
          <p>Busque por categoria, cidade e faixa de preço. O catálogo reflete apenas anúncios ativos.</p>
        </header>

        <section className="formCard stack">
          <div className="formRow">
            <label className="fieldLabel">
              Busca
              <input value={filtros.q} onChange={(event) => updateFilters({ q: event.target.value })} placeholder="Ex: esmerilhadeira" />
            </label>
            <label className="fieldLabel">
              Cidade
              <input value={filtros.cidade} onChange={(event) => updateFilters({ cidade: event.target.value })} placeholder="Ex: Salvador" />
            </label>
          </div>
          <div className="formRow">
            <label className="fieldLabel">
              Categoria
              <select value={filtros.categoria} onChange={(event) => updateFilters({ categoria: event.target.value })}>
                <option value="">Todas</option>
                {CATEGORIAS.map((categoria) => (
                  <option key={categoria.value} value={categoria.value}>{categoria.label}</option>
                ))}
              </select>
            </label>
            <label className="fieldLabel">
              Ordenação
              <select value={filtros.sort} onChange={(event) => updateFilters({ sort: event.target.value })}>
                {SORT_OPTIONS.map((option) => (
                  <option key={option.value} value={option.value}>{option.label}</option>
                ))}
              </select>
            </label>
          </div>
          <div className="formRow">
            <label className="fieldLabel">
              Preço mínimo
              <input value={filtros.precoMin} onChange={(event) => updateFilters({ precoMin: event.target.value })} placeholder="0,00" />
            </label>
            <label className="fieldLabel">
              Preço máximo
              <input value={filtros.precoMax} onChange={(event) => updateFilters({ precoMax: event.target.value })} placeholder="200,00" />
            </label>
          </div>
        </section>

        {loading ? <LoadingState message="Carregando anúncios..." /> : null}
        {!loading && error ? <ErrorState message={error} /> : null}
        {!loading && !error && resultado.itens.length === 0 ? (
          <EmptyState title="Nenhum anúncio encontrado" description="Ajuste os filtros ou amplie a busca para ver mais resultados." />
        ) : null}
        {!loading && !error && resultado.itens.length > 0 ? (
          <>
            <div className="anunciosGrid">
              {resultado.itens.map((anuncio) => (
                <CardFerramenta key={anuncio.id} anuncio={anuncio} />
              ))}
            </div>
            <div className="inlineActions">
              <button type="button" className="button button--secondary" disabled={resultado.pagina <= 0} onClick={() => updateFilters({ page: Math.max(resultado.pagina - 1, 0) })}>
                Página anterior
              </button>
              <span className="pageNotice">Página {resultado.pagina + 1} de {Math.max(resultado.totalPaginas, 1)} · {resultado.totalItens} anúncios</span>
              <button type="button" className="button button--secondary" disabled={resultado.pagina + 1 >= resultado.totalPaginas} onClick={() => updateFilters({ page: resultado.pagina + 1 })}>
                Próxima página
              </button>
            </div>
          </>
        ) : null}
      </main>
      <SiteFooter />
    </div>
  );
};

export default Anuncios;
