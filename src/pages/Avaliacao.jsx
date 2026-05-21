import { useState } from "react";
import { useNavigate } from "react-router-dom";

import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./Avaliacao.css";

const aluguelMock = {
  titulo: "Furadeira de Impacto",
  locador: "Marina Souza",
  periodo: "12 a 15 de Abril",
  imagem:
    "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=800&q=80",
};

const StarRating = ({ valor, aoMudar }) => (
  <div className="avaliacaoStars" role="radiogroup" aria-label="Nota de 1 a 5">
    {[1, 2, 3, 4, 5].map((estrela) => (
      <button
        key={estrela}
        type="button"
        className={`avaliacaoStar${estrela <= valor ? " avaliacaoStar--active" : ""}`}
        onClick={() => aoMudar(estrela)}
        aria-label={`${estrela} estrela${estrela > 1 ? "s" : ""}`}
      >
        ★
      </button>
    ))}
  </div>
);

const Avaliacao = () => {
  const [nota, setNota] = useState(0);
  const [comentario, setComentario] = useState("");
  const [erros, setErros] = useState({});
  const [enviado, setEnviado] = useState(false);
  const navigate = useNavigate();

  const validar = () => {
    const proximosErros = {};
    if (nota === 0) proximosErros.nota = "Selecione uma nota de 1 a 5.";
    if (!comentario.trim()) proximosErros.comentario = "Escreva um comentario.";
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

  if (enviado) {
    return (
      <div className="avaliacaoPage">
        <BarraNavegacao />
        <div className="avaliacaoCard avaliacaoSuccess">
          <div className="avaliacaoSuccessIcon">★</div>
          <h1>Avaliacao enviada!</h1>
          <p>Obrigado pelo seu feedback.</p>
          <Botao type="button" onClick={() => navigate("/meus-alugueis")}>
            Voltar para Meus Alugueis
          </Botao>
        </div>
      </div>
    );
  }

  return (
    <div className="avaliacaoPage">
      <BarraNavegacao />
      <div className="avaliacaoCard">
        <header className="avaliacaoHeader">
          <span className="avaliacaoBadge">Avaliacao</span>
          <h1>Como foi sua experiencia?</h1>
          <p>Avalie a ferramenta e o locador.</p>
        </header>

        {/* Resumo do aluguel */}
        <div className="avaliacaoResumo">
          <img
            src={aluguelMock.imagem}
            alt={aluguelMock.titulo}
            className="avaliacaoResumoImage"
          />
          <div className="avaliacaoResumoInfo">
            <strong>{aluguelMock.titulo}</strong>
            <span>Locador: {aluguelMock.locador}</span>
            <span>{aluguelMock.periodo}</span>
          </div>
        </div>

        <form className="avaliacaoForm" onSubmit={handleSubmit}>
          {/* Estrelas */}
          <div className="avaliacaoField">
            <label className="avaliacaoLabel">Sua nota</label>
            <StarRating valor={nota} aoMudar={setNota} />
            {nota > 0 ? (
              <span className="avaliacaoNotaText">
                {nota === 1 && "Pessimo"}
                {nota === 2 && "Ruim"}
                {nota === 3 && "Regular"}
                {nota === 4 && "Bom"}
                {nota === 5 && "Excelente"}
              </span>
            ) : null}
            {erros.nota ? (
              <span className="avaliacaoError">{erros.nota}</span>
            ) : null}
          </div>

          {/* Comentário */}
          <div className="avaliacaoField">
            <label className="avaliacaoLabel" htmlFor="avaliacaoComentario">
              Comentario
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

          <Botao type="submit">Enviar Avaliacao</Botao>
          <Botao
            type="button"
            variante="secondary"
            onClick={() => navigate("/meus-alugueis")}
          >
            Cancelar
          </Botao>
        </form>
      </div>
    </div>
  );
};

export default Avaliacao;
