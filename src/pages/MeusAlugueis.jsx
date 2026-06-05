import { useState } from "react";
import { useNavigate } from "react-router-dom";

import CardAluguel from "../components/RentalCard";
import BarraNavegacao from "../components/NavigationBar";
import ConfirmationModal from "../components/ConfirmationModal";
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
    status: "confirmado",
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
  const [modalConfirmacao, setModalConfirmacao] = useState({
    aberto: false,
    tipo: "", // "cancelar" | "reprovar"
    aluguelId: null,
    tituloAluguel: "",
  });
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
                        setModalConfirmacao({
                          aberto: true,
                          tipo: "cancelar",
                          aluguelId: aluguel.id,
                          tituloAluguel: aluguel.titulo,
                        }),
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
                  aluguel.status === "confirmado"
                    ? [
                        {
                          rotulo: "Confirmar Pagamento e Entrega",
                          variante: "primary",
                          aoClicar: () =>
                            handleOwnerAction(aluguel.id, "confirmar_pagamento_entrega"),
                        },
                      ]
                    : aluguel.status === "analise"
                      ? [
                          {
                            rotulo: "Aprovar",
                            variante: "solidGreen",
                            aoClicar: () => handleOwnerAction(aluguel.id, "aprovar"),
                          },
                          {
                            rotulo: "Reprovar",
                            variante: "outlineRed",
                            aoClicar: () =>
                              setModalConfirmacao({
                                aberto: true,
                                tipo: "reprovar",
                                aluguelId: aluguel.id,
                                tituloAluguel: aluguel.titulo,
                              }),
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

        <ConfirmationModal
          aberto={modalConfirmacao.aberto}
          aoFechar={() =>
            setModalConfirmacao({
              aberto: false,
              tipo: "",
              aluguelId: null,
              tituloAluguel: "",
            })
          }
          aoConfirmar={() => {
            if (modalConfirmacao.tipo === "cancelar") {
              console.log({ id: modalConfirmacao.aluguelId, acao: "cancelar" });
            } else if (modalConfirmacao.tipo === "reprovar") {
              handleOwnerAction(modalConfirmacao.aluguelId, "reprovar");
            }
            setModalConfirmacao({
              aberto: false,
              tipo: "",
              aluguelId: null,
              tituloAluguel: "",
            });
          }}
          titulo={
            modalConfirmacao.tipo === "cancelar"
              ? "Cancelar solicitação?"
              : "Reprovar solicitação?"
          }
          descricao={
            <p>
              Tem certeza que deseja{" "}
              {modalConfirmacao.tipo === "cancelar" ? "cancelar" : "reprovar"} o
              pedido de aluguel para <strong>{modalConfirmacao.tituloAluguel}</strong>?
            </p>
          }
          textoConfirmar={
            modalConfirmacao.tipo === "cancelar"
              ? "Confirmar Cancelamento"
              : "Reprovar Solicitação"
          }
          varianteConfirmar="outlineRed"
        />
      </div>
    </div>
  );
};

export default MeusAlugueis;
