import {
  BrowserRouter as Roteador,
  Routes as Rotas,
  Route as Rota,
} from "react-router-dom";

import {
  Home,
  Login,
  Cadastro,
  Anuncios,
  AnuncioDetalhe,
  Perfil,
  Anunciar,
  MeusAlugueis,
  Chat,
  EditarPerfil,
  Avaliacao,
  EditarAnuncio,
} from "../pages";

const RotasApp = () => (
  <Roteador>
    <Rotas>
      <Rota path="/" element={<Home />} />
      <Rota path="/login" element={<Login />} />
      <Rota path="/cadastro" element={<Cadastro />} />
      <Rota path="/anuncios" element={<Anuncios />} />
      <Rota path="/anuncios/:id" element={<AnuncioDetalhe />} />
      <Rota path="/anunciar" element={<Anunciar />} />
      <Rota path="/meus-alugueis" element={<MeusAlugueis />} />
      <Rota path="/chat" element={<Chat />} />
      <Rota path="/perfil" element={<Perfil />} />
      <Rota path="/perfil/editar" element={<EditarPerfil />} />
      <Rota path="/avaliar" element={<Avaliacao />} />
      <Rota path="/editar-anuncio" element={<EditarAnuncio />} />
    </Rotas>
  </Roteador>
);

export default RotasApp;
