import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { api } from "../api/services.js";
import { BarraNavegacao, LoadingState } from "../components/index.js";
import AnnouncementForm from "../components/AnnouncementForm.jsx";
import { useToast } from "../context/ToastContext.jsx";
import "./Anunciar.css";

const EditarAnuncio = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const toast = useToast();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [anuncio, setAnuncio] = useState(null);

  useEffect(() => {
    let active = true;
    api.anuncios
      .detalhar(id)
      .then((data) => {
        if (active) {
          setAnuncio({
            ...data,
            imagens: (data.imagens || []).map((url, index) => ({
              id: data.imagemIds?.[index],
              imagemId: data.imagemIds?.[index],
              url,
              nomeArquivo: `Imagem ${index + 1}`,
            })),
          });
          setLoading(false);
        }
      })
      .catch((requestError) => {
        toast.error(requestError.message);
        navigate("/meus-anuncios");
      });

    return () => {
      active = false;
    };
  }, [id, navigate, toast]);

  const handleSubmit = async (payload) => {
    setSaving(true);
    try {
      await api.anuncios.atualizar(id, payload);
      toast.success("Anúncio atualizado.");
      navigate(`/anuncios/${id}`);
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  if (loading || !anuncio) {
    return <LoadingState message="Carregando anúncio..." />;
  }

  return (
    <div className="anunciarPage">
      <BarraNavegacao />
      <AnnouncementForm
        initialData={anuncio}
        badge="Editar anúncio"
        title="Atualize fotos, texto e preço"
        subtitle="As mudanças são aplicadas no mesmo anúncio, respeitando as regras de aluguel já em andamento."
        submitLabel="Salvar anúncio"
        onSubmit={handleSubmit}
        submitting={saving}
      />
    </div>
  );
};

export default EditarAnuncio;
