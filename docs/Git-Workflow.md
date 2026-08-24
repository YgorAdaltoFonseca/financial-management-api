\## 🔄 Fluxo padrão para toda nova funcionalidade



Imagine que você vai implementar uma nova funcionalidade, por exemplo:



> `Cadastro de transações financeiras`



\### 1. Verifique em qual branch você está



```bash

git branch

```



A branch marcada com `\*` é a atual.



Depois:



```bash

git status

```



Isso mostra se existem alterações pendentes.



\---



\### 2. Atualize seu projeto antes de começar



Se estiver trabalhando na `master`:



```bash

git pull origin master

```



Isso traz as alterações mais recentes do GitHub.



\---



\### 3. Crie uma branch para a funcionalidade



\*\*Não faça tudo diretamente na `master`.\*\*



Exemplo:



```bash

git checkout -b feature/cadastro-transacao

```



Agora você está trabalhando na:



```text

feature/cadastro-transacao

```



Confira:



```bash

git branch

```



\---



\# 💻 4. Desenvolva a funcionalidade



Agora você trabalha normalmente no IntelliJ.



Por exemplo:



```text

Controller

Service

Repository

Entity

DTO

Tests

```



Implemente e teste a funcionalidade.



\---



\# 🔎 5. Verifique o que foi alterado



Depois de terminar uma parte:



```bash

git status

```



Para visualizar as alterações:



```bash

git diff

```



\---



\# ➕ 6. Adicione os arquivos ao Git



Se estiver tudo certo:



```bash

git add .

```



Depois confira:



```bash

git status

```



Os arquivos deverão aparecer como:



```text

Changes to be committed

```



\---



\# 💾 7. Faça o commit



Use uma mensagem que explique \*\*o que foi feito\*\*.



Exemplo:



```bash

git commit -m "feat: implement transaction registration"

```



Alguns padrões úteis:



```text

feat: nova funcionalidade



fix: correção de bug



refactor: alteração estrutural sem mudar comportamento



test: criação ou alteração de testes



docs: alteração de documentação



chore: configuração/manutenção do projeto

```



Exemplos:



```bash

git commit -m "feat: implement transaction registration"

```



```bash

git commit -m "fix: validate transaction amount"

```



```bash

git commit -m "test: add transaction service tests"

```



```bash

git commit -m "docs: update README"

```



\---



\# ☁️ 8. Envie a branch para o GitHub



```bash

git push -u origin feature/cadastro-transacao

```



O `-u` conecta sua branch local à branch remota.



Nas próximas vezes, nessa mesma branch, basta:



```bash

git push

```



\---



\# 🔀 9. Faça o Merge



Agora você terá no GitHub algo parecido com:



```text

master

&#x20;  │

&#x20;  └── feature/cadastro-transacao

```



No GitHub, abra um \*\*Pull Request\*\*:



```text

feature/cadastro-transacao

&#x20;             ↓

&#x20;          master

```



Revise as alterações e faça o merge.



\---



\# 🧹 10. Depois do Merge



Volte para sua máquina:



```bash

git checkout master

```



Atualize a `master`:



```bash

git pull origin master

```



Agora você pode apagar a branch antiga:



```bash

git branch -d feature/cadastro-transacao

```



E apagar a branch remota:



```bash

git push origin --delete feature/cadastro-transacao

```



\---



\# 🔁 Seu ciclo completo



Para praticamente \*\*toda nova funcionalidade\*\*, siga:



```bash

git checkout master



git pull origin master



git checkout -b feature/nome-da-funcionalidade

```



↓



\*\*Desenvolva\*\*



↓



```bash

git status



git diff



git add .



git commit -m "feat: descricao da funcionalidade"



git push -u origin feature/nome-da-funcionalidade

```



↓



\*\*Pull Request no GitHub\*\*



↓



\*\*Merge\*\*



↓



```bash

git checkout master



git pull origin master



git branch -d feature/nome-da-funcionalidade

```



\---



\# 🐛 E quando encontrar um bug?



Use uma branch `fix`:



```bash

git checkout master

git pull origin master



git checkout -b fix/corrigir-cadastro-usuario

```



Faça a correção:



```bash

git add .



git commit -m "fix: correct user registration validation"



git push -u origin fix/corrigir-cadastro-usuario

```



Depois:



\*\*Pull Request → Merge → voltar para master → pull.\*\*



\---



\# 📝 E quando alterar somente o README?



Não precisa criar uma branch obrigatoriamente em um projeto pessoal pequeno, mas eu recomendo começar a praticar o fluxo correto:



```bash

git checkout master

git pull origin master



git checkout -b docs/update-readme

```



Edite o README e depois:



```bash

git add README.md



git commit -m "docs: update README"



git push -u origin docs/update-readme

```



Depois faça o Pull Request.



\---



\# 🚨 Comando mais importante quando você estiver perdido



Se você não souber o que está acontecendo:



```bash

git status

```



\*\*Não saia executando `git reset`, `git checkout`, `git clean` ou comandos destrutivos sem entender o que eles vão fazer.\*\*



O `git status` é praticamente seu "painel de controle".



\---



\## 📌 Coloque isso no seu README



Você pode adicionar uma seção como:



````markdown

\# 🔄 Fluxo de Desenvolvimento



Para desenvolver uma nova funcionalidade:



```bash

\# 1. Atualizar a branch principal

git checkout master

git pull origin master



\# 2. Criar uma branch para a funcionalidade

git checkout -b feature/nome-da-funcionalidade



\# 3. Desenvolver e testar a funcionalidade



\# 4. Verificar alterações

git status

git diff



\# 5. Adicionar alterações

git add .



\# 6. Criar commit

git commit -m "feat: descricao da funcionalidade"



\# 7. Enviar branch para o GitHub

git push -u origin feature/nome-da-funcionalidade



\# 8. Criar Pull Request no GitHub



\# 9. Depois do merge, atualizar a master

git checkout master

git pull origin master



\# 10. Remover branch local

git branch -d feature/nome-da-funcionalidade

````



````



\### Para o seu nível atual, eu usaria este padrão



```text

master

&#x20; │

&#x20; ├── feature/cadastro-usuario

&#x20; ├── feature/cadastro-transacao

&#x20; ├── feature/autenticacao

&#x20; ├── feature/relatorio-financeiro

&#x20; │

&#x20; ├── fix/corrigir-validacao

&#x20; └── docs/update-readme

````

