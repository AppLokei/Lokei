# Teste Unitário — Criação de Denúncia de Anúncio

## 1. Objetivo
Validar, de forma isolada, a regra de negócio da funcionalidade **criar denúncia de um anúncio**, implementada em `DenunciaService.criar(...)`. O teste cobre o caminho feliz e os principais caminhos de erro, sem subir o contexto do Spring nem banco de dados.

## 2. Funcionalidade testada
- **Classe sob teste (SUT):** `Lokei.aplication.application.service.DenunciaService`
- **Método:** `DenunciaResponse criar(Integer usuarioId, Integer anuncioId, CriarDenunciaRequest request)`
- **Comportamento esperado:**
  - Buscar o usuário denunciante e o anúncio alvo;
  - Criar a denúncia com status inicial `EM_ANALISE`;
  - Converter o motivo (texto) para o enum `motivoDenunciaEnum` e rejeitar motivos inválidos;
  - Normalizar (trim) a descrição;
  - Persistir e retornar o `DenunciaResponse`;
  - **Não** disparar notificação na criação (isso só ocorre na moderação).

## 3. O que foi usado
| Item | Versão / Detalhe |
|------|------------------|
| Linguagem | Java 21 |
| Framework de teste | JUnit 5 (Jupiter) |
| Mocks | Mockito (`@Mock`, `@InjectMocks`, `MockitoExtension`) |
| Asserções | AssertJ (`assertThat`, `assertThatThrownBy`) |
| Build / runner | Maven (`./mvnw`) + Surefire |
| Dependência | `spring-boot-starter-test` (traz JUnit 5, Mockito e AssertJ) |

**Tipo de teste:** unitário puro — todas as dependências (`DenunciaRepository`, `UsuarioRepository`, `AnuncioService`, `NotificacaoService`) são *mockadas*. Não há I/O, rede ou banco.

## 4. Arquivo
`src/test/java/Lokei/aplication/unit/DenunciaServiceTest.java`

## 5. Passo a passo (como foi montado)
1. Anotar a classe com `@ExtendWith(MockitoExtension.class)`.
2. Declarar os colaboradores como `@Mock` e o `DenunciaService` como `@InjectMocks` (injeção via construtor).
3. Em cada cenário:
   - Preparar os dados (`Usuario`, `Anuncio`) e programar os mocks (`when(...).thenReturn(...)`);
   - Para o caminho feliz, fazer o `denunciaRepository.save(...)` devolver o próprio objeto recebido (`thenAnswer(inv -> inv.getArgument(0))`);
   - Executar `denunciaService.criar(...)`;
   - Verificar o `DenunciaResponse` retornado e, via `ArgumentCaptor`, a entidade efetivamente passada ao `save`.

## 6. Cenários cobertos
| # | Cenário | Entrada | Resultado esperado |
|---|---------|---------|--------------------|
| 1 | `deveCriarDenunciaComStatusEmAnaliseEDescricaoNormalizada` | motivo `ANUNCIO_FALSO`, descrição com espaços nas pontas, imagens `null` | status `EM_ANALISE`, motivo `ANUNCIO_FALSO`, descrição sem espaços, `anuncioId`/`titulo`/`denunciante` corretos, lista de imagens vazia, `save` chamado 1x, nenhuma interação com `NotificacaoService` |
| 2 | `deveRejeitarMotivoInvalido` | motivo `MOTIVO_INEXISTENTE` | lança `RegraDeNegocioException` (mensagem contém "Motivo"); `save` nunca é chamado |
| 3 | `deveFalharQuandoDenuncianteNaoExiste` | `usuarioId` inexistente | lança `RecursoNaoEncontradoException`; `save` nunca é chamado |

## 7. Comando executado
```bash
./mvnw -o test -Dtest=DenunciaServiceTest
```

## 8. Resultado
```
[INFO] Running Lokei.aplication.unit.DenunciaServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.784 s -- in Lokei.aplication.unit.DenunciaServiceTest
[INFO] Results:
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```
- **Total:** 3 testes
- **Sucesso:** 3
- **Falhas/erros:** 0
- **Tempo:** ~0,78 s (só os testes) / ~3 s (build total)
- **Relatório Surefire:** `target/surefire-reports/Lokei.aplication.unit.DenunciaServiceTest.txt` (e `.xml`)

## 9. Observações / pontos extras
- Por ser unitário, é **rápido e determinístico** (sub-segundo) — não depende de Postgres, H2 nem rede.
- O teste protege contra regressões na regra de status inicial (`EM_ANALISE`), na conversão do motivo e na normalização da descrição.
- A asserção `verifyNoInteractions(notificacaoService)` documenta uma regra importante: **criar** denúncia não notifica ninguém; a notificação é responsabilidade da **moderação** (`moderar(...)`), que não faz parte deste teste.
- Limitação natural: por usar mocks, este teste **não** valida o mapeamento JPA, constraints do banco ou a camada HTTP — isso é coberto pelo teste de integração (ver `TESTE-INTEGRACAO-DENUNCIA.md`).
