import { useState } from "react";
import { Link } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./EditarPerfil.css";

const EditarPerfil = () => {
  const [nome, setNome] = useState("Joao Souza");
  const [email, setEmail] = useState("joao@email.com");
  const [telefone, setTelefone] = useState("(11) 98765-4321");
  const [cpf] = useState("123.456.789-00");
  const [cep, setCep] = useState("");
  const [logradouro, setLogradouro] = useState("");
  const [numero, setNumero] = useState("");
  const [complemento, setComplemento] = useState("");
  const [bairro, setBairro] = useState("");
  const [cidade, setCidade] = useState("");
  const [estado, setEstado] = useState("");
  const [erros, setErros] = useState({});

  const validar = () => {
    const proximosErros = {};

    if (!nome.trim()) proximosErros.nome = "Informe o nome completo.";
    if (!telefone.trim()) proximosErros.telefone = "Informe o telefone.";

    return proximosErros;
  };

  const handleSubmit = (evento) => {
    evento.preventDefault();
    const proximosErros = validar();
    setErros(proximosErros);
    if (Object.keys(proximosErros).length > 0) return;

    if (email !== "joao@email.com") {
      console.log("Enviar link de verificacao para", email);
    }

    console.log({ nome, email, telefone, cpf });
  };

  return (
    <div className="editarPerfilPage">
      <BarraNavegacao />
      <div className="editarPerfilLayout">
        <section className="editarPerfilMain">
          <header className="editarPerfilHeader">
            <span className="editarPerfilBadge">Meus Dados</span>
            <h1>Editar Perfil</h1>
            <p>Atualize suas informacoes com seguranca e clareza.</p>
          </header>
          <form className="editarPerfilForm" onSubmit={handleSubmit}>
              <div className="editarPerfilSection">
                <div className="editarPerfilSectionHeader">
                  <p className="editarPerfilSectionTitle">1. Dados Pessoais</p>
                  <span className="editarPerfilSectionHint">
                    Informacoes para identificar sua conta.
                  </span>
                </div>
                <div className="editarPerfilFieldGroup">
                  <CampoEntrada
                    rotulo="Nome Completo"
                    value={nome}
                    onChange={(evento) => setNome(evento.target.value)}
                    erro={erros.nome}
                  />
                  <CampoEntrada
                    rotulo="E-mail"
                    type="email"
                    value={email}
                    onChange={(evento) => setEmail(evento.target.value)}
                  />
                  <CampoEntrada
                    rotulo="Telefone"
                    value={telefone}
                    onChange={(evento) => setTelefone(evento.target.value)}
                    erro={erros.telefone}
                  />
                  <div className="editarPerfilCpf">
                    <div className="editarPerfilCpfHeader">
                      <span className="editarPerfilCpfLabel">CPF</span>
                      <span className="editarPerfilCpfLock" aria-hidden="true">
                        <svg viewBox="0 0 24 24">
                          <path d="M7 11V8a5 5 0 0 1 10 0v3" fill="none" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" />
                          <rect x="5" y="11" width="14" height="9" rx="3" fill="none" stroke="currentColor" strokeWidth="1.6" />
                        </svg>
                      </span>
                    </div>
                    <div className="editarPerfilCpfValue">{cpf}</div>
                    <p className="editarPerfilCpfHint">
                      CPF bloqueado por seguranca e regra do cadastro.
                    </p>
                  </div>
                </div>
              </div>

              <div className="editarPerfilSection">
                <div className="editarPerfilSectionHeader">
                  <p className="editarPerfilSectionTitle">2. Endereco</p>
                  <span className="editarPerfilSectionHint">
                    Use este endereco para retiradas e entregas.
                  </span>
                </div>
                <div className="editarPerfilFieldGroup">
                  <CampoEntrada
                    rotulo="CEP"
                    value={cep}
                    onChange={(evento) => setCep(evento.target.value)}
                    placeholder="00000-000"
                  />
                  <CampoEntrada
                    rotulo="Logradouro"
                    value={logradouro}
                    onChange={(evento) => setLogradouro(evento.target.value)}
                    placeholder="Rua, avenida"
                  />
                  <div className="editarPerfilFieldRow">
                    <CampoEntrada
                      rotulo="Numero"
                      value={numero}
                      onChange={(evento) => setNumero(evento.target.value)}
                      placeholder="Numero"
                    />
                    <CampoEntrada
                      rotulo="Complemento"
                      value={complemento}
                      onChange={(evento) => setComplemento(evento.target.value)}
                      placeholder="Apartamento, bloco"
                    />
                  </div>
                  <CampoEntrada
                    rotulo="Bairro"
                    value={bairro}
                    onChange={(evento) => setBairro(evento.target.value)}
                    placeholder="Seu bairro"
                  />
                  <div className="editarPerfilFieldRow">
                    <CampoEntrada
                      rotulo="Cidade"
                      value={cidade}
                      onChange={(evento) => setCidade(evento.target.value)}
                      placeholder="Cidade"
                    />
                    <CampoEntrada
                      rotulo="Estado"
                      value={estado}
                      onChange={(evento) => setEstado(evento.target.value)}
                      placeholder="UF"
                    />
                  </div>
                </div>
              </div>

            <div className="editarPerfilActions">
              <Link className="editarPerfilCancel" to="/perfil">
                Cancelar e voltar
              </Link>
              <Botao type="submit">Salvar Alteracoes</Botao>
            </div>
          </form>
        </section>
      </div>
    </div>
  );
};

export default EditarPerfil;
