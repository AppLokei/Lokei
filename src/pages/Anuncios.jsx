import { Link } from "react-router-dom";

import CardFerramenta from "../components/ToolCard";
import BarraNavegacao from "../components/NavigationBar";
import "./Anuncios.css";

const anunciosMock = [
  {
    id: 1,
    titulo: "Furadeira de Impacto",
    localizacao: "Sao Paulo, SP",
    preco: "R$ 45,00/dia",
    imagem:
      "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 2,
    titulo: "Serra Eletrica",
    localizacao: "Campinas, SP",
    preco: "R$ 60,00/dia",
    imagem:
      "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 3,
    titulo: "Parafusadeira",
    localizacao: "Santos, SP",
    preco: "R$ 35,00/dia",
    imagem:
      "https://images.pexels.com/photos/3877525/pexels-photo-3877525.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
  {
    id: 4,
    titulo: "Lixadeira Orbital",
    localizacao: "Osasco, SP",
    preco: "R$ 42,00/dia",
    imagem:
      "https://images.pexels.com/photos/5710754/pexels-photo-5710754.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
];

const Anuncios = () => (
  <div className="anunciosPage">
    <BarraNavegacao />
    <div className="anunciosContainer">
      <header className="anunciosHeader">
        <h1>Explorar anuncios</h1>
        <p>Encontre ferramentas disponiveis perto de voce.</p>
        <Link className="anunciosCta" to="/anunciar">
          Publicar anuncio
        </Link>
      </header>

      <div className="anunciosGrid">
        {anunciosMock.map((anuncio) => (
          <CardFerramenta
            key={anuncio.id}
            titulo={anuncio.titulo}
            local={anuncio.localizacao}
            preco={anuncio.preco}
            valorDiario={Number(anuncio.preco.replace("R$", "").split("/")[0].replace(",", "."))}
            imagem={anuncio.imagem}
            hrefCta={`/anuncios/${anuncio.id}`}
          />
        ))}
      </div>
    </div>
  </div>
);

export default Anuncios;
