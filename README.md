````document
# Financial Management API

REST API para gerenciamento financeiro, desenvolvida com **Java e Spring Boot**.

O projeto tem como objetivo fornecer uma API para gerenciamento de usuários e informações financeiras, aplicando conceitos utilizados no desenvolvimento de aplicações backend modernas, como arquitetura em camadas, persistência de dados, validação, segurança, testes automatizados e containerização com Docker.

---

## 📋 Sobre o Projeto

O **Financial Management API** é um projeto backend desenvolvido com foco em aprendizado prático e aplicação de conceitos utilizados no mercado de desenvolvimento de software.

A aplicação utiliza o **Spring Boot** como framework principal e **PostgreSQL** como banco de dados.

O ambiente de desenvolvimento é containerizado utilizando **Docker e Docker Compose**, facilitando a configuração e execução da aplicação e seus serviços.

### 🎯 Objetivos

- Desenvolver uma API REST utilizando Java e Spring Boot.
- Aplicar arquitetura baseada em camadas.
- Trabalhar com persistência utilizando JPA e Hibernate.
- Implementar autenticação e autorização.
- Aplicar validação de dados.
- Criar testes automatizados.
- Utilizar Docker para padronizar o ambiente.
- Documentar a API utilizando Swagger/OpenAPI.
- Aplicar boas práticas de desenvolvimento backend.
- Construir um projeto para portfólio profissional.

---

# 🚀 Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem de programação |
| Spring Boot | Framework principal |
| Spring Security | Segurança, autenticação e autorização |
| Spring Data JPA | Persistência de dados |
| Hibernate | ORM |
| PostgreSQL | Banco de dados relacional |
| Maven | Gerenciamento de dependências e build |
| Docker | Containerização |
| Docker Compose | Orquestração dos containers |
| JUnit | Testes automatizados |
| Mockito | Testes unitários e mocks |
| Swagger / OpenAPI | Documentação da API |
| Git | Controle de versão |
| GitHub | Hospedagem do código |

---

# 🏗️ Arquitetura

O projeto utiliza uma arquitetura baseada em camadas, separando as responsabilidades de cada componente da aplicação.

```text
                CLIENT
                   │
                   ▼
              CONTROLLER
                   │
                   ▼
                SERVICE
                   │
                   ▼
              REPOSITORY
                   │
                   ▼
               DATABASE
````

### Controller

Responsável por receber as requisições HTTP, encaminhar os dados para a camada de serviço e retornar as respostas da API.

### Service

Responsável pela implementação das regras de negócio da aplicação.

### Repository

Responsável pela comunicação com o banco de dados utilizando Spring Data JPA.

### Entity

Representa as entidades persistidas no banco de dados.

### DTO

Responsável por definir os dados utilizados na comunicação entre a API e o cliente, evitando a exposição direta das entidades em determinados fluxos.

---

# ⚙️ Funcionalidades

## 👤 Usuários

* Cadastro de usuários
* Persistência de usuários
* Validação dos dados
* Autenticação
* Autorização

## 💰 Gestão Financeira

O projeto foi estruturado para trabalhar com informações financeiras, incluindo:

* Entradas financeiras
* Saídas financeiras
* Valores
* Datas
* Categorias
* Transações financeiras

---

# 🗄️ Banco de Dados

O projeto utiliza **PostgreSQL** como banco de dados relacional.

### Banco utilizado

```text
financial_management
```

A aplicação realiza a comunicação com o PostgreSQL através do **Spring Data JPA** e **Hibernate**.

---

# 🐳 Docker

O projeto possui um ambiente Docker composto pela API, PostgreSQL e pgAdmin.

```text
┌────────────────────────────────────────────┐
│                  Docker                    │
│                                            │
│  ┌──────────────┐                          │
│  │     API      │                          │
│  │   Port 8080  │                          │
│  └───────┬──────┘                          │
│          │                                 │
│          ▼                                 │
│  ┌──────────────┐                          │
│  │  PostgreSQL  │                          │
│  │   Port 5432  │                          │
│  └──────────────┘                          │
│                                            │
│  ┌──────────────┐                          │
│  │   pgAdmin    │                          │
│  │   Port 5050  │                          │
│  └──────────────┘                          │
│                                            │
└────────────────────────────────────────────┘
```

### Serviços

| Serviço    | Porta |
| ---------- | ----: |
| API        |  8080 |
| PostgreSQL |  5432 |
| pgAdmin    |  5050 |

---

# 🔐 Variáveis de Ambiente

As informações sensíveis não devem ser armazenadas diretamente no código-fonte.

O projeto utiliza variáveis de ambiente para configurar informações como:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
```

O arquivo `.env` é ignorado pelo Git e **não deve ser enviado para o GitHub**.

### Exemplo

Crie um arquivo `.env` na raiz do projeto:

```env
POSTGRES_DB=financial_management
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha

DB_URL=jdbc:postgresql://postgres:5432/financial_management
DB_USERNAME=postgres
DB_PASSWORD=sua_senha
```

> **Importante:** nunca coloque senhas, tokens, chaves privadas ou outras credenciais reais no repositório.

---

# 📦 Pré-requisitos

Para executar o projeto, é necessário ter instalado:

* Java 21
* Docker
* Docker Compose
* Git

O projeto também utiliza o **Maven Wrapper**, portanto não é obrigatório instalar o Maven globalmente.

---

# ▶️ Executando o Projeto

## 1. Clonar o repositório

```bash
git clone https://github.com/YgorAdaltoFonseca/financial-management-api.git
```

