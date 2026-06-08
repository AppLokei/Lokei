import { TOKEN_STORAGE_KEY } from "../lib/constants.js";
import { resolveApiPath } from "../lib/url.js";

export class ApiError extends Error {
  constructor(message, status, code) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.code = code;
  }
}

const parseResponse = async (response) => {
  const contentType = response.headers.get("content-type") || "";
  if (contentType.includes("application/json")) {
    return response.json();
  }
  return response.text();
};

export const apiRequest = async (path, options = {}) => {
  const token = sessionStorage.getItem(TOKEN_STORAGE_KEY);
  const headers = new Headers(options.headers || {});

  if (!headers.has("Accept")) {
    headers.set("Accept", "application/json");
  }

  if (token && !headers.has("Authorization")) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const isFormData = options.body instanceof FormData;
  if (options.body && !isFormData && !headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }

  const response = await fetch(resolveApiPath(path), {
    ...options,
    headers,
    body: isFormData || typeof options.body === "string" ? options.body : options.body ? JSON.stringify(options.body) : undefined,
  });

  const data = await parseResponse(response);

  if (!response.ok) {
    const message = typeof data === "object" && data?.mensagem ? data.mensagem : "Falha ao processar a requisição.";
    const code = typeof data === "object" && data?.codigo ? data.codigo : "REQUEST_ERROR";
    throw new ApiError(message, response.status, code);
  }

  return data;
};
