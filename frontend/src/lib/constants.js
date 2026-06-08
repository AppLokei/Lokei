export const TOKEN_STORAGE_KEY = "lokei.auth.token";
export const USER_STORAGE_KEY = "lokei.auth.user";

export const CATEGORIAS = [
  { value: "ALICATES", label: "Alicates" },
  { value: "APARADORES_E_CORTADORES_DE_GRAMA", label: "Aparadores e cortadores de grama" },
  { value: "BETONEIRAS", label: "Betoneiras" },
  { value: "CAIXAS_E_MALETAS_DE_FERRAMENTAS", label: "Caixas e maletas" },
  { value: "CHAVES_DE_FENDA", label: "Chaves de fenda" },
  { value: "EQUIPAMENTOS_DE_PROTECAO_INDIVIDUAL", label: "EPIs" },
  { value: "ESCADAS", label: "Escadas" },
  { value: "ESMILHADEIRAS", label: "Esmerilhadeiras" },
  { value: "ESQUADROS_FITAS_METRICAS_E_TRENAS", label: "Medição e trenas" },
  { value: "FURADEIRAS_E_PARAFUSADEIRAS", label: "Furadeiras e parafusadeiras" },
  { value: "LIXADEIRAS", label: "Lixadeiras" },
  { value: "MARTELOS", label: "Martelos" },
  { value: "SERRAS_E_MOTOSSERRAS", label: "Serras e motosserras" },
  { value: "OUTROS", label: "Outros" },
];

export const PAPEL_OPTIONS = [
  { value: "LOCATARIO", label: "Locatário" },
  { value: "LOCADOR", label: "Locador" },
];

export const MOTIVOS_DENUNCIA = [
  { value: "ANUNCIO_ENGANOSO", label: "Anúncio enganoso" },
  { value: "ITEM_PROIBIDO", label: "Item proibido" },
  { value: "FRAUDE", label: "Fraude" },
  { value: "COMPORTAMENTO_INADEQUADO", label: "Comportamento inadequado" },
  { value: "OUTRO", label: "Outro" },
];

export const STATUS_LABELS = {
  ATIVO: "Ativo",
  PAUSADO: "Pausado",
  DESATIVADO: "Desativado",
  EM_APROVACAO: "Em aprovação",
  CONFIRMADO: "Confirmado",
  ATIVO_ALUGUEL: "Em andamento",
  ATIVO_LOCACAO: "Em andamento",
  ATIVO_RESERVA: "Em andamento",
  ATIVO_USO: "Em andamento",
  ATIVO: "Ativo",
  REPROVADO: "Reprovado",
  CANCELADO: "Cancelado",
  CONCLUIDO: "Concluído",
};

export const STATUS_VARIANTS = {
  ATIVO: "success",
  PAUSADO: "neutral",
  DESATIVADO: "danger",
  EM_APROVACAO: "warning",
  CONFIRMADO: "info",
  REPROVADO: "danger",
  CANCELADO: "neutral",
  CONCLUIDO: "success",
};

export const SORT_OPTIONS = [
  { value: "recente", label: "Mais recentes" },
  { value: "precoAsc", label: "Menor preço" },
  { value: "precoDesc", label: "Maior preço" },
];