Entre na pasta do projeto:

```bash
cd financial-management-api
```

---

## 2. Configurar as variáveis de ambiente

Crie um arquivo `.env` na raiz do projeto:

```text
.env
```

Configure as variáveis necessárias de acordo com o ambiente local.

---

## 3. Executar com Docker

Para construir as imagens e iniciar todos os serviços:

```bash
docker compose up -d --build
```

---

## 4. Verificar os containers

```bash
docker compose ps
```

Os principais serviços esperados são:

```text
financial-management-api
financial-management-db
financial-management-pgadmin
```

---

# 🐳 Comandos Docker

### Iniciar os serviços

```bash
docker compose up -d
```

### Construir novamente a aplicação

```bash
docker compose up -d --build
```

### Verificar os containers

```bash
docker compose ps
```

### Ver logs da API

```bash
docker logs financial-management-api
```

### Ver as últimas linhas dos logs

```bash
docker logs financial-management-api --tail 50
```

### Parar os serviços

```bash
docker compose down
```

---

# 💻 Executando pelo IntelliJ IDEA

Durante o desenvolvimento, a API também pode ser executada diretamente pelo **IntelliJ IDEA**.

O PostgreSQL e o pgAdmin podem continuar sendo executados pelo Docker.

```bash
docker compose up -d postgres pgadmin
```

Depois, execute a classe principal da aplicação:

```text
FinancialManagementApiApplication
```

A API ficará disponível em:

```text
http://localhost:8080
```

---

# 📖 Swagger / OpenAPI

A documentação da API pode ser acessada através do Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

O Swagger permite:

* Visualizar os endpoints disponíveis.
* Consultar os parâmetros das requisições.
* Visualizar os modelos de dados.
* Testar os endpoints diretamente pelo navegador.

---

# 🗃️ pgAdmin

O **pgAdmin** é utilizado para administrar o banco de dados PostgreSQL.

Acesse:

```text
http://localhost:5050
```

### Configuração do servidor

Ao conectar o pgAdmin ao PostgreSQL dentro do Docker, utilize:

```text
Host: postgres
Port: 5432
Database: financial_management
Username: postgres
```

As credenciais devem ser obtidas através das variáveis de ambiente configuradas localmente.

---

# 🧪 Testes

O projeto utiliza **JUnit** e **Mockito** para testes automatizados.

Os testes têm como objetivo validar o comportamento das regras de negócio e dos componentes da aplicação.

### Windows

```bash
mvnw.cmd test
```

### Linux / macOS

```bash
./mvnw test
```

---

# 🔨 Build

Para gerar o build da aplicação:

### Windows

```bash
mvnw.cmd clean package
```

### Linux / macOS

```bash
./mvnw clean package
```

---

# 📁 Estrutura do Projeto

```text
financial-management-api/
│
├── docs/
│   ├── DiagramaClasses.png
│   └── DiagramaRelacionamentoClasse.png
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── side/
│   │   │       └── financialmanagementapi/
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/
│
├── .gitignore
├── Dockerfile
├── compose.yaml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── Requirements
```

---

# 🔒 Segurança

A aplicação utiliza **Spring Security** para proteger os recursos da API.

Informações sensíveis devem ser fornecidas através de variáveis de ambiente.

As seguintes informações **não devem ser armazenadas no Git**:

* Senhas
* Tokens
* API Keys
* Credenciais do banco de dados
* Chaves privadas
* Arquivos `.env`

O arquivo `.gitignore` possui regras para impedir o envio dessas informações para o repositório.

---

# 🛠️ Desenvolvimento

O projeto aplica práticas de desenvolvimento backend, incluindo:

* Separação de responsabilidades
* Arquitetura em camadas
* DTOs
* Validação de dados
* Persistência com JPA
* Hibernate
* Tratamento de exceções
* Testes automatizados
* Containerização
* Controle de versão com Git
* Documentação com Swagger/OpenAPI

---

# 🗺️ Roadmap

Funcionalidades planejadas para evolução do projeto:

* [ ] Finalizar cadastro de usuários
* [ ] Implementar autenticação completa
* [ ] Implementar JWT
* [ ] Implementar CRUD de transações
* [ ] Implementar categorias financeiras
* [ ] Implementar filtros de transações
* [ ] Implementar paginação
* [ ] Implementar relatórios financeiros
* [ ] Aumentar cobertura de testes
* [ ] Criar testes de integração
* [ ] Utilizar Testcontainers
* [ ] Configurar CI/CD
* [ ] Deploy em ambiente de produção
* [ ] Implementar monitoramento da aplicação
* [ ] Melhorar mecanismos de segurança

---

# 📚 Documentação

Os diagramas e documentos relacionados ao projeto estão disponíveis no diretório:

```text
docs/
```

Atualmente, o projeto possui diagramas relacionados a:

* Diagrama de classes
* Diagrama de relacionamento entre classes

---

# 🔀 Controle de Versão

O projeto utiliza **Git** para controle de versão.

Exemplo de fluxo básico de desenvolvimento:

```bash
git status

git add .

git commit -m "feat: implement user registration"

git push
```

---

# 👨‍💻 Autor

## Ygor Adalto Fonseca

Desenvolvedor Java Backend em formação, com foco em desenvolvimento de APIs REST, Spring Boot, bancos de dados e arquitetura de software.

### GitHub

https://github.com/YgorAdaltoFonseca

---

# 📄 Licença

Este projeto está em desenvolvimento e é destinado principalmente para fins de **estudo, portfólio e evolução profissional**.
:::
