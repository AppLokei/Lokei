import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { Botao, CampoEntrada } from "../components/index.js";
import { useAuth } from "../context/AuthContext.jsx";
import { useToast } from "../context/ToastContext.jsx";
import { PAPEL_OPTIONS } from "../lib/constants.js";
import { api } from "../api/services.js";
import "./Cadastro.css";

const initialForm = {
  nome: "",
  email: "",
  cpf: "",
  telefone: "",
  senha: "",
  aceitouTermos: false,
  papel: "LOCATARIO",
  endereco: {
    cep: "",
    logradouro: "",
    bairro: "",
    numero: "",
    complemento: "",
    cidade: "",
    estado: "",
  },
};

const Cadastro = () => {
  const navigate = useNavigate();
  const toast = useToast();
  const { register, loading } = useAuth();
  const [form, setForm] = useState(initialForm);
  const [error, setError] = useState("");
  const [lookingUpCep, setLookingUpCep] = useState(false);

  const setAddress = (field, value) => {
    setForm((current) => ({ ...current, endereco: { ...current.endereco, [field]: value } }));
  };

  const lookupCep = async () => {
    const cep = form.endereco.cep.replace(/\D/g, "");
    if (cep.length !== 8) {
      toast.error("Informe um CEP com 8 dígitos.");
      return;
    }
    setLookingUpCep(true);
    try {
      const result = await api.profile.lookupCep(cep);
      setForm((current) => ({
        ...current,
        endereco: {
          ...current.endereco,
          cep: result.cep,
          logradouro: result.logradouro,
          bairro: result.bairro,
          cidade: result.cidade,
          estado: result.estado,
        },
      }));
      toast.success("Endereço preenchido pelo CEP.");
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setLookingUpCep(false);
    }
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      await register({
        ...form,
        cpf: form.cpf.replace(/\D/g, ""),
        telefone: form.telefone.replace(/\D/g, ""),
        endereco: {
          ...form.endereco,
          cep: form.endereco.cep.replace(/\D/g, ""),
          estado: form.endereco.estado.toUpperCase(),
        },
      });
      toast.success("Conta criada com sucesso.");
      navigate("/");
    } catch (requestError) {
      setError(requestError.message);
      toast.error(requestError.message);
    }
  };

  return (
    <div className="cadastroPage">
      <div className="cadastroSplit">
        <section className="cadastroBrand">
          <div className="cadastroBrandInner">
            <img src="/logo.png" alt="Lokei" className="cadastroBrandLogo" />
            <strong className="cadastroBrandTitle">Crie uma conta para publicar ferramentas ou alugar com segurança.</strong>
            <p className="cadastroBrandSubtitle">O cadastro respeita validação de CPF, endereço por CEP, aceite de termos e perfil de uso.</p>
          </div>
        </section>
        <section className="cadastroFormPanel">
          <header className="cadastroHeader">
            <span className="cadastroEyebrow">Cadastro</span>
            <h1 className="cadastroTitle">Comece pela sua conta</h1>
            <p className="cadastroSubtitle">Preencha seus dados pessoais e endereço principal.</p>
          </header>
          <form className="cadastroForm" onSubmit={handleSubmit}>
            <section className="cadastroSection">
              <div className="cadastroSectionHeader">
                <h2 className="cadastroSectionTitle">Dados pessoais</h2>
              </div>
              <div className="cadastroFieldGroup">
                <CampoEntrada rotulo="Nome completo" value={form.nome} onChange={(event) => setForm((current) => ({ ...current, nome: event.target.value }))} />
                <div className="cadastroFieldRow">
                  <CampoEntrada rotulo="E-mail" type="email" value={form.email} onChange={(event) => setForm((current) => ({ ...current, email: event.target.value }))} />
                  <label className="fieldLabel">
                    Perfil principal
                    <select value={form.papel} onChange={(event) => setForm((current) => ({ ...current, papel: event.target.value }))}>
                      {PAPEL_OPTIONS.map((option) => (
                        <option key={option.value} value={option.value}>{option.label}</option>
                      ))}
                    </select>
                  </label>
                </div>
                <div className="cadastroFieldRow">
                  <CampoEntrada rotulo="CPF" value={form.cpf} onChange={(event) => setForm((current) => ({ ...current, cpf: event.target.value }))} />
                  <CampoEntrada rotulo="Telefone" value={form.telefone} onChange={(event) => setForm((current) => ({ ...current, telefone: event.target.value }))} />
                </div>
                <CampoEntrada rotulo="Senha" type="password" value={form.senha} onChange={(event) => setForm((current) => ({ ...current, senha: event.target.value }))} erro={error} />
              </div>
            </section>

            <section className="cadastroSection">
              <div className="cadastroSectionHeader">
                <h2 className="cadastroSectionTitle">Endereço</h2>
                <p className="cadastroSectionHint">Você pode consultar o CEP antes de preencher os demais campos.</p>
              </div>
              <div className="cadastroFieldGroup">
                <div className="cadastroFieldRow">
                  <CampoEntrada rotulo="CEP" value={form.endereco.cep} onChange={(event) => setAddress("cep", event.target.value)} acao={<button className="inputActionButton" type="button" onClick={lookupCep}>{lookingUpCep ? "Buscando" : "Buscar CEP"}</button>} />
                  <CampoEntrada rotulo="Estado" value={form.endereco.estado} onChange={(event) => setAddress("estado", event.target.value)} />
                </div>
                <CampoEntrada rotulo="Logradouro" value={form.endereco.logradouro} onChange={(event) => setAddress("logradouro", event.target.value)} />
                <div className="cadastroFieldRow">
                  <CampoEntrada rotulo="Bairro" value={form.endereco.bairro} onChange={(event) => setAddress("bairro", event.target.value)} />
                  <CampoEntrada rotulo="Cidade" value={form.endereco.cidade} onChange={(event) => setAddress("cidade", event.target.value)} />
                </div>
                <div className="cadastroFieldRow">
                  <CampoEntrada rotulo="Número" value={form.endereco.numero} onChange={(event) => setAddress("numero", event.target.value)} />
                  <CampoEntrada rotulo="Complemento" value={form.endereco.complemento} onChange={(event) => setAddress("complemento", event.target.value)} />
                </div>
              </div>
            </section>

            <label className="cadastroTerms">
              <input type="checkbox" checked={form.aceitouTermos} onChange={(event) => setForm((current) => ({ ...current, aceitouTermos: event.target.checked }))} />
              <span>Declaro que li e aceito os termos de uso e a política mínima de privacidade da plataforma.</span>
            </label>

            <div className="cadastroActions">
              <Botao type="submit" disabled={loading}>{loading ? "Criando conta..." : "Criar conta"}</Botao>
              <Link className="cadastroLink" to="/login">Já tenho conta</Link>
            </div>
          </form>
        </section>
      </div>
    </div>
  );
};

export default Cadastro;
