import { MemoryRouter, Route, Routes } from "react-router-dom";
import { render, screen } from "@testing-library/react";

import ProtectedRoute from "../components/ProtectedRoute.jsx";
import { AuthProvider } from "../context/AuthContext.jsx";
import { TOKEN_STORAGE_KEY, USER_STORAGE_KEY } from "../lib/constants.js";

describe("ProtectedRoute", () => {
  beforeEach(() => {
    sessionStorage.clear();
  });

  it("redireciona visitante para login", () => {
    render(
      <MemoryRouter initialEntries={["/protegida"]} future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<div>Login page</div>} />
            <Route element={<ProtectedRoute />}>
              <Route path="/protegida" element={<div>Área protegida</div>} />
            </Route>
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByText("Login page")).toBeInTheDocument();
  });

  it("permite acesso quando há sessão autenticada", () => {
    sessionStorage.setItem(TOKEN_STORAGE_KEY, "token-valido");
    sessionStorage.setItem(USER_STORAGE_KEY, JSON.stringify({ id: 9, nome: "Admin", papel: "ADMIN" }));

    render(
      <MemoryRouter initialEntries={["/protegida"]} future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
        <AuthProvider>
          <Routes>
            <Route path="/login" element={<div>Login page</div>} />
            <Route element={<ProtectedRoute />}>
              <Route path="/protegida" element={<div>Área protegida</div>} />
            </Route>
          </Routes>
        </AuthProvider>
      </MemoryRouter>
    );

    expect(screen.getByText("Área protegida")).toBeInTheDocument();
  });
});
