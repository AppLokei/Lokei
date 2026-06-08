import { useMemo, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";

import { useAuth } from "../context/AuthContext.jsx";
import { useToast } from "../context/ToastContext.jsx";
import NotificationPanel from "./NotificationPanel.jsx";
import "./NavigationBar.css";

const icon = {
  home: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 10.5 12 4l8 6.5v8a1 1 0 0 1-1 1h-5v-6H10v6H5a1 1 0 0 1-1-1z" /></svg>
  ),
  catalog: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 7h16M4 12h16M4 17h16" strokeWidth="2" strokeLinecap="round" /></svg>
  ),
  plus: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 5v14m-7-7h14" strokeWidth="2" strokeLinecap="round" /></svg>
  ),
  rentals: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M7 4h10a2 2 0 0 1 2 2v13a1 1 0 0 1-1.6.8L12 16.2 6.6 19.8A1 1 0 0 1 5 19V6a2 2 0 0 1 2-2z" /></svg>
  ),
  chat: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 6a3 3 0 0 1 3-3h10a3 3 0 0 1 3 3v7a3 3 0 0 1-3 3H9l-5 4v-4a3 3 0 0 1-3-3z" /></svg>
  ),
  profile: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 12a4 4 0 1 0-4-4 4 4 0 0 0 4 4zm0 2c-4.4 0-8 2.2-8 5v1h16v-1c0-2.8-3.6-5-8-5z" /></svg>
  ),
  admin: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M12 2 4 6v6c0 5 3.4 9.7 8 11 4.6-1.3 8-6 8-11V6z" /></svg>
  ),
  bell: (
    <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M15 18H6a1 1 0 0 1-.8-1.6L6 15V10a6 6 0 1 1 12 0v5l.8 1.4A1 1 0 0 1 18 18h-3Zm-3 4a2.5 2.5 0 0 0 2.4-2h-4.8A2.5 2.5 0 0 0 12 22Z" /></svg>
  ),
};

const NavigationBar = () => {
  const { authenticated, user, logout, isAdmin } = useAuth();
  const toast = useToast();
  const navigate = useNavigate();
  const [notificationsOpen, setNotificationsOpen] = useState(false);

  const links = useMemo(() => {
    if (!authenticated) {
      return [
        { to: "/", label: "Início", icon: icon.home },
        { to: "/anuncios", label: "Catálogo", icon: icon.catalog },
        { to: "/login", label: "Entrar", icon: icon.profile },
        { to: "/cadastro", label: "Cadastro", icon: icon.plus, primary: true },
      ];
    }

    const items = [
      { to: "/", label: "Início", icon: icon.home },
      { to: "/anuncios", label: "Catálogo", icon: icon.catalog },
      { to: "/anunciar", label: "Anunciar", icon: icon.plus, primary: true },
      { to: "/meus-alugueis", label: "Reservas", icon: icon.rentals },
      { to: "/chat", label: "Chat", icon: icon.chat },
      { to: "/perfil", label: "Perfil", icon: icon.profile },
    ];

    if (isAdmin) {
      items.splice(5, 0, { to: "/admin/denuncias", label: "Admin", icon: icon.admin });
    }

    return items;
  }, [authenticated, isAdmin]);

  const handleLogout = async () => {
    await logout();
    toast.success("Sessão encerrada.");
    navigate("/");
  };

  return (
    <>
      <nav className="navigationBar">
        <div className="navContainer">
          <NavLink className="navBrand" to="/">
            <img className="navBrandLogo" src="/logo.png" alt="Lokei" />
          </NavLink>

          <div className="navLinks">
            {links.map((link) => (
              <NavLink key={link.to} className={({ isActive }) => `navItem${link.primary ? " navItem--primary" : ""}${isActive ? " active" : ""}`} to={link.to} end={link.to === "/"}>
                <span className="navIcon">{link.icon}</span>
                <span className="navLabel">{link.label}</span>
              </NavLink>
            ))}
          </div>

          {authenticated ? (
            <div className="navActions">
              <button type="button" className="navActionButton" onClick={() => setNotificationsOpen(true)} aria-label="Abrir notificações">
                <span className="navIcon">{icon.bell}</span>
                <span className="navActionText">Alertas</span>
              </button>
              <button type="button" className="navActionButton navActionButton--ghost" onClick={handleLogout}>
                Sair
              </button>
              <div className="navIdentity">
                <strong>{user?.nome}</strong>
                <span>{user?.papel?.toLowerCase().replace("_", " ")}</span>
              </div>
            </div>
          ) : null}
        </div>
      </nav>
      <NotificationPanel open={notificationsOpen} onClose={() => setNotificationsOpen(false)} />
    </>
  );
};

export default NavigationBar;
