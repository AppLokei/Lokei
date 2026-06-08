import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

const proxyPrefixes = [
  "/auth",
  "/perfil",
  "/enderecos",
  "/arquivos",
  "/anuncios",
  "/alugueis",
  "/avaliacoes",
  "/chats",
  "/notificacoes",
  "/admin",
];

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), "");
  const proxyTarget = env.VITE_DEV_PROXY_TARGET || "http://localhost:8080";

  return {
    plugins: [react()],
    server: {
      port: 5173,
      proxy: proxyPrefixes.reduce((acc, prefix) => {
        acc[prefix] = {
          target: proxyTarget,
          changeOrigin: true,
        };
        return acc;
      }, {}),
    },
    test: {
      environment: "jsdom",
      setupFiles: "./src/test/setup.js",
      globals: true,
      css: false,
    },
  };
});
