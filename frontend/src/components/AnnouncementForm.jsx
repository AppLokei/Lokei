import { useEffect, useMemo, useState } from "react";

import { api } from "../api/services.js";
import { CATEGORIAS } from "../lib/constants.js";
import { resolveApiPath } from "../lib/url.js";
import { useToast } from "../context/ToastContext.jsx";
import { Botao, CampoEntrada } from "./index.js";

const toDraft = (initialData) => ({
  titulo: initialData?.titulo || "",
  categoria: initialData?.categoria || "",
  valorDiario: initialData?.valorDiario ? String(initialData.valorDiario).replace(".", ",") : "",
  descricao: initialData?.descricao || "",
  imagens:
    initialData?.imagens?.map((imagem, index) => ({
      imagemId: imagem.imagemId ?? imagem.id,
      url: imagem.url,
      nomeArquivo: imagem.nomeArquivo || `Imagem ${index + 1}`,
    })) || [],
});

const AnnouncementForm = ({
  initialData,
  title,
  subtitle,
  badge,
  submitLabel,
  onSubmit,
  submitting,
}) => {
  const toast = useToast();
  const [draft, setDraft] = useState(() => toDraft(initialData));
  const [uploading, setUploading] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    setDraft(toDraft(initialData));
  }, [initialData]);

  const canAddMore = draft.imagens.length < 5;
  const imageIds = useMemo(() => draft.imagens.map((image) => image.imagemId), [draft.imagens]);

  const validate = () => {
    const next = {};
    if (!draft.titulo.trim()) next.titulo = "Informe o título.";
    if (!draft.categoria) next.categoria = "Selecione uma categoria.";
    if (!draft.valorDiario.trim() || Number.isNaN(Number(draft.valorDiario.replace(",", ".")))) {
      next.valorDiario = "Informe um valor diário válido.";
    }
    if (!draft.descricao.trim()) next.descricao = "Descreva a ferramenta.";
    if (!imageIds.length) next.imagens = "Adicione pelo menos uma imagem.";
    return next;
  };

  const handleUpload = async (event) => {
    const files = Array.from(event.target.files || []);
    if (!files.length) return;
    setUploading(true);
    try {
      const uploaded = await api.files.upload(files.slice(0, 5 - draft.imagens.length));
      setDraft((current) => ({
        ...current,
        imagens: [
          ...current.imagens,
          ...uploaded.map((item) => ({
            imagemId: item.imagemId,
            url: item.url,
            nomeArquivo: item.nomeArquivo,
          })),
        ].slice(0, 5),
      }));
      toast.success("Imagem enviada com sucesso.");
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setUploading(false);
      event.target.value = "";
    }
  };

  const submit = async (event) => {
    event.preventDefault();
    const nextErrors = validate();
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length) return;
    await onSubmit({
      titulo: draft.titulo.trim(),
      descricao: draft.descricao.trim(),
      categoria: draft.categoria,
      valorDiario: Number(draft.valorDiario.replace(",", ".")),
      imagemIds: imageIds,
    });
  };

  return (
    <div className="anunciarLayout">
      <section className="anunciarMain">
        <header className="anunciarHeader">
          <span className="anunciarBadge">{badge}</span>
          <h1 className="anunciarTitle">{title}</h1>
          <p className="anunciarSubtitle">{subtitle}</p>
        </header>

        <form className="anunciarForm" onSubmit={submit}>
          <div className="anunciarUpload">
            <label className="anunciarUploadLabel">Fotos (máximo 5)</label>
            <div className="anunciarUploadArea">
              <div>
                <strong>Galeria do anúncio</strong>
                <span>JPG, JPEG ou PNG até 5MB por arquivo.</span>
              </div>
              {canAddMore ? (
                <label className="anunciarUploadButton">
                  {uploading ? "Enviando..." : "Adicionar fotos"}
                  <input className="anunciarUploadInput" type="file" accept=".jpg,.jpeg,.png" multiple onChange={handleUpload} disabled={uploading} />
                </label>
              ) : null}
            </div>
            {draft.imagens.length ? (
              <div className="anunciarPreviewGrid">
                {draft.imagens.map((imagem) => (
                  <figure key={imagem.imagemId} className="anunciarPreview">
                    <div className="anunciarPreviewImage">
                      <img src={resolveApiPath(imagem.url)} alt={imagem.nomeArquivo} />
                      <button type="button" className="anunciarRemove" onClick={() => setDraft((current) => ({ ...current, imagens: current.imagens.filter((item) => item.imagemId !== imagem.imagemId) }))}>
                        Remover
                      </button>
                    </div>
                    <figcaption>{imagem.nomeArquivo}</figcaption>
                  </figure>
                ))}
              </div>
            ) : null}
            {errors.imagens ? <span className="anunciarError">{errors.imagens}</span> : null}
          </div>

          <CampoEntrada rotulo="Título" value={draft.titulo} onChange={(event) => setDraft((current) => ({ ...current, titulo: event.target.value }))} erro={errors.titulo} />

          <label className="anunciarSelectField">
            <span>Categoria</span>
            <select value={draft.categoria} onChange={(event) => setDraft((current) => ({ ...current, categoria: event.target.value }))} className={errors.categoria ? "hasError" : ""}>
              <option value="">Selecione</option>
              {CATEGORIAS.map((categoria) => (
                <option key={categoria.value} value={categoria.value}>{categoria.label}</option>
              ))}
            </select>
            {errors.categoria ? <span className="anunciarError">{errors.categoria}</span> : null}
          </label>

          <CampoEntrada rotulo="Valor da diária (R$)" value={draft.valorDiario} onChange={(event) => setDraft((current) => ({ ...current, valorDiario: event.target.value }))} erro={errors.valorDiario} />

          <label className="anunciarTextareaField">
            <span>Descrição</span>
            <textarea value={draft.descricao} onChange={(event) => setDraft((current) => ({ ...current, descricao: event.target.value }))} className={errors.descricao ? "hasError" : ""} rows="5" />
            {errors.descricao ? <span className="anunciarError">{errors.descricao}</span> : null}
          </label>

          <div className="anunciarCtaBar">
            <Botao type="submit" disabled={submitting || uploading}>{submitting ? "Salvando..." : submitLabel}</Botao>
          </div>
        </form>
      </section>

      <aside className="anunciarTips">
        <div className="anunciarTipsInner">
          <h2>Checklist do anúncio</h2>
          <ul>
            <li>Título objetivo e categoria compatível.</li>
            <li>Pelo menos uma foto nítida da ferramenta.</li>
            <li>Descrição com estado, acessórios e cuidados.</li>
            <li>Preço coerente com o uso diário.</li>
          </ul>
        </div>
      </aside>
    </div>
  );
};

export default AnnouncementForm;
