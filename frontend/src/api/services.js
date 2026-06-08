import { apiRequest } from "./client.js";

const qs = (params = {}) => {
  const search = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value === undefined || value === null || value === "") return;
    search.set(key, value);
  });
  const query = search.toString();
  return query ? `?${query}` : "";
};

export const api = {
  auth: {
    register: (payload) => apiRequest("/auth/register", { method: "POST", body: payload }),
    login: (payload) => apiRequest("/auth/login", { method: "POST", body: payload }),
    logout: () => apiRequest("/auth/logout", { method: "POST" }),
    forgotPassword: (payload) => apiRequest("/auth/forgot-password", { method: "POST", body: payload }),
    resetPassword: (payload) => apiRequest("/auth/reset-password", { method: "POST", body: payload }),
  },
  profile: {
    get: () => apiRequest("/perfil"),
    update: (payload) => apiRequest("/perfil", { method: "PUT", body: payload }),
    requestEmailChange: (payload) => apiRequest("/perfil/email/solicitar", { method: "POST", body: payload }),
    confirmEmailChange: (payload) => apiRequest("/perfil/email/confirmar", { method: "POST", body: payload }),
    lookupCep: (cep) => apiRequest(`/enderecos/cep/${cep}`),
  },
  files: {
    upload: (files) => {
      const formData = new FormData();
      files.forEach((file) => formData.append("files", file));
      return apiRequest("/arquivos/upload", { method: "POST", body: formData });
    },
  },
  anuncios: {
    principais: (limite = 8) => apiRequest(`/anuncios/principais${qs({ limite })}`),
    listar: (params) => apiRequest(`/anuncios${qs(params)}`),
    meus: () => apiRequest("/anuncios/meus"),
    detalhar: (id) => apiRequest(`/anuncios/${id}`),
    disponibilidade: (id) => apiRequest(`/anuncios/${id}/disponibilidade`),
    criar: (payload) => apiRequest("/anuncios", { method: "POST", body: payload }),
    atualizar: (id, payload) => apiRequest(`/anuncios/${id}`, { method: "PUT", body: payload }),
    pausar: (id) => apiRequest(`/anuncios/${id}/pausar`, { method: "PATCH" }),
    reativar: (id) => apiRequest(`/anuncios/${id}/reativar`, { method: "PATCH" }),
    excluir: (id) => apiRequest(`/anuncios/${id}`, { method: "DELETE" }),
    reservar: (id, payload) => apiRequest(`/anuncios/${id}/reservas`, { method: "POST", body: payload }),
    denunciar: (id, payload) => apiRequest(`/anuncios/${id}/denuncias`, { method: "POST", body: payload }),
  },
  alugueis: {
    meus: () => apiRequest("/alugueis/meus"),
    recebidos: () => apiRequest("/alugueis/recebidos"),
    detalhar: (id) => apiRequest(`/alugueis/${id}`),
    aprovar: (id) => apiRequest(`/alugueis/${id}/aprovar`, { method: "PATCH" }),
    reprovar: (id, payload) => apiRequest(`/alugueis/${id}/reprovar`, { method: "PATCH", body: payload }),
    cancelar: (id, payload) => apiRequest(`/alugueis/${id}/cancelar`, { method: "PATCH", body: payload }),
    abrirChat: (id) => apiRequest(`/alugueis/${id}/chat`),
  },
  avaliacoes: {
    anuncio: (payload) => apiRequest("/avaliacoes/anuncios", { method: "POST", body: payload }),
    perfil: (payload) => apiRequest("/avaliacoes/perfis", { method: "POST", body: payload }),
    listarAnuncio: (anuncioId) => apiRequest(`/avaliacoes/anuncios/${anuncioId}`),
    listarUsuario: (usuarioId) => apiRequest(`/avaliacoes/usuarios/${usuarioId}`),
  },
  chats: {
    listar: () => apiRequest("/chats"),
    detalhar: (chatId) => apiRequest(`/chats/${chatId}`),
    enviarMensagem: (chatId, payload) => apiRequest(`/chats/${chatId}/mensagens`, { method: "POST", body: payload }),
  },
  notificacoes: {
    listar: () => apiRequest("/notificacoes"),
    marcarLida: (id) => apiRequest(`/notificacoes/${id}/lida`, { method: "PATCH" }),
  },
  admin: {
    denuncias: () => apiRequest("/admin/denuncias"),
    moderarDenuncia: (id, payload) => apiRequest(`/admin/denuncias/${id}`, { method: "PATCH", body: payload }),
  },
};
