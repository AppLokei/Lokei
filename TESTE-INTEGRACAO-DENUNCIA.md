# Teste de Integração — Criação de Denúncia de Anúncio

## 1. Objetivo
Validar a funcionalidade **criar denúncia de um anúncio** de ponta a ponta, exercitando o fluxo HTTP real contra o contexto Spring completo e um banco de dados em memória. Diferente do teste unitário, aqui passam por: controller → segurança/JWT → serviço → repositórios → banco.

## 2. Funcionalidade testada
- **Endpoint principal:** `POST /anuncios/{id}/denuncias`
- **Fluxo exercitado:**
  1. Registrar um **locador** e um **locatário** (`POST /auth/register`);
  2. Fazer upload de imagem e publicar um anúncio (`POST /anuncios`);
  3. **Criar a denúncia** como locatário (funcionalidade sob teste);
  4. Conferir que a denúncia aparece no **painel do administrador** (`GET /admin/denuncias`);
  5. Garantir que um usuário comum **não** acessa o painel (autorização).

## 3. O que foi usado
| Item | Versão / Detalhe |
|------|------------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.0.5 |
| Teste de contexto | `@SpringBootTest` + `@AutoConfigureMockMvc` + `@ActiveProfiles("test")` |
| Cliente HTTP simulado | `MockMvc` (sem subir porta real) |
| Segurança em teste | `spring-security-test` (`SecurityMockMvcRequestPostProcessors.csrf()`, JWT via header `Authorization`) |
| Banco | **H2 2.4.240** em memória, modo PostgreSQL (`jdbc:h2:mem:lokei;MODE=PostgreSQL`) |
| ORM | Hibernate 7.2.7 (`ddl-auto=create-drop`, Flyway desabilitado no perfil de teste) |
| Asserções | AssertJ |
| JSON | `tools.jackson` (`ObjectMapper`, `JsonNode`) |
| Build / runner | Maven (`./mvnw`) + Surefire |

**Tipo de teste:** integração — usa o perfil `test` (H2 em memória), recriado a cada execução. Reaproveita a infraestrutura de `AbstractIntegrationTest` (helpers `registrarUsuario`, `login`, `uploadImagem`, `bearer`, `readJson`).

## 4. Arquivo
`src/test/java/Lokei/aplication/integration/DenunciaCriacaoIntegrationTest.java`
(estende `Lokei.aplication.integration.AbstractIntegrationTest`)

## 5. Passo a passo (o que o teste faz)
1. `registrarUsuario("LOCADOR", ...)` e `registrarUsuario("LOCATARIO", ...)` → obtém tokens JWT.
2. `uploadImagem(tokenLocador, "furadeira.jpg")` → obtém `imagemId`.
3. `POST /anuncios` (como locador) com `titulo`, `descricao`, `valorDiario`, `categoria`, `imagemIds` → espera **201 Created** e captura o `id` do anúncio.
4. `POST /anuncios/{id}/denuncias` (como locatário) com `motivo=ANUNCIO_FALSO`, `descricao` e uma imagem → espera **201 Created**.
5. Asserções no corpo da denúncia: `status=EM_ANALISE`, `motivo=ANUNCIO_FALSO`, `anuncioId` igual ao criado, `denunciante` não vazio, `imagens` com 1 item.
6. `login("admin@lokei.local", "Admin1234")` → token de admin (usuário administrador semeado no perfil de teste).
7. `GET /admin/denuncias` (como admin) → **200 OK**; verifica que a denúncia recém-criada está na lista com status `EM_ANALISE`.
8. `GET /admin/denuncias` (como locatário) → **403 Forbidden** (regra de autorização).

## 6. Cenário coberto
| # | Cenário | Resultado esperado |
|---|---------|--------------------|
| 1 | `deveCriarDenunciaDeAnuncioEDisponibilizarParaModeracao` | Denúncia criada (201) e persistida com `EM_ANALISE`; visível no painel do admin; usuário comum bloqueado (403) no painel |

## 7. Comando executado
```bash
./mvnw -o test -Dtest=DenunciaCriacaoIntegrationTest
```

## 8. Resultado
```
... Starting DenunciaCriacaoIntegrationTest ... The following 1 profile is active: "test"
... Database JDBC URL [jdbc:h2:mem:lokei]  driver: H2 JDBC Driver  dialect: H2Dialect
... Started DenunciaCriacaoIntegrationTest in 3.985 seconds
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 5.391 s -- in Lokei.aplication.integration.DenunciaCriacaoIntegrationTest
[INFO] Results:
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
- **Total:** 1 teste (cobrindo um fluxo com várias chamadas HTTP)
- **Sucesso:** 1
- **Falhas/erros:** 0
- **Tempo:** ~5,4 s (teste) — inclui subir o contexto Spring + H2
- **Relatório Surefire:** `target/surefire-reports/Lokei.aplication.integration.DenunciaCriacaoIntegrationTest.txt` (e `.xml`)

## 9. Observações / pontos extras
- O perfil `test` usa **H2 em memória no modo PostgreSQL** e `create-drop`, então cada execução parte de um banco limpo e isolado — **não toca no Postgres real** (`Lokei_db`).
- O teste valida camadas que o unitário não cobre: **roteamento HTTP, serialização JSON, autenticação JWT, autorização por papel, persistência real (incluindo a tabela `denuncia_imagens`) e o status inicial gravado no banco**.
- O administrador (`admin@lokei.local` / `Admin1234`) é um usuário semeado no perfil de teste, usado para validar o painel de moderação.
- A verificação do **403** para usuário comum documenta a regra de acesso ao painel administrativo.
- Como diferença em relação ao ambiente real: aqui o storage de imagem e o banco são de teste; o objetivo é validar o **comportamento**, não a infraestrutura de produção.
- Complementa o teste unitário (`TESTE-UNITARIO-DENUNCIA.md`): juntos cobrem regra de negócio isolada **e** o caminho completo da requisição.
