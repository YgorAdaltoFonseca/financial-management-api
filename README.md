\# Financial Management API



REST API para gerenciamento financeiro, desenvolvida com Java e Spring Boot.



O projeto tem como objetivo fornecer uma API para gerenciamento de usuários e informações financeiras, utilizando uma arquitetura organizada em camadas, persistência de dados com PostgreSQL, autenticação e autorização, testes automatizados e ambiente containerizado com Docker.



\---



\## Sobre o Projeto



O \*\*Financial Management API\*\* é um projeto backend desenvolvido para praticar e aplicar conceitos utilizados no desenvolvimento de APIs REST modernas com Java.



A aplicação utiliza o Spring Boot como framework principal e PostgreSQL como banco de dados.



O projeto também utiliza Docker para facilitar a configuração e execução do ambiente de desenvolvimento.



\### Objetivos



\- Desenvolver uma API REST utilizando Java e Spring Boot.

\- Aplicar conceitos de arquitetura em camadas.

\- Trabalhar com persistência de dados utilizando JPA e Hibernate.

\- Implementar autenticação e autorização.

\- Criar testes automatizados.

\- Utilizar Docker para padronizar o ambiente.

\- Documentar a API utilizando Swagger/OpenAPI.

\- Aplicar boas práticas de desenvolvimento backend.



\---



\# Tecnologias



| Tecnologia | Utilização |

|---|---|

| Java 21 | Linguagem de programação |

| Spring Boot | Framework principal |

| Spring Security | Segurança e autenticação |

| Spring Data JPA | Persistência de dados |

| Hibernate | ORM |

| PostgreSQL | Banco de dados |

| Maven | Gerenciamento de dependências e build |

| Docker | Containerização |

| Docker Compose | Orquestração dos containers |

| JUnit | Testes automatizados |

| Mockito | Testes unitários e mocks |

| Swagger / OpenAPI | Documentação da API |

| Git | Controle de versão |

| GitHub | Hospedagem do código |



\---



\# Arquitetura



O projeto utiliza uma arquitetura baseada em camadas.



