import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { api } from "../api/services.js";
import { BarraNavegacao, Botao, CampoEntrada, LoadingState } from "../components/index.js";
import { useToast } from "../context/ToastContext.jsx";
import { useAuth } from "../context/AuthContext.jsx";
import "./EditarPerfil.css";

const EditarPerfil = () => {
  const navigate = useNavigate();
  const toast = useToast();
  const { refreshProfile } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [perfil, setPerfil] = useState(null);
  const [tokenEmail, setTokenEmail] = useState("");
  const [novoEmail, setNovoEmail] = useState("");

  useEffect(() => {
    let active = true;
    api.profile.get().then((data) => {
      if (active) {
        setPerfil(data);
        setLoading(false);
      }
    });
    return () => {
      active = false;
    };
  }, []);

  const updateAddress = (field, value) => {
    setPerfil((current) => ({ ...current, endereco: { ...current.endereco, [field]: value } }));
  };

  const saveProfile = async (event) => {
    event.preventDefault();
    setSaving(true);
    try {
      const response = await api.profile.update({
        nome: perfil.nome,
        telefone: perfil.telefone.replace(/\D/g, ""),
        endereco: {
          ...perfil.endereco,
          cep: perfil.endereco.cep.replace(/\D/g, ""),
          estado: perfil.endereco.estado.toUpperCase(),
        },
      });
      setPerfil(response);
      await refreshProfile();
      toast.success("Perfil atualizado.");
      navigate("/perfil");
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  const requestEmailChange = async () => {
    try {
      await api.profile.requestEmailChange({ novoEmail });
      toast.success("Solicitação de alteração de e-mail registrada.");
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  const confirmEmailChange = async () => {
    try {
      await api.profile.confirmEmailChange({ token: tokenEmail });
      toast.success("E-mail alterado com sucesso.");
      await refreshProfile();
    } catch (requestError) {
      toast.error(requestError.message);
    }
  };

  if (loading || !perfil) {
    return <LoadingState message="Carregando edição de perfil..." />;
  }

  return (
    <div className="editarPerfilPage">
      <BarraNavegacao />
      <div className="editarPerfilLayout">
        <main className="editarPerfilMain">
          <header className="editarPerfilHeader">
            <span className="editarPerfilBadge">Perfil</span>
            <h1>Atualize seus dados</h1>
            <p>Mantenha telefone, endereço e e-mail sob controle.</p>
          </header>

          <form className="editarPerfilForm" onSubmit={saveProfile}>
            <section className="editarPerfilSection">
              <div className="editarPerfilFieldGroup">
                <CampoEntrada rotulo="Nome completo" value={perfil.nome} onChange={(event) => setPerfil((current) => ({ ...current, nome: event.target.value }))} />
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="E-mail atual" value={perfil.email} disabled />
                  <CampoEntrada rotulo="Telefone" value={perfil.telefone} onChange={(event) => setPerfil((current) => ({ ...current, telefone: event.target.value }))} />
                </div>
                <div className="editarPerfilCpf">
                  <div className="editarPerfilCpfHeader">
                    <span className="editarPerfilCpfLabel">CPF</span>
                    <span className="editarPerfilCpfLock">🔒</span>
                  </div>
                  <strong className="editarPerfilCpfValue">{perfil.cpf}</strong>
                  <p className="editarPerfilCpfHint">O CPF permanece bloqueado após o cadastro.</p>
                </div>
              </div>
            </section>

            <section className="editarPerfilSection">
              <div className="editarPerfilFieldGroup">
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="CEP" value={perfil.endereco?.cep || ""} onChange={(event) => updateAddress("cep", event.target.value)} />
                  <CampoEntrada rotulo="Estado" value={perfil.endereco?.estado || ""} onChange={(event) => updateAddress("estado", event.target.value)} />
                </div>
                <CampoEntrada rotulo="Logradouro" value={perfil.endereco?.logradouro || ""} onChange={(event) => updateAddress("logradouro", event.target.value)} />
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="Bairro" value={perfil.endereco?.bairro || ""} onChange={(event) => updateAddress("bairro", event.target.value)} />
                  <CampoEntrada rotulo="Cidade" value={perfil.endereco?.cidade || ""} onChange={(event) => updateAddress("cidade", event.target.value)} />
                </div>
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="Número" value={perfil.endereco?.numero || ""} onChange={(event) => updateAddress("numero", event.target.value)} />
                  <CampoEntrada rotulo="Complemento" value={perfil.endereco?.complemento || ""} onChange={(event) => updateAddress("complemento", event.target.value)} />
                </div>
              </div>
            </section>

            <section className="editarPerfilSection">
              <div className="editarPerfilFieldGroup">
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="Novo e-mail" value={novoEmail} onChange={(event) => setNovoEmail(event.target.value)} />
                  <Botao type="button" variante="secondary" onClick={requestEmailChange}>Solicitar troca</Botao>
                </div>
                <div className="editarPerfilFieldRow">
                  <CampoEntrada rotulo="Token de confirmação" value={tokenEmail} onChange={(event) => setTokenEmail(event.target.value)} />
                  <Botao type="button" variante="secondary" onClick={confirmEmailChange}>Confirmar e-mail</Botao>
                </div>
              </div>
            </section>

            <div className="editarPerfilActions">
              <Link className="editarPerfilCancel" to="/perfil">Cancelar</Link>
              <Botao type="submit" disabled={saving}>{saving ? "Salvando..." : "Salvar alterações"}</Botao>
            </div>
          </form>
        </main>
      </div>
    </div>
  );
};

export default EditarPerfil;
