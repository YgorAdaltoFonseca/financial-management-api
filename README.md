# Financial Management API

API REST e aplicação web para gerenciamento financeiro pessoal. O backend usa Java 21, Spring Boot 4.1.1, Spring Security, JWT e PostgreSQL. O frontend usa React, TypeScript e Vite.

## Funcionalidades

- Cadastro, login JWT, consulta e atualização do próprio perfil e desativação da conta.
- CRUD de categorias, transações e assinaturas recorrentes cadastradas.
- Resumo financeiro histórico e série mensal, sempre limitados ao usuário autenticado.
- Interface responsiva em português, com dashboard, gráficos, filtros e estados de carregamento, erro e lista vazia.
- Senhas armazenadas com BCrypt. O frontend mantém o JWT somente em memória; ao recarregar a página, faça login novamente. O backend não oferece cookie `HttpOnly`.

As frequências de assinatura e a próxima cobrança são apenas dados de cadastro. O backend não gera transações automaticamente. O resumo soma as entradas e despesas existentes, sem incorporar uma projeção de cobranças futuras.

## Requisitos

- JDK 21.
- Docker Desktop e Docker Compose para PostgreSQL e testes com Testcontainers.
- Node.js 20.19+ (ou 22.12+) e pnpm para o frontend.

Use o Maven Wrapper incluído no repositório (`mvnw.cmd` no Windows e `./mvnw` no Linux/macOS).

## Configuração local

1. Copie `.env.example` para `.env` e substitua todos os valores de exemplo por valores locais. Não versione o `.env`.
2. Para gerar um segredo JWT local, use pelo menos 32 bytes aleatórios e configure `JWT_SECRET`.
3. Inicie o banco (e, se desejar, pgAdmin):

```bash
docker compose up -d postgres pgadmin
```

4. Execute o backend com o perfil `dev` (ativo por padrão pelo Maven):

```bash
./mvnw spring-boot:run
```

No Windows, use `mvnw.cmd spring-boot:run`. A API ficará em `http://localhost:8080`; o Swagger UI fica em `/swagger-ui/index.html`.

O Compose publica PostgreSQL em `5432` e pgAdmin em `5050`. O banco usado em testes é efêmero e separado, criado pelo Testcontainers.

## Frontend

```bash
cd frontend
pnpm install
cp .env.example .env
pnpm dev
```

No PowerShell, copie a variável de exemplo com `Copy-Item .env.example .env`. `VITE_API_BASE_URL` aponta para a API e, por padrão, vale `http://localhost:8080`. O backend permite a origem `http://localhost:5173`; defina `CORS_ALLOWED_ORIGIN` com a origem exata usada no navegador. É possível informar uma lista separada por vírgulas.

## Deploy de demonstração

O projeto está preparado para hospedar o frontend React no Vercel e a API Java no Render. O frontend usa `frontend/vercel.json` para que as rotas do React Router funcionem ao atualizar a página. No Vercel, importe o repositório e defina `frontend` como Root Directory; configure `VITE_API_BASE_URL` com a URL pública da API Render.

O arquivo `render.yaml` cria a API no plano gratuito do Render. Configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` com as credenciais de um PostgreSQL externo (por exemplo, Neon), e `CORS_ALLOWED_ORIGIN` com a origem exata `.vercel.app` do frontend. `DB_URL` deve ser JDBC, no formato `jdbc:postgresql://HOST/DATABASE?sslmode=require`. O Render gera `JWT_SECRET` e fornece a variável `PORT`. A API no plano gratuito pode dormir após inatividade, causando demora na primeira chamada. O Postgres gratuito do Render expira após 30 dias; por isso, use banco externo neste arranjo.

Este arranjo é para demonstração. Não registre dados financeiros reais em serviços gratuitos: eles têm limites de recursos, disponibilidade e recuperação. O perfil `prod` usa atualização automática do esquema somente para inicializar a demonstração; antes de uso real, configure migrações versionadas, backups e hospedagem apropriada.

## Endpoints usados pelo frontend

| Método | Endpoint | Recurso |
| --- | --- | --- |
| POST | `/api/user/createUser` | Cadastro (`name`, `email`, `senhaHash`) |
| POST | `/api/user/login` | Login (`email`, `senha`), retorna `{ token }` |
| GET | `/api/user/me` | Perfil do usuário autenticado |
| PUT / DELETE | `/api/user/updateUser`, `/api/user/deleteUser` | Atualizar ou desativar a própria conta |
| GET / POST / PUT / DELETE | `/api/transaction/listTransaction`, `/api/transaction/createTransaction/{categoryId}`, `/api/transaction/updateTransaction/{id}/{categoryId}`, `/api/transaction/delete/{id}` | Transações |
| GET / POST / PUT / DELETE | `/api/category/categoriesList`, `/api/category/createCategory`, `/api/category/updateCategory/{id}`, `/api/category/deleteCategory/{id}` | Categorias |
| GET / POST / PUT / DELETE | `/api/subscription/listSubscriptions`, `/api/subscription/createSubscription/{categoryId}`, `/api/subscription/updateSubscription/{id}/{categoryId}`, `/api/subscription/deleteSubscription/{id}` | Assinaturas |
| GET | `/api/dashboard/total`, `/api/dashboard/monthly` | Totais e dados mensais |

Todos os endpoints acima, exceto cadastro e login, exigem `Authorization: Bearer <token>`. O usuário e a propriedade dos recursos são definidos no backend; o frontend não envia `userId` para autorizar operações.

## Testes e build

```bash
./mvnw -Ptest clean verify
cd frontend
pnpm test
pnpm build
```

O perfil Maven `test` usa PostgreSQL descartável via Testcontainers e requer Docker disponível. Os testes de unidade usam Mockito; os testes de integração exercitam cadastro, login, token válido/inválido/expirado, endpoints protegidos, senha BCrypt e isolamento de transações/categorias entre usuários. Testes de integração não devem apontar para banco de produção.

## Variáveis de ambiente

| Variável | Uso |
| --- | --- |
| `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | Banco no Docker Compose |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Conexão JDBC da API |
| `JWT_SECRET` | Chave HMAC para JWT; não versionar |
| `CORS_ALLOWED_ORIGIN` | Origem ou lista exata de origens do frontend |
| `PGADMIN_DEFAULT_EMAIL`, `PGADMIN_DEFAULT_PASSWORD` | Acesso local ao pgAdmin |
| `VITE_API_BASE_URL` | URL pública da API no frontend |

## Limitações conhecidas

- A autenticação usa JWT em memória no navegador, pois a API não implementa cookies seguros `HttpOnly` nem refresh token.
- O modelo de transação não oferece data informada pelo usuário, filtros por intervalo ou paginação; a data é gerada no backend e a lista retorna todos os registros do usuário.
- Assinaturas não são cobradas automaticamente nem convertidas em transações.
- Não existe endpoint administrativo nem papéis de usuário persistidos no modelo atual.
- `PUT /api/user/updateUser` exige uma senha junto com nome e e-mail, então o formulário de perfil pede uma senha nova.
- Para uso em produção, substitua `ddl-auto=update` por migrações versionadas e mantenha credenciais fora do ambiente de desenvolvimento.
