import { Link, useNavigate } from "react-router-dom";

import BarraNavegacao from "../components/NavigationBar";
import "./Perfil.css";

const Perfil = () => {
  const navigate = useNavigate();

  return (
    <div className="perfilPage">
      <BarraNavegacao />
      <div className="perfilShell">
        <header className="perfilHero">
          <div className="perfilIdentity">
            <div className="perfilAvatar">
              <span className="perfilAvatarInitials">JS</span>
            </div>
            <div className="perfilIdentityText">
              <div className="perfilNameRow">
                <h1 className="perfilName">Joao Souza</h1>
              </div>
              <div className="perfilMeta">
                <span className="perfilRating">
                  <strong>4.9</strong>
                  <span className="perfilStars">★★★★★</span>
                </span>
                <span className="perfilSince">Na Lokei desde Jan 2024</span>
              </div>
            </div>
          </div>
        </header>

        <div className="perfilPanel">
          <nav className="perfilNav" aria-label="Menu do perfil">
            <div className="perfilSection">
              <p className="perfilSectionTitle">Conta</p>
              <ul className="perfilList">
                <li>
                  <Link className="perfilItem" to="/perfil/editar">
                    <span className="perfilItemIcon">ID</span>
                    <span className="perfilItemText">
                      <span className="perfilItemLabel">Meus Dados</span>
                      <span className="perfilItemDesc">
                        Atualize nome, telefone, endereços e preferências
                      </span>
                    </span>
                  </Link>
                </li>
              </ul>
            </div>
          </nav>
        </div>

        <footer className="perfilLogoutSection">
          <button
            className="perfilLogout"
            type="button"
            onClick={() => navigate("/login")}
          >
            Sair da conta
          </button>
          <p className="perfilLogoutHint">Encerra sua sessao neste dispositivo.</p>
        </footer>
      </div>
    </div>
  );
};

export default Perfil;
