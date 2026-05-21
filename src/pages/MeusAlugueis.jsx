import { useState } from "react";
import { useNavigate } from "react-router-dom";

import CardAluguel from "../components/RentalCard";
import BarraNavegacao from "../components/NavigationBar";
import "./MeusAlugueis.css";

const itensLocatario = [
  {
    id: 1,
    titulo: "Furadeira de Impacto",
    periodo: "12 a 15 de Abril",
    status: "analise",
    imagem:
      "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 2,
    titulo: "Serra Eletrica",
    periodo: "02 a 10 de Maio",
    status: "andamento",
    imagem:
      "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 3,
    titulo: "Parafusadeira",
    periodo: "15 a 18 de Marco",
    status: "concluido",
    imagem:
      "https://images.pexels.com/photos/3877525/pexels-photo-3877525.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
];

const itensLocador = [
  {
    id: 4,
    titulo: "Lixadeira Orbital",
    periodo: "08 a 12 de Abril",
    status: "analise",
    imagem:
      "https://images.pexels.com/photos/5710754/pexels-photo-5710754.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
  {
    id: 5,
    titulo: "Martelo Demolidor",
    periodo: "01 a 05 de Maio",
    status: "andamento",
    imagem:
      "https://images.unsplash.com/photo-1586864387789-628af9feed72?auto=format&fit=crop&w=800&q=80",
  },
  {
    id: 6,
    titulo: "Lavadora de Pressao",
    periodo: "20 a 25 de Abril",
    status: "pausado",
    imagem:
      "https://images.pexels.com/photos/4876669/pexels-photo-4876669.jpeg?auto=compress&cs=tinysrgb&w=800",
  },
];

const MeusAlugueis = () => {
  const [abaAtiva, setAbaAtiva] = useState("alugando");
  const navigate = useNavigate();

  const handleOwnerAction = (id, acao) => {
    console.log({ id, acao });
  };

  return (
    <div className="rentalsPage">
      <BarraNavegacao />
      <div className="rentalsContainer">
        <header className="rentalsHeader">
          <h1>Meus Alugueis</h1>
          <p>Gerencie seus pedidos e ferramentas emprestadas.</p>
        </header>

        <div className="rentalsTabs">
          <button
            className={`rentalsTab${abaAtiva === "alugando" ? " active" : ""}`}
            type="button"
            onClick={() => setAbaAtiva("alugando")}
          >
            Alugando
          </button>
          <button
            className={`rentalsTab${abaAtiva === "emprestados" ? " active" : ""}`}
            type="button"
            onClick={() => setAbaAtiva("emprestados")}
          >
            Emprestados
          </button>
        </div>

        <div className="rentalsList">
          {abaAtiva === "alugando"
            ? itensLocatario.map((aluguel) => {
                const acao =
                  aluguel.status === "analise"
                    ? {
                      rotulo: "Cancelar Solicitacao",
                      variante: "outlineRed",
                      aoClicar: () =>
                          console.log({ id: aluguel.id, acao: "cancelar" }),
                    }
                    : aluguel.status === "andamento"
                      ? {
                          rotulo: "Abrir Chat",
                          variante: "outlineYellow",
                          aoClicar: () => navigate("/chat"),
                        }
                      : aluguel.status === "concluido"
                        ? {
                            rotulo: "Avaliar",
                            variante: "primary",
                            aoClicar: () => navigate("/avaliar"),
                          }
                        : null;

                return (
                  <CardAluguel
                    key={aluguel.id}
                    titulo={aluguel.titulo}
                    periodo={aluguel.periodo}
                    status={aluguel.status}
                    imagem={aluguel.imagem}
                    acao={acao}
                  />
                );
              })
            : itensLocador.map((aluguel) => {
                const acoesLocador =
                  aluguel.status === "analise"
                    ? [
                        {
                          rotulo: "Aprovar",
                          variante: "solidGreen",
                          aoClicar: () => handleOwnerAction(aluguel.id, "aprovar"),
                        },
                        {
                          rotulo: "Reprovar",
                          variante: "outlineRed",
                          aoClicar: () => handleOwnerAction(aluguel.id, "reprovar"),
                        },
                      ]
                    : null;

                return (
                  <CardAluguel
                    key={aluguel.id}
                    titulo={aluguel.titulo}
                    periodo={aluguel.periodo}
                    status={aluguel.status}
                    imagem={aluguel.imagem}
                    acoesLocador={acoesLocador}
                  />
                );
              })}
        </div>
      </div>
    </div>
  );
};

export default MeusAlugueis;
