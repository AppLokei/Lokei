import { useState } from "react";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./EditarPerfil.css";

const EditarPerfil = () => {
  const [nome, setNome] = useState("Joao Souza");
  const [email, setEmail] = useState("joao@email.com");
  const [telefone, setTelefone] = useState("(11) 98765-4321");
  const [cpf] = useState("123.456.789-00");
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
      <div className="editarPerfilCard">
        <header className="editarPerfilHeader">
          <span className="editarPerfilBadge">Meus Dados</span>
          <h1>Editar Perfil</h1>
          <p>Atualize suas informacoes pessoais.</p>
        </header>

        <form className="editarPerfilForm" onSubmit={handleSubmit}>
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
          <CampoEntrada rotulo="CPF" value={cpf} disabled />
          <span className="editarPerfilHint">
            O CPF nao pode ser alterado apos o cadastro.
          </span>

          <Botao type="submit">Salvar Alteracoes</Botao>
        </form>
      </div>
    </div>
  );
};

export default EditarPerfil;
