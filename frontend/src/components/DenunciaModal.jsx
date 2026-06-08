import { useState } from "react";

import Botao from "./Button.jsx";
import "./DenunciaModal.css";

const DenunciaModal = ({ aberto, aoFechar, aoEnviar, motivos, carregando }) => {
  const [motivo, setMotivo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [imagens, setImagens] = useState([]);
  const [erros, setErros] = useState({});

  if (!aberto) return null;

  const validar = () => {
    const nextErrors = {};
    if (!motivo) nextErrors.motivo = "Selecione um motivo.";
    if (!descricao.trim()) nextErrors.descricao = "Descreva a ocorrência.";
    return nextErrors;
  };

  const handleSubmit = async () => {
    const nextErrors = validar();
    setErros(nextErrors);
    if (Object.keys(nextErrors).length) return;
    await aoEnviar({ motivo, descricao: descricao.trim(), imagens });
    setMotivo("");
    setDescricao("");
    setImagens([]);
  };

  const appendImages = (event) => {
    const files = Array.from(event.target.files || []);
    setImagens((current) => [...current, ...files].slice(0, 5));
    event.target.value = "";
  };

  return (
    <div className="denunciaOverlay" onClick={aoFechar}>
      <div className="denunciaModal" onClick={(event) => event.stopPropagation()} role="dialog" aria-modal="true">
        <header className="denunciaHeader">
          <h2>Reportar anúncio</h2>
          <button className="denunciaClose" type="button" onClick={aoFechar} aria-label="Fechar">×</button>
        </header>
        <div className="denunciaBody">
          <label className="denunciaField">
            <span className="denunciaLabel">Motivo</span>
            <select className={`denunciaSelect${erros.motivo ? " denunciaSelect--error" : ""}`} value={motivo} onChange={(event) => setMotivo(event.target.value)}>
              <option value="">Selecione um motivo</option>
              {motivos.map((item) => (
                <option key={item.value} value={item.value}>{item.label}</option>
              ))}
            </select>
            {erros.motivo ? <span className="denunciaError">{erros.motivo}</span> : null}
          </label>

          <label className="denunciaField">
            <span className="denunciaLabel">Descrição</span>
            <textarea className={`avaliacaoTextarea${erros.descricao ? " avaliacaoTextarea--error" : ""}`} rows="5" value={descricao} onChange={(event) => setDescricao(event.target.value)} placeholder="Descreva com clareza o problema encontrado." />
            {erros.descricao ? <span className="denunciaError">{erros.descricao}</span> : null}
          </label>

          <div className="denunciaImages">
            <span className="denunciaLabel">Anexos opcionais</span>
            <div className="denunciaImageList">
              {imagens.map((image, index) => (
                <div key={`${image.name}-${index}`} className="denunciaImageItem">
                  <span className="denunciaImageName">{image.name}</span>
                  <button type="button" className="denunciaImageRemove" onClick={() => setImagens((current) => current.filter((_, currentIndex) => currentIndex !== index))}>
                    ×
                  </button>
                </div>
              ))}
            </div>
            <label className="denunciaAddImageBtn">
              Adicionar imagens
              <input className="denunciaFileInput" type="file" accept=".jpg,.jpeg,.png" multiple onChange={appendImages} />
            </label>
          </div>
        </div>
        <footer className="denunciaFooter">
          <Botao type="button" variante="secondary" onClick={aoFechar}>Cancelar</Botao>
          <Botao type="button" onClick={handleSubmit} disabled={carregando}>{carregando ? "Enviando..." : "Enviar denúncia"}</Botao>
        </footer>
      </div>
    </div>
  );
};

export default DenunciaModal;
