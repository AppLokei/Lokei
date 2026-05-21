import { Link } from "react-router-dom";

import BarraNavegacao from "../components/NavigationBar";
import "./Perfil.css";

const Perfil = () => (
  <div className="perfilPage">
    <BarraNavegacao />
    <div className="perfilContainer">
      <header className="perfilHeader">
        <div className="perfilAvatar">JS</div>
        <h1 className="perfilName">Joao Souza</h1>
        <div className="perfilRating">
          <span>4.9</span>
          <div className="perfilStars">★★★★★</div>
        </div>
        <p className="perfilSince">Na Lokei desde Jan 2024</p>
      </header>

      <div className="perfilActions">
        <Link className="perfilAction" to="/perfil/editar">
          Meus Dados
        </Link>
        <button className="perfilAction" type="button">
          Enderecos
        </button>
        <button className="perfilAction" type="button">
          Formas de Pagamento
        </button>
        <button className="perfilAction" type="button">
          Central de Ajuda
        </button>
        <button className="perfilAction perfilLogout" type="button">
          Sair
        </button>
      </div>
    </div>
  </div>
);

export default Perfil;
