import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./AnuncioDetalhe.css";

const anuncioMock = {
  titulo: "Furadeira de Impacto Bosch",
  localizacao: "Sao Paulo, SP",
  diaria: 45,
  semanal: 250,
  mensal: 720,
  descricao:
    "Furadeira de impacto com alta potencia, ideal para obras e reparos. Inclui maleta, brocas e empunhadura lateral.",
  locador: {
    nome: "Marina Souza",
    nota: 4.8,
    totalAvaliacoes: 2,
  },
  avaliacoes: [
    {
      id: 1,
      nome: "Carlos",
      nota: 5,
      texto: "Equipamento impecavel e entrega rapida.",
    },
    {
      id: 2,
      nome: "Renata",
      nota: 4,
      texto: "Ferramenta potente, recomendo.",
    },
  ],
  imagem:
    "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=1200&q=80",
};

const paraData = (valor) => {
  if (!valor) return null;
  const [ano, mes, dia] = valor.split("-").map(Number);
  return new Date(ano, mes - 1, dia);
};

const diferencaEmDias = (dataInicio, dataFim) => {
  const diff = dataFim - dataInicio;
  return Math.ceil(diff / (1000 * 60 * 60 * 24)) + 1;
};

const formatarMoeda = (valor) =>
  `R$ ${valor.toFixed(2)}`.replace(".", ",");

const AnuncioDetalhe = () => {
  const [souLocador] = useState(false);
  const [dataInicio, setDataInicio] = useState("");
  const [dataFim, setDataFim] = useState("");
  const [erros, setErros] = useState({});
  const navigate = useNavigate();

  const valorHoje = useMemo(() => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0");
    const day = String(today.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }, []);

  const resumoSelecao = useMemo(() => {
    const inicio = paraData(dataInicio);
    const fim = paraData(dataFim);
    if (!inicio || !fim) return null;
    const dias = diferencaEmDias(inicio, fim);
    if (Number.isNaN(dias) || dias <= 0) return null;
    const total = dias * anuncioMock.diaria;
    return `${dias} dias x ${formatarMoeda(anuncioMock.diaria)} = ${formatarMoeda(total)}`;
  }, [dataInicio, dataFim]);

  const validar = () => {
    const proximosErros = {};
    const inicio = paraData(dataInicio);
    const fim = paraData(dataFim);
    const hoje = paraData(valorHoje);

    if (!dataInicio) {
      proximosErros.dataInicio = "Selecione a data de inicio.";
    }

    if (!dataFim) {
      proximosErros.dataFim = "Selecione a data de fim.";
    }

    if (inicio && hoje && inicio < hoje) {
      proximosErros.dataInicio = "A data nao pode ser retroativa.";
    }

    if (inicio && fim && fim < inicio) {
      proximosErros.dataFim = "A data de fim nao pode ser menor.";
    }

    if (inicio && fim) {
      const dias = diferencaEmDias(inicio, fim);
      if (dias > 30) {
        proximosErros.dataFim = "O limite maximo e de 30 dias.";
      }
    }

    return proximosErros;
  };

  const handleSubmit = () => {
    if (souLocador) return;
    const proximosErros = validar();
    setErros(proximosErros);
    if (Object.keys(proximosErros).length > 0) return;

    console.log({
      anuncioId: 1,
      dataInicio,
      dataFim,
    });
  };

  return (
    <div className="detailPage">
      <BarraNavegacao />
      <div className="detailContainer">
        <div className="detailContent">
          <div className="detailMedia">
            <img
              src={anuncioMock.imagem}
              alt={anuncioMock.titulo}
              className="detailImage"
            />
          </div>

          <div className="detailHeader">
            <h1 className="detailTitle">{anuncioMock.titulo}</h1>
            <p className="detailLocation">{anuncioMock.localizacao}</p>
          </div>

          <div className="detailSection">
            <h2>Descricao</h2>
            <p>{anuncioMock.descricao}</p>
          </div>

          <div className="detailSection detailOwner">
            <div>
              <h2>Locador</h2>
              <p>{anuncioMock.locador.nome}</p>
            </div>
            <div className="detailOwnerRating">
              <span>{anuncioMock.locador.nota} ★</span>
              <span>{anuncioMock.locador.totalAvaliacoes} avaliacoes</span>
            </div>
          </div>

          <div className="detailSection">
            <h2>Avaliacoes</h2>
            <div className="detailReviews">
              {anuncioMock.avaliacoes.map((review) => (
                <article key={review.id} className="detailReview">
                  <div className="detailReviewHeader">
                    <strong>{review.nome}</strong>
                    <span>{review.nota} ★</span>
                  </div>
                  <p>{review.texto}</p>
                </article>
              ))}
            </div>
          </div>
        </div>

        <aside className="detailBooking">
          <div className="detailBookingCard">
            <div className="detailPriceHero">
              <span className="detailPriceLabel">Diaria</span>
              <div className="detailPriceValue">
                <span>R$</span>
                <strong>{anuncioMock.diaria}</strong>
                <small>/dia</small>
              </div>
            </div>

            {!souLocador ? (
              <div className="detailSection">
                <h2>Selecione o periodo</h2>
                <p className="detailPolicy">Limite de 30 dias</p>
                <div className="detailDates">
                  <CampoEntrada
                    rotulo="Data de inicio"
                    type="date"
                    value={dataInicio}
                    min={valorHoje}
                    onChange={(evento) => setDataInicio(evento.target.value)}
                    erro={erros.dataInicio}
                  />
                  <CampoEntrada
                    rotulo="Data de fim"
                    type="date"
                    value={dataFim}
                    min={dataInicio || valorHoje}
                    onChange={(evento) => setDataFim(evento.target.value)}
                    erro={erros.dataFim}
                  />
                </div>
                {resumoSelecao ? (
                  <div className="detailSummary">{resumoSelecao}</div>
                ) : null}
                <Botao type="button" onClick={handleSubmit}>
                  Solicitar Aluguel
                </Botao>
                <button
                  className="detailChatLink"
                  type="button"
                  onClick={() => navigate("/chat")}
                >
                  Abrir Chat
                </button>
              </div>
            ) : (
              <div className="detailSection">
                <Botao type="button" onClick={() => navigate("/editar-anuncio")}>
                  Editar anuncio
                </Botao>
              </div>
            )}

            <div className="detailSavings">
              <h3>Economize</h3>
              <div className="detailSavingsRow">
                <span>Semana</span>
                <strong>{formatarMoeda(anuncioMock.semanal)}</strong>
              </div>
              <div className="detailSavingsRow">
                <span>Mes</span>
                <strong>{formatarMoeda(anuncioMock.mensal)}</strong>
              </div>
            </div>
          </div>
        </aside>
      </div>
    </div>
  );
};

export default AnuncioDetalhe;
