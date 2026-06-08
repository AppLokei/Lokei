const baseUrl = (import.meta.env.VITE_API_BASE_URL || "").replace(/\/$/, "");

export const resolveApiPath = (path) => {
  if (!path) return "";
  if (/^https?:\/\//i.test(path)) return path;
  if (!baseUrl) return path;
  return `${baseUrl}${path.startsWith("/") ? path : `/${path}`}`;
};