```text

&#x20;               CLIENT

&#x20;                  │

&#x20;                  ▼

&#x20;             CONTROLLER

&#x20;                  │

&#x20;                  ▼

&#x20;               SERVICE

&#x20;                  │

&#x20;                  ▼

&#x20;             REPOSITORY

&#x20;                  │

&#x20;                  ▼

&#x20;              DATABASE

Controller



Responsável por receber as requisições HTTP e retornar as respostas da API.



Service



Responsável pela implementação das regras de negócio da aplicação.



Repository



Responsável pela comunicação com o banco de dados utilizando Spring Data JPA.



Entity



Representa as entidades persistidas no banco de dados.



DTO



Responsável por definir os dados utilizados na comunicação entre a API e o cliente.



Funcionalidades

Usuários

Cadastro de usuários

Persistência de usuários

Validação dos dados

Autenticação e autorização

Gestão Financeira



O projeto foi estruturado para trabalhar com informações financeiras, incluindo:



Entradas financeiras

Saídas financeiras

Valores

Datas

Categorias

Transações financeiras

Banco de Dados



O projeto utiliza PostgreSQL.



O banco utilizado pela aplicação é:



financial\_management



A aplicação se conecta ao PostgreSQL através do Spring Data JPA e Hibernate.



Docker



O projeto possui um ambiente Docker composto pelos seguintes serviços:



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

Serviços

Serviço	Porta

API	8080

PostgreSQL	5432

pgAdmin	5050

Variáveis de Ambiente



As informações sensíveis não são armazenadas diretamente no código-fonte.



O projeto utiliza variáveis de ambiente para informações como:



DB\_URL

DB\_USERNAME

DB\_PASSWORD

POSTGRES\_DB

POSTGRES\_USER

POSTGRES\_PASSWORD



O arquivo .env é ignorado pelo Git e não deve ser enviado para o GitHub.



Exemplo:



POSTGRES\_DB=financial\_management

POSTGRES\_USER=postgres

POSTGRES\_PASSWORD=sua\_senha



DB\_URL=jdbc:postgresql://postgres:5432/financial\_management

DB\_USERNAME=postgres

DB\_PASSWORD=sua\_senha



Nunca coloque senhas, tokens, chaves privadas ou outras credenciais reais no repositório.



Pré-requisitos



Para executar o projeto, é necessário ter instalado:



Java 21

Docker

Docker Compose

Git

Executando o Projeto

1\. Clonar o repositório

git clone https://github.com/SEU-USUARIO/financial-management-api.git



Entrar na pasta:



cd financial-management-api

2\. Configurar as variáveis de ambiente



Crie um arquivo:



.env



na raiz do projeto.



Configure as variáveis necessárias.



3\. Executar com Docker



Para construir as imagens e iniciar todos os serviços:



docker compose up -d --build

4\. Verificar os containers

docker compose ps



Os serviços esperados são:



financial-management-api

financial-management-db

financial-management-pgadmin

Comandos Docker

Iniciar os serviços

docker compose up -d

Construir novamente a aplicação

docker compose up -d --build

Ver containers

docker compose ps

Ver logs da API

docker logs financial-management-api

Ver últimas linhas dos logs

docker logs financial-management-api --tail 50

Parar os serviços

docker compose down

Parar e remover os containers

docker compose down

Executando pelo IntelliJ IDEA



Durante o desenvolvimento, a API também pode ser executada diretamente pelo IntelliJ IDEA.



O banco PostgreSQL pode continuar sendo executado pelo Docker:



docker compose up -d postgres pgadmin



Depois, execute a classe principal da aplicação:



FinancialManagementApiApplication



A API ficará disponível em:



http://localhost:8080

Swagger



A documentação da API pode ser acessada através do Swagger UI:



http://localhost:8080/swagger-ui/index.html



O Swagger permite visualizar e testar os endpoints da API diretamente pelo navegador.



pgAdmin



O pgAdmin é utilizado para administrar o banco PostgreSQL.



Acesse:



http://localhost:5050



O servidor PostgreSQL utilizado pelo pgAdmin dentro do Docker é:



Host: postgres

Port: 5432

Database: financial\_management

Username: postgres



As credenciais devem ser obtidas através das variáveis de ambiente configuradas localmente.



Testes



O projeto utiliza JUnit e Mockito para testes automatizados.



Os testes têm como objetivo validar o comportamento das regras de negócio e dos componentes da aplicação.



Executar os testes no Windows

mvnw.cmd test

Executar os testes com Maven Wrapper

./mvnw test

Build



Para gerar o build da aplicação:



Windows

mvnw.cmd clean package

Linux / macOS

./mvnw clean package

Estrutura do Projeto

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

Segurança



A aplicação utiliza Spring Security para proteger os recursos da API.



Informações sensíveis devem ser fornecidas através de variáveis de ambiente.



O projeto não deve armazenar no Git:



Senhas

Tokens

API Keys

Credenciais do banco de dados

Chaves privadas

Arquivos .env



O arquivo .gitignore possui regras para impedir o envio dessas informações para o repositório.



Desenvolvimento



O projeto está sendo desenvolvido utilizando práticas de desenvolvimento backend, incluindo:



Separação de responsabilidades

Arquitetura em camadas

DTOs

Validação de dados

Persistência com JPA

Tratamento de exceções

Testes automatizados

Containerização

Controle de versão com Git

Roadmap



Funcionalidades planejadas para evolução do projeto:



&#x20;Finalizar cadastro de usuários

&#x20;Implementar autenticação completa

&#x20;Implementar JWT

&#x20;Implementar CRUD de transações

&#x20;Implementar categorias financeiras

&#x20;Implementar filtros de transações

&#x20;Implementar paginação

&#x20;Implementar relatórios financeiros

&#x20;Aumentar cobertura de testes

&#x20;Criar testes de integração

&#x20;Utilizar Testcontainers

&#x20;Configurar CI/CD

&#x20;Deploy em ambiente de produção

&#x20;Monitoramento da aplicação

&#x20;Melhorias de segurança

Documentação



Os diagramas e documentos relacionados ao projeto estão disponíveis no diretório:



docs/



Atualmente o projeto possui diagramas de:



Classes

Relacionamento entre classes

Controle de Versão



O projeto utiliza Git para controle de versão.



Exemplo de fluxo de desenvolvimento:



git status



git add .



git commit -m "feat: implement user registration"



git push

Autor

Ygor Adalto Fonseca



Desenvolvedor Java Backend em formação, com foco em desenvolvimento de APIs REST, Spring Boot, bancos de dados e arquitetura de software.



GitHub:



https://github.com/YgorAdaltoFonseca



Licença



Este projeto está em desenvolvimento e é destinado principalmente para fins de estudo, portfólio e evolução profissional.

