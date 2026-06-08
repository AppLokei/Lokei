import { MemoryRouter } from "react-router-dom";
import { render, screen } from "@testing-library/react";

import ToolCard from "../components/ToolCard.jsx";

describe("ToolCard", () => {
  it("renderiza preço formatado e link para detalhe", () => {
    render(
      <MemoryRouter future={{ v7_startTransition: true, v7_relativeSplatPath: true }}>
        <ToolCard
          anuncio={{
            id: 12,
            titulo: "Furadeira de impacto",
            cidade: "Salvador",
            valorDiario: 45.5,
            descricaoCurta: "Potente e com maleta.",
            imagemPrincipalUrl: "/arquivos/furadeira.jpg",
          }}
        />
      </MemoryRouter>
    );

    expect(screen.getByText("Furadeira de impacto")).toBeInTheDocument();
    expect(screen.getByText("Salvador")).toBeInTheDocument();
    expect(screen.getByRole("link", { name: /ver detalhes de furadeira/i })).toHaveAttribute("href", "/anuncios/12");
    expect(screen.getByLabelText(/R\$ 45,50 por dia/i)).toBeInTheDocument();
  });
});
