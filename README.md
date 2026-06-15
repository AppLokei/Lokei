# Lokei

API REST da plataforma Lokei (aluguel de ferramentas), construída com Spring Boot.

## Requisitos

- Java 21
- Maven (ou o wrapper `./mvnw` incluído no projeto)

## Como rodar

O projeto já vem com um perfil padrão (`test`) que usa banco **H2 em memória** — não precisa de banco externo nem credenciais.

```bash
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8081`.

> Os dados ficam em memória e são reiniciados a cada execução. Para testar o fluxo completo, cadastre o usuário primeiro (ele recebe o id 1) e depois use esse id nos demais endpoints.

### Console do H2

Disponível em `http://localhost:8081/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, usuário `sa`, sem senha).

## Endpoints principais

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/usuario/cadastro` | Cadastra um usuário |
| PUT | `/usuario/{id}` | Atualiza um usuário |
| POST | `/anuncio` | Publica um anúncio/ferramenta (multipart: `anuncio` + `imagens`) |
| GET | `/anuncios` | Lista anúncios |
| POST | `/auth/login` | Autenticação (retorna token JWT) |

## Rodar os testes

```bash
./mvnw test
```

## Perfis disponíveis

| Perfil | Banco | Observações |
|--------|-------|-------------|
| `test` (padrão) | H2 em memória | Integrações externas (CPF e Cloudinary) desabilitadas para rodar sem credenciais |
| `dev` | PostgreSQL | Requer banco e variáveis de ambiente configuradas |
| `local` | PostgreSQL (Docker) | Aponta para `Lokei_db` local; integrações externas desabilitadas |

Para escolher um perfil:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=<perfil>
```

> Em ambientes com credenciais reais (`dev`/produção), a validação de CPF e o upload no Cloudinary permanecem ativos por padrão.
