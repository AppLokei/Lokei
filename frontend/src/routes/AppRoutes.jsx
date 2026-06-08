import { Navigate, Route, Routes } from "react-router-dom";

import { ProtectedRoute } from "../components/index.js";
import {
  Home,
  Login,
  Cadastro,
  EsqueciSenha,
  RedefinirSenha,
  Anuncios,
  AnuncioDetalhe,
  Perfil,
  EditarPerfil,
  Anunciar,
  EditarAnuncio,
  MeusAlugueis,
  Chat,
  Avaliacao,
  AdminDenuncias,
} from "../pages/index.js";

const AppRoutes = () => (
  <Routes>
    <Route path="/" element={<Home />} />
    <Route path="/anuncios" element={<Anuncios />} />
    <Route path="/anuncios/:id" element={<AnuncioDetalhe />} />
    <Route path="/login" element={<Login />} />
    <Route path="/cadastro" element={<Cadastro />} />
    <Route path="/esqueci-senha" element={<EsqueciSenha />} />
    <Route path="/redefinir-senha" element={<RedefinirSenha />} />

    <Route element={<ProtectedRoute />}>
      <Route path="/perfil" element={<Perfil />} />
      <Route path="/perfil/editar" element={<EditarPerfil />} />
      <Route path="/anunciar" element={<Anunciar />} />
      <Route path="/anuncios/:id/editar" element={<EditarAnuncio />} />
      <Route path="/meus-alugueis" element={<MeusAlugueis />} />
      <Route path="/meus-anuncios" element={<Navigate to="/meus-alugueis?aba=anuncios" replace />} />
      <Route path="/reservas-recebidas" element={<Navigate to="/meus-alugueis?aba=recebidos" replace />} />
      <Route path="/chat" element={<Chat />} />
      <Route path="/avaliar" element={<Avaliacao />} />
    </Route>

    <Route element={<ProtectedRoute roles={["ADMIN"]} />}>
      <Route path="/admin/denuncias" element={<AdminDenuncias />} />
    </Route>

    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes>
);

export default AppRoutes;
