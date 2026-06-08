import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { api } from "../api/services.js";
import { BarraNavegacao } from "../components/index.js";
import AnnouncementForm from "../components/AnnouncementForm.jsx";
import { useToast } from "../context/ToastContext.jsx";
import "./Anunciar.css";

const Anunciar = () => {
  const navigate = useNavigate();
  const toast = useToast();
  const [saving, setSaving] = useState(false);

  const handleSubmit = async (payload) => {
    setSaving(true);
    try {
      const created = await api.anuncios.criar(payload);
      toast.success("Anúncio publicado com sucesso.");
      navigate(`/anuncios/${created.id}`);
    } catch (requestError) {
      toast.error(requestError.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="anunciarPage">
      <BarraNavegacao />
      <AnnouncementForm
        badge="Novo anúncio"
        title="Publique sua ferramenta"
        subtitle="Envie imagens, descreva o estado e defina o valor diário para aparecer no catálogo." 
        submitLabel="Publicar anúncio"
        onSubmit={handleSubmit}
        submitting={saving}
      />
    </div>
  );
};

export default Anunciar;
