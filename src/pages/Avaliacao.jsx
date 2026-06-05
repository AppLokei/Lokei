import { useState } from "react";
import { useNavigate } from "react-router-dom";

import Botao from "../components/Button";
import "./Avaliacao.css";

const aluguelMock = {
  titulo: "Furadeira de Impacto",
  locador: "Marina Souza",
  periodo: "12 a 15 de Abril",
  imagem:
    "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=800&q=80",
};

const StarIcon = ({ ativa }) => (
  <svg
    className={`starIcon ${ativa ? "starIcon--active" : ""}`}
    viewBox="0 0 24 24"
    aria-hidden="true"
  >
    <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2" />
  </svg>
);

const StarRating = ({ valor, aoMudar, hoverValor, aoMudarHover }) => (
  <div className="avaliacaoStars" role="radiogroup" aria-label="Nota de 1 a 5">
    {[1, 2, 3, 4, 5].map((estrela) => (
      <button
        key={estrela}
        type="button"
        className="avaliacaoStar"
        onClick={() => aoMudar(estrela)}
        onMouseEnter={() => aoMudarHover(estrela)}
        onMouseLeave={() => aoMudarHover(0)}
        aria-label={`${estrela} estrela${estrela > 1 ? "s" : ""}`}
      >
        <StarIcon ativa={estrela <= (hoverValor || valor)} />
      </button>
    ))}
  </div>
);

const Avaliacao = () => {
  const [nota, setNota] = useState(0);
  const [hoverNota, setHoverNota] = useState(0);
  const [comentario, setComentario] = useState("");
  const [erros, setErros] = useState({});
  const [enviado, setEnviado] = useState(false);
  const navigate = useNavigate();

  const validar = () => {
    const proximosErros = {};
    if (nota === 0) proximosErros.nota = "Selecione uma nota de 1 a 5.";
    if (!comentario.trim()) proximosErros.comentario = "Escreva um comentário.";
    return proximosErros;
  };

  const handleSubmit = (evento) => {
    evento.preventDefault();
    const proximosErros = validar();
    setErros(proximosErros);
    if (Object.keys(proximosErros).length > 0) return;

    console.log({ nota, comentario });
    setEnviado(true);
  };

  const handleBackdropClick = (evento) => {
    if (evento.target.classList.contains("avaliacaoPage")) {
      navigate("/meus-alugueis");
    }
  };

  return (
    <div className="avaliacaoPage" onClick={handleBackdropClick}>
      <div className="avaliacaoDrawer" role="dialog" aria-modal="true" aria-labelledby="avaliacaoTitle">
        {enviado ? (
          <div className="avaliacaoSuccess">
            <div className="avaliacaoSuccessIcon">★</div>
            <h1 id="avaliacaoTitle">Avaliação enviada!</h1>
            <p>Obrigado pelo seu feedback.</p>
            <Botao type="button" onClick={() => navigate("/meus-alugueis")}>
              Voltar para Meus Alugueis
            </Botao>
          </div>
        ) : (
          <>
            <header className="avaliacaoHeader">
              <span className="avaliacaoBadge">Avaliação</span>
              <h1 id="avaliacaoTitle">Como foi sua experiência?</h1>
              <p>Avalie a ferramenta e o locador.</p>
            </header>

            {/* Resumo minimalista de leitura */}
            <div className="avaliacaoResumo">
              <img
                src={aluguelMock.imagem}
                alt={aluguelMock.titulo}
                className="avaliacaoResumoImage"
              />
              <div className="avaliacaoResumoInfo">
                <strong>{aluguelMock.titulo}</strong>
                <span>Locador: {aluguelMock.locador}</span>
                <span>Período: {aluguelMock.periodo}</span>
              </div>
            </div>

            <form className="avaliacaoForm" onSubmit={handleSubmit}>
              {/* Estrelas */}
              <div className="avaliacaoField">
                <label className="avaliacaoLabel">Sua nota</label>
                <StarRating
                  valor={nota}
                  aoMudar={setNota}
                  hoverValor={hoverNota}
                  aoMudarHover={setHoverNota}
                />
                {(hoverNota || nota) > 0 ? (
                  <span className="avaliacaoNotaText">
                    {(hoverNota || nota) === 1 && "Péssimo"}
                    {(hoverNota || nota) === 2 && "Ruim"}
                    {(hoverNota || nota) === 3 && "Regular"}
                    {(hoverNota || nota) === 4 && "Bom"}
                    {(hoverNota || nota) === 5 && "Excelente"}
                  </span>
                ) : null}
                {erros.nota ? (
                  <span className="avaliacaoError">{erros.nota}</span>
                ) : null}
              </div>

              {/* Comentário */}
              <div className="avaliacaoField">
                <label className="avaliacaoLabel" htmlFor="avaliacaoComentario">
                  Comentário
                </label>
                <textarea
                  id="avaliacaoComentario"
                  className={`avaliacaoTextarea${erros.comentario ? " avaliacaoTextarea--error" : ""}`}
                  placeholder="Conte como foi alugar esta ferramenta..."
                  value={comentario}
                  onChange={(evento) => setComentario(evento.target.value)}
                  rows={4}
                />
                {erros.comentario ? (
                  <span className="avaliacaoError">{erros.comentario}</span>
                ) : null}
              </div>

              <Botao type="submit">Enviar Avaliação</Botao>
              <Botao
                type="button"
                variante="secondary"
                onClick={() => navigate("/meus-alugueis")}
              >
                Cancelar
              </Botao>
            </form>
          </>
        )}
      </div>
    </div>
  );
};

export default Avaliacao;
