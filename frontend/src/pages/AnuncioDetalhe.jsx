import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { api } from "../api/services.js";
import {
  AvailabilityCalendar,
  BarraNavegacao,
  Botao,
  EmptyState,
  ErrorState,
  LoadingState,
  ModalDenuncia,
  SiteFooter,
} from "../components/index.js";
import { useAuth } from "../context/AuthContext.jsx";
import { useToast } from "../context/ToastContext.jsx";
import { dayCountInclusive } from "../lib/date.js";
import { categoryLabel, dateLabel, money, statusLabel } from "../lib/format.js";
import { MOTIVOS_DENUNCIA } from "../lib/constants.js";
import { resolveApiPath } from "../lib/url.js";
import "./AnuncioDetalhe.css";

const AnuncioDetalhe = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const toast = useToast();
  const { authenticated } = useAuth();
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [anuncio, setAnuncio] = useState(null);
  const [disponibilidade, setDisponibilidade] = useState(null);
  const [datas, setDatas] = useState({ startDate: "", endDate: "" });
  const [reservando, setReservando] = useState(false);
  const [denunciaAberta, setDenunciaAberta] = useState(false);
  const [denunciando, setDenunciando] = useState(false);
  const [imagemAtiva, setImagemAtiva] = useState(0);

  useEffect(() => {
    let active = true;
    setLoading(true);
    Promise.all([api.anuncios.detalhar(id), api.anuncios.disponibilidade(id)])
      .then(([detail, availability]) => {
        if (!active) return;
        setAnuncio(detail);
        setDisponibilidade(availability);
      })
      .catch((requestError) => {
        if (!active) return;
        setError(requestError.message);
        toast.error(requestError.message);
      })
      .finally(() => {
        if (active) setLoading(false);
      });

    return () => {
      active = false;
    };
  }, [id, toast]);

  const dias = useMemo(() => dayCountInclusive(datas.startDate, datas.endDate), [datas]);
  const valorTotal = useMemo(() => (dias ? Number(anuncio?.valorDiario || 0) * dias : 0), [anuncio?.valorDiario, dias]);

  const acaoPrimaria = async () => {
    if (!anuncio) return;
    if (anuncio.acaoPrimaria === "FAZER_LOGIN") {
      navigate("/login", { state: { from: `/anuncios/${id}` } });
      return;
    }
    if (anuncio.acaoPrimaria === "EDITAR_ANUNCIO") {
      navigate(`/anuncios/${id}/editar`);
      return;
    }
    if (!datas.startDate || !datas.endDate) {
      toast.info("Selecione um período válido antes de reservar.");
      return;
    }

    setReservando(true);
    try {
      await api.anuncios.reservar(id, { dataInicio: datas.startDate, dataFim: datas.endDate });
      toast.success("Solicitação de aluguel criada.");
      navigate("/meus-alugueis");
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setReservando(false);
    }
  };

  const submitDenuncia = async ({ motivo, descricao, imagens }) => {
    setDenunciando(true);
    try {
      const uploaded = imagens.length ? await api.files.upload(imagens) : [];
      await api.anuncios.denunciar(id, {
        motivo,
        descricao,
        imagens: uploaded.map((item) => item.url),
      });
      toast.success("Denúncia registrada.");
      setDenunciaAberta(false);
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setDenunciando(false);
    }
  };

  if (loading) return <LoadingState message="Carregando anúncio..." />;
  if (error || !anuncio || !disponibilidade) return <ErrorState message={error || "Anúncio não encontrado."} />;

  const currentImage = anuncio.imagens?.[imagemAtiva] || anuncio.imagens?.[0];
  const primaryLabel =
    anuncio.acaoPrimaria === "FAZER_LOGIN"
      ? "Entrar para reservar"
      : anuncio.acaoPrimaria === "EDITAR_ANUNCIO"
        ? "Editar anúncio"
        : anuncio.acaoPrimaria === "INDISPONIVEL"
          ? "Indisponível"
          : reservando
            ? "Solicitando..."
            : "Solicitar aluguel";

  return (
    <div className="detailPage">
      <BarraNavegacao />
      <main className="detailContainer">
        <section className="detailMedia stack">
          <img className="detailImage" src={resolveApiPath(currentImage)} alt={anuncio.titulo} />
          {anuncio.imagens?.length > 1 ? (
            <div className="detailThumbGrid">
              {anuncio.imagens.map((imagem, index) => (
                <button key={`${imagem}-${index}`} type="button" className={`button button--secondary${imagemAtiva === index ? " detailThumbActive" : ""}`} onClick={() => setImagemAtiva(index)}>
                  Foto {index + 1}
                </button>
              ))}
            </div>
          ) : null}
        </section>

        <section className="detailContent">
          <div className="detailBookingCard">
            <header className="detailHeader">
              <h1 className="detailTitle">{anuncio.titulo}</h1>
              <p className="detailLocation">{anuncio.proprietario?.cidade || "Cidade não informada"} · {categoryLabel(anuncio.categoria)}</p>
            </header>

            <div className="detailPriceHero">
              <span className="detailPriceLabel">Diária</span>
              <div className="detailPriceValue">
                <span>R$</span>
                <strong>{Number(anuncio.valorDiario || 0).toFixed(2).replace(".", ",")}</strong>
                <small>/dia</small>
              </div>
            </div>

            <section className="detailSection">
              <h2>Status e proprietário</h2>
              <div className="badgeRow">
                <span className="badge badge--info">{statusLabel(anuncio.status)}</span>
                <span className="badge badge--neutral">Ação: {primaryLabel}</span>
              </div>
              <div className="detailOwner">
                <div>
                  <strong>{anuncio.proprietario?.nome}</strong>
                  <p>{anuncio.proprietario?.emailMascarado} · {anuncio.proprietario?.telefoneMascarado}</p>
                </div>
                <div className="detailOwnerRating">
                  <strong>{anuncio.notaMedia?.toFixed?.(2) || anuncio.notaMedia || "0,00"}</strong>
                  <span>{anuncio.totalAvaliacoes} avaliações</span>
                </div>
              </div>
            </section>

            <section className="detailSection">
              <h2>Descrição</h2>
              <p>{anuncio.descricao}</p>
            </section>

            <section className="detailSection">
              <h2>Disponibilidade</h2>
              <p className="detailPolicy">Datas bloqueadas consideram reservas em aprovação, confirmadas ou em andamento.</p>
              <AvailabilityCalendar
                periodos={disponibilidade.periodosReservados}
                startDate={datas.startDate}
                endDate={datas.endDate}
                onChange={setDatas}
                disabled={!anuncio.disponivelParaReserva}
              />
              <div className="detailDates">
                <div className="detailSummary">Início: {datas.startDate ? dateLabel(datas.startDate) : "Selecione"}</div>
                <div className="detailSummary">Fim: {datas.endDate ? dateLabel(datas.endDate) : "Selecione"}</div>
              </div>
              <div className="detailSummary">Total estimado: {dias ? `${dias} dia(s) · ${money(valorTotal)}` : "Selecione um intervalo válido"}</div>
              <Botao type="button" onClick={acaoPrimaria} disabled={anuncio.acaoPrimaria === "INDISPONIVEL" || reservando}>
                {primaryLabel}
              </Botao>
              {authenticated && anuncio.acaoPrimaria !== "EDITAR_ANUNCIO" ? (
                <button type="button" className="detailChatLink" onClick={() => setDenunciaAberta(true)}>
                  Reportar anúncio
                </button>
              ) : null}
            </section>

            <section className="detailSection">
              <h2>Períodos já ocupados</h2>
              {disponibilidade.periodosReservados.length === 0 ? (
                <EmptyState title="Sem bloqueios atuais" description="Não há reservas conflitantes neste momento." />
              ) : (
                disponibilidade.periodosReservados.map((periodo) => (
                  <div key={periodo.aluguelId} className="detailReview">
                    <div className="detailReviewHeader">
                      <strong>{dateLabel(periodo.dataInicio)} até {dateLabel(periodo.dataFim)}</strong>
                      <span>{statusLabel(periodo.status)}</span>
                    </div>
                  </div>
                ))
              )}
            </section>

            <section className="detailSection">
              <h2>Avaliações</h2>
              <div className="detailReviews">
                {anuncio.avaliacoes.length === 0 ? (
                  <EmptyState title="Sem avaliações" description="As avaliações aparecem aqui assim que reservas concluídas forem avaliadas." />
                ) : (
                  anuncio.avaliacoes.map((avaliacao) => (
                    <article key={avaliacao.id} className="detailReview">
                      <div className="detailReviewHeader">
                        <strong>{avaliacao.autor}</strong>
                        <span>{avaliacao.nota}/5 · {avaliacao.dataCriacao}</span>
                      </div>
                      <p className="cardTextMuted">{avaliacao.comentario}</p>
                    </article>
                  ))
                )}
              </div>
            </section>
          </div>
        </section>
      </main>
      <SiteFooter />
      <ModalDenuncia aberto={denunciaAberta} aoFechar={() => setDenunciaAberta(false)} aoEnviar={submitDenuncia} motivos={MOTIVOS_DENUNCIA} carregando={denunciando} />
    </div>
  );
};

export default AnuncioDetalhe;
