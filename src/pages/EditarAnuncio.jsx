import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import CampoEntrada from "../components/Input";
import Botao from "../components/Button";
import BarraNavegacao from "../components/NavigationBar";
import "./EditarAnuncio.css";

const MAX_FOTOS = 5;
const TAMANHO_MAXIMO_ARQUIVO = 5 * 1024 * 1024;
const TIPOS_PERMITIDOS = ["image/jpeg", "image/jpg", "image/png"];

/* Mock: dados atuais do anuncio */
const anuncioMock = {
  id: 1,
  titulo: "Furadeira de Impacto Bosch",
  categoria: "eletricas",
  valor: "45,00",
  descricao:
    "Furadeira de impacto com alta potencia, ideal para obras e reparos. Inclui maleta, brocas e empunhadura lateral.",
  fotosExistentes: [
    {
      nome: "furadeira-01.jpg",
      url: "https://images.unsplash.com/photo-1572981779307-38b8cabb2407?auto=format&fit=crop&w=400&q=60",
    },
  ],
  /* RN: se a ferramenta esta alugada, o preco nao pode ser alterado */
  alugada: true,
};

const EditarAnuncio = () => {
  const [titulo, setTitulo] = useState(anuncioMock.titulo);
  const [categoria, setCategoria] = useState(anuncioMock.categoria);
  const [valor, setValor] = useState(anuncioMock.valor);
  const [descricao, setDescricao] = useState(anuncioMock.descricao);
  const [fotosExistentes, setFotosExistentes] = useState(
    anuncioMock.fotosExistentes
  );
  const [fotosNovas, setFotosNovas] = useState([]);
  const [erros, setErros] = useState({});
  const navigate = useNavigate();

  const totalFotos = fotosExistentes.length + fotosNovas.length;
  const podeAdicionarMais = totalFotos < MAX_FOTOS;

  const previewNovas = useMemo(
    () =>
      fotosNovas.map((arquivo) => ({
        nome: arquivo.name,
        url: URL.createObjectURL(arquivo),
      })),
    [fotosNovas]
  );

  useEffect(() => {
    return () => {
      previewNovas.forEach((arquivo) => URL.revokeObjectURL(arquivo.url));
    };
  }, [previewNovas]);

  const handleFiles = (evento) => {
    const selecionados = Array.from(evento.target.files || []);
    if (!selecionados.length) return;

    const proximosErros = { ...erros };
    delete proximosErros.fotos;

    const slotsDisponiveis = MAX_FOTOS - totalFotos;
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
      setFotosNovas((prev) => [...prev, ...arquivosValidos]);
    }

    if (selecionados.length > slotsDisponiveis) {
      proximosErros.fotos = "Limite maximo de 5 fotos.";
    }

    setErros(proximosErros);
    evento.target.value = "";
  };

  const removeExistente = (index) => {
    setFotosExistentes((prev) => prev.filter((_, i) => i !== index));
  };

  const removeNova = (index) => {
    setFotosNovas((prev) => prev.filter((_, i) => i !== index));
  };

  const validar = () => {
    const proximosErros = {};
    const valorNumero = Number(valor.replace(",", "."));

    if (!titulo.trim()) proximosErros.titulo = "Informe o titulo.";
    if (!categoria) proximosErros.categoria = "Selecione uma categoria.";

    if (!anuncioMock.alugada) {
      if (!valor.trim() || Number.isNaN(valorNumero) || valorNumero <= 0) {
        proximosErros.valor = "O valor deve ser maior que R$ 0,00.";
      }
    }

    if (!descricao.trim()) proximosErros.descricao = "Informe a descricao.";

    if (fotosExistentes.length + fotosNovas.length === 0) {
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
      id: anuncioMock.id,
      titulo,
      categoria,
      valor,
      descricao,
      fotosExistentes,
      fotosNovas,
    });
    navigate("/anuncios/" + anuncioMock.id);
  };

  return (
    <div className="editarAnuncioPage">
      <BarraNavegacao />
      <div className="editarAnuncioCard">
        <header className="editarAnuncioHeader">
          <span className="editarAnuncioBadge">Editar anuncio</span>
          <h1>Atualize sua ferramenta</h1>
          <p>Altere fotos, descricao e detalhes do anuncio.</p>
        </header>

        <form className="editarAnuncioForm" onSubmit={handleSubmit}>
          {/* Fotos */}
          <div className="editarAnuncioUpload">
            <label className="editarAnuncioUploadLabel">
              Fotos ({totalFotos}/{MAX_FOTOS})
            </label>

            {/* Fotos existentes */}
            {fotosExistentes.length > 0 || previewNovas.length > 0 ? (
              <div className="editarAnuncioPreviewGrid">
                {fotosExistentes.map((arquivo, index) => (
                  <figure key={`existing-${index}`} className="editarAnuncioPreview">
                    <div className="editarAnuncioPreviewImage">
                      <img src={arquivo.url} alt={arquivo.nome} />
                      <button
                        type="button"
                        className="editarAnuncioRemove"
                        onClick={() => removeExistente(index)}
                        aria-label="Remover foto"
                      >
                        Remover
                      </button>
                    </div>
                    <figcaption>{arquivo.nome}</figcaption>
                  </figure>
                ))}
                {previewNovas.map((arquivo, index) => (
                  <figure key={`new-${index}`} className="editarAnuncioPreview">
                    <div className="editarAnuncioPreviewImage">
                      <img src={arquivo.url} alt={arquivo.nome} />
                      <button
                        type="button"
                        className="editarAnuncioRemove"
                        onClick={() => removeNova(index)}
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

            {podeAdicionarMais ? (
              <div className="editarAnuncioUploadArea">
                <div>
                  <strong>Arraste ou selecione fotos</strong>
                  <span>Formatos JPG, JPEG ou PNG ate 5MB</span>
                </div>
                <label className="editarAnuncioUploadButton">
                  Adicionar fotos
                  <input
                    type="file"
                    accept=".jpg,.jpeg,.png"
                    multiple
                    onChange={handleFiles}
                    className="editarAnuncioUploadInput"
                  />
                </label>
              </div>
            ) : null}

            {erros.fotos ? (
              <span className="editarAnuncioError">{erros.fotos}</span>
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

          <label className="editarAnuncioSelectField">
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
              <span className="editarAnuncioError">{erros.categoria}</span>
            ) : null}
          </label>

          {/* Valor: bloqueado se a ferramenta esta alugada */}
          <div>
            <CampoEntrada
              rotulo="Valor do aluguel (R$)"
              name="valor"
              placeholder="0,00"
              value={valor}
              onChange={(evento) => setValor(evento.target.value)}
              erro={erros.valor}
              disabled={anuncioMock.alugada}
            />
            {anuncioMock.alugada ? (
              <span className="editarAnuncioHint">
                O valor nao pode ser alterado enquanto a ferramenta esta alugada.
              </span>
            ) : null}
          </div>

          <label className="editarAnuncioTextareaField">
            <span>Descricao</span>
            <textarea
              value={descricao}
              onChange={(evento) => setDescricao(evento.target.value)}
              placeholder="Descreva o estado, itens inclusos e observacoes."
              className={erros.descricao ? "hasError" : ""}
              rows={4}
            />
            {erros.descricao ? (
              <span className="editarAnuncioError">{erros.descricao}</span>
            ) : null}
          </label>

          <Botao type="submit">Salvar Alteracoes</Botao>
          <Botao
            type="button"
            variante="secondary"
            onClick={() => navigate(-1)}
          >
            Cancelar
          </Botao>
        </form>
      </div>
    </div>
  );
};

export default EditarAnuncio;
