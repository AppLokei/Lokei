import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./Anunciar.css";

const MAX_FOTOS = 5;
const TAMANHO_MAXIMO_ARQUIVO = 5 * 1024 * 1024;
const TIPOS_PERMITIDOS = ["image/jpeg", "image/jpg", "image/png"];

const Anunciar = () => {
  const [titulo, setTitulo] = useState("");
  const [categoria, setCategoria] = useState("");
  const [valor, setValor] = useState("");
  const [descricao, setDescricao] = useState("");
  const [fotos, setFotos] = useState([]);
  const [erros, setErros] = useState({});
  const navigate = useNavigate();

  const podeAdicionarMais = fotos.length < MAX_FOTOS;

  const urlsPreview = useMemo(
    () =>
      fotos.map((arquivo) => ({
        nome: arquivo.name,
        url: URL.createObjectURL(arquivo),
      })),
    [fotos]
  );

  useEffect(() => {
    return () => {
      urlsPreview.forEach((arquivo) => URL.revokeObjectURL(arquivo.url));
    };
  }, [urlsPreview]);

  const handleFiles = (evento) => {
    const selecionados = Array.from(evento.target.files || []);
    if (!selecionados.length) return;

    const proximosErros = { ...erros };
    delete proximosErros.fotos;

    const slotsDisponiveis = MAX_FOTOS - fotos.length;
    const arquivosValidos = [];

    selecionados.forEach((arquivo) => {
      if (arquivosValidos.length >= slotsDisponiveis) return;
      if (!TIPOS_PERMITIDOS.includes(arquivo.type)) {
        proximosErros.fotos = "Use apenas JPG, JPEG ou PNG.";
        return;
      }
      if (arquivo.size > TAMANHO_MAXIMO_ARQUIVO) {
        proximosErros.fotos = "Cada foto deve ter no maximo 5MB.";
        return;
      }
      arquivosValidos.push(arquivo);
    });

    if (arquivosValidos.length) {
      setFotos((prev) => [...prev, ...arquivosValidos]);
    }

    if (selecionados.length > slotsDisponiveis) {
      proximosErros.fotos = "Limite maximo de 5 fotos.";
    }

    setErros(proximosErros);
    evento.target.value = "";
  };

  const validar = () => {
    const proximosErros = {};
    const valorNumero = Number(valor.replace(",", "."));

    if (!titulo.trim()) proximosErros.titulo = "Informe o titulo.";
    if (!categoria) proximosErros.categoria = "Selecione uma categoria.";
    if (!valor.trim() || Number.isNaN(valorNumero) || valorNumero <= 0) {
      proximosErros.valor = "O valor deve ser maior que R$ 0,00.";
    }
    if (!descricao.trim()) proximosErros.descricao = "Informe a descricao.";
    if (fotos.length === 0) {
      proximosErros.fotos = "Adicione pelo menos uma foto.";
    }

    return proximosErros;
  };

  const handleSubmit = (evento) => {
    evento.preventDefault();
    const proximosErros = validar();
    setErros(proximosErros);
    if (Object.keys(proximosErros).length > 0) return;

    console.log({
      titulo,
      categoria,
      valor,
      descricao,
      fotos,
    });
    navigate("/");
  };

  return (
    <div className="anunciarPage">
      <BarraNavegacao />
      <div className="anunciarCard">
        <header className="anunciarHeader">
          <span className="anunciarBadge">Novo anuncio</span>
          <h1 className="anunciarTitle">Publique sua ferramenta</h1>
          <p className="anunciarSubtitle">
            Adicione fotos e detalhes para atrair mais locatarios.
          </p>
        </header>

        <form className="anunciarForm" onSubmit={handleSubmit}>
          <div className="anunciarUpload">
            <label className="anunciarUploadLabel">Fotos (max. 5)</label>
            <div className="anunciarUploadArea">
              <div>
                <strong>Arraste ou selecione fotos</strong>
                <span>Formatos JPG, JPEG ou PNG ate 5MB</span>
              </div>
              {podeAdicionarMais ? (
                <label className="anunciarUploadButton">
                  Adicionar fotos
                  <input
                    type="file"
                    accept=".jpg,.jpeg,.png"
                    multiple
                    onChange={handleFiles}
                    className="anunciarUploadInput"
                  />
                </label>
              ) : null}
            </div>
            {urlsPreview.length ? (
              <div className="anunciarPreviewGrid">
                {urlsPreview.map((arquivo, index) => (
                  <figure
                    key={`${arquivo.nome}-${index}`}
                    className="anunciarPreview"
                  >
                    <div className="anunciarPreviewImage">
                      <img src={arquivo.url} alt={arquivo.nome} />
                      <button
                        type="button"
                        className="anunciarRemove"
                        onClick={() =>
                          setFotos((prev) => prev.filter((_, i) => i !== index))
                        }
                        aria-label="Remover foto"
                      >
                        Remover
                      </button>
                    </div>
                    <figcaption>{arquivo.nome}</figcaption>
                  </figure>
                ))}
              </div>
            ) : null}
            {erros.fotos ? (
              <span className="anunciarError">{erros.fotos}</span>
            ) : null}
          </div>

          <CampoEntrada
            rotulo="Titulo"
            name="titulo"
            placeholder="Ex: Furadeira de impacto"
            value={titulo}
            onChange={(evento) => setTitulo(evento.target.value)}
            erro={erros.titulo}
          />

          <label className="anunciarSelectField">
            <span>Categoria</span>
            <select
              value={categoria}
              onChange={(evento) => setCategoria(evento.target.value)}
              className={erros.categoria ? "hasError" : ""}
            >
              <option value="">Selecione</option>
              <option value="eletricas">Ferramentas Eletricas</option>
              <option value="manuais">Ferramentas Manuais</option>
              <option value="jardinagem">Jardinagem</option>
            </select>
            {erros.categoria ? (
              <span className="anunciarError">{erros.categoria}</span>
            ) : null}
          </label>

          <CampoEntrada
            rotulo="Valor do aluguel (R$)"
            name="valor"
            placeholder="0,00"
            value={valor}
            onChange={(evento) => setValor(evento.target.value)}
            erro={erros.valor}
          />

          <label className="anunciarTextareaField">
            <span>Descricao</span>
            <textarea
              value={descricao}
              onChange={(evento) => setDescricao(evento.target.value)}
              placeholder="Descreva o estado, itens inclusos e observacoes."
              className={erros.descricao ? "hasError" : ""}
              rows={4}
            />
            {erros.descricao ? (
              <span className="anunciarError">{erros.descricao}</span>
            ) : null}
          </label>

          <Botao type="submit">Publicar Anuncio</Botao>
        </form>
      </div>
    </div>
  );
};

export default Anunciar;
