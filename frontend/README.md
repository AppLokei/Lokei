# Frontend Lokei

## Stack

- React 18
- React Router 6
- Vite 4
- Vitest + Testing Library

## Estrutura

- `src/api`: cliente HTTP e serviços da API
- `src/components`: navegação, calendário, cards, modais e estados de página
- `src/context`: sessão e toasts
- `src/lib`: constantes e formatadores
- `src/pages`: telas da aplicação
- `src/routes`: rotas públicas, protegidas e admin

## Comandos

```bash
cd frontend
npm install
npm run dev
npm test
npm run build
```

## Ambiente

Use `.env.example` como referência:

- `VITE_API_BASE_URL=`
- `VITE_DEV_PROXY_TARGET=http://localhost:8080`

## Observações

- Neste ambiente local o Node disponível é `16.x`, então os scripts usam `--experimental-global-webcrypto`.
- Em `dev`, o Vite faz proxy para o backend Spring Boot.
- A edição de anúncio depende do campo `imagemIds` exposto no detalhe do anúncio.
