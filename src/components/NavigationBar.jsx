import { NavLink } from "react-router-dom";

import "./NavigationBar.css";

const BarraNavegacao = () => (
  <nav className="navigationBar">
    <div className="navContainer">
      <NavLink className="navBrand" to="/">
        <img className="navBrandLogo" src="/logo.png" alt="Lokei Logo" />
      </NavLink>

      <div className="navLinks">
        <NavLink className="navItem" to="/" end>
          <span className="navIcon">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M4 10.5 12 4l8 6.5v8a1 1 0 0 1-1 1h-5v-6H10v6H5a1 1 0 0 1-1-1z" />
            </svg>
          </span>
          <span className="navLabel">Inicio</span>
        </NavLink>
        <NavLink className="navItem" to="/meus-alugueis">
          <span className="navIcon">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M7 4h10a2 2 0 0 1 2 2v13a1 1 0 0 1-1.6.8L12 16.2 6.6 19.8A1 1 0 0 1 5 19V6a2 2 0 0 1 2-2z" />
            </svg>
          </span>
          <span className="navLabel">Meus Alugueis</span>
        </NavLink>
        <NavLink className="navItem navItem--primary" to="/anunciar">
          <span className="navIcon">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M12 5v14m-7-7h14" strokeWidth="2" strokeLinecap="round" />
            </svg>
          </span>
          <span className="navLabel">Anunciar</span>
        </NavLink>
        <NavLink className="navItem" to="/chat">
          <span className="navIcon">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M4 6a3 3 0 0 1 3-3h10a3 3 0 0 1 3 3v7a3 3 0 0 1-3 3H9l-5 4v-4a3 3 0 0 1-3-3z" />
            </svg>
          </span>
          <span className="navLabel">Chat</span>
        </NavLink>
        <NavLink className="navItem" to="/perfil">
          <span className="navIcon">
            <svg viewBox="0 0 24 24" aria-hidden="true">
              <path d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4zm0 2c-4.4 0-8 2.2-8 5v1h16v-1c0-2.8-3.6-5-8-5z" />
            </svg>
          </span>
          <span className="navLabel">Perfil</span>
        </NavLink>
      </div>
    </div>
  </nav>
);

export default BarraNavegacao;
