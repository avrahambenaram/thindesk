# Thindesk

Microserviço para gerenciamento de chamados

## Tecnologias utilizadas

- **Java 23** — linguagem e ambiente de execução.
- **Spring Boot** — estrutura principal da aplicação.
- **Spring Web** — controllers e rotas HTTP.
- **Spring Security** — autenticação e autorização.
- **Spring OpenAPI** - Documentação dos endpoints REST.
- **Spring Data MongoDB** e **MongoDB** — persistência dos dados.
- **Lombok** — redução de código repetitivo.
- **Maven Wrapper** — execução do Maven sem instalação global do Maven.

## Pré-requisitos

- JDK 23.
- MongoDB local ou uma instância acessível do MongoDB Atlas.

## 1. Clone o repositório

```bash
git clone https://github.com/avrahambenaram/thindesk.git
cd thindesk
```

## 2. Configurar o arquivo `.env`

Copie `.env.example` para `.env`, na raiz do projeto.

### Linux

```bash
cp .env.example .env
```

### Windows — PowerShell

```powershell
Copy-Item .env.example .env
```

### Windows — Prompt de Comando (CMD)

```cmd
copy .env.example .env
```

Edite o `.env`. O arquivo de exemplo contém as variável `MONGODB_URI`. Para uma instância local do MongoDB sem autenticação, por exemplo:

```dotenv
MONGODB_URI=mongodb://localhost:27017/thindesk
```

Ajuste a URI conforme a configuração do seu banco.

### MongoDB local com autenticação

Se o MongoDB exigir usuário e senha, use o formato:

```dotenv
MONGODB_URI=mongodb://USUARIO:SENHA@localhost:27017/thindesk?authSource=admin
```

Substitua `USUARIO` e `SENHA` pelas credenciais do banco. Se a senha tiver caracteres especiais, codifique-os para uso em uma URI.

### MongoDB Atlas

Copie a URI de conexão fornecida pelo painel do Atlas e ajuste as credenciais e o banco:

```dotenv
MONGODB_URI=mongodb+srv://USUARIO:SENHA@SEU_CLUSTER.mongodb.net/pediupatrao
```

No Atlas, confira também se o endereço IP de onde a aplicação será executada está autorizado nas configurações de rede.

### Usuário padrão

Também é necessário editar no `.env` o usuário admin padrão que será criado para acesso do sistema:
```dotenv
ADMIN_USERNAME=admin
ADMIN_PASSWORD=password
```

## 3. Usuário e senha iniciais da aplicação

Na configuração de desenvolvimento atual, as credenciais iniciais de acesso à aplicação são:

| Campo | Valor |
|---|---|
| Usuário | `user` |
| Senha | `password` |

Essas credenciais são para entrar na aplicação.

## 4. Executar a aplicação

Execute os comandos na raiz do projeto, onde estão `pom.xml` e o Maven Wrapper.

### Linux

Se necessário, dê permissão de execução ao wrapper:

```bash
chmod +x mvnw
```

Inicie a aplicação:

```bash
./mvnw spring-boot:run
```

### Windows — PowerShell

```powershell
.\mvnw.cmd spring-boot:run
```

### Windows — Prompt de Comando (CMD)

```cmd
mvnw.cmd spring-boot:run
```

Quando a aplicação iniciar, acesse:

**http://localhost:8080**

### Executar com Docker

A imagem não contém credenciais: a configuração vem do arquivo `.env` ou das variáveis de ambiente do sistema (`MONGODB_URI`, `ADMIN_USERNAME`, `ADMIN_PASSWORD` e, opcionalmente, `PORT`). A aplicação escuta na porta definida em `PORT` (padrão `8080`).

Com Docker Compose (lê o `.env` da raiz ou o ambiente e publica a mesma porta `PORT` no host):

```bash
docker compose up --build
```

Com variáveis de ambiente do sistema:

```bash
MONGODB_URI="mongodb+srv://..." ADMIN_USERNAME=admin ADMIN_PASSWORD=senha PORT=9090 docker compose up --build
```

Sem Compose, montando o `.env` dentro do container:

```bash
docker build -t thindesk .
docker run --rm -p 8080:8080 -v "$(pwd)/.env:/app/.env:ro" thindesk
```

> Se `PORT` for diferente de 8080, publique a mesma porta (`-p 9090:9090`). Evite `--env-file` quando houver valores entre aspas no `.env`, pois o Docker mantém as aspas; prefira o Compose ou o volume acima.

## 5. Painel administrativo (dashboard)

Tela exclusiva do perfil **ADMIN**, disponível em `/dashboard` e no menu lateral como **Painel**. Ela mostra, em tempo real, a visão geral do sistema:

- total de chamados, abertos, em andamento e resolvidos;
- taxa de resolução (com barra de progresso);
- total de clientes e de técnicos.

Usuários sem o perfil ADMIN não veem o link no menu e recebem acesso negado se tentarem abrir a rota diretamente.

![Painel administrativo](docs/painel.png)

## 6. Versionamento

O projeto segue o **Versionamento Semântico** ([semver.org](https://semver.org/lang/pt-BR/)), no formato:

```
MAJOR.MINOR.PATCH-ESTÁGIO
```

| Parte | Quando incrementar | Exemplo |
|---|---|---|
| `MAJOR` | mudanças incompatíveis (ex.: alterar o formato da API ou do banco de forma que quebre quem já usa) | `1.x.x` → `2.0.0` |
| `MINOR` | nova funcionalidade compatível com a versão anterior | `1.0.0` → `1.1.0` |
| `PATCH` | correção de bug, sem nova funcionalidade | `1.1.0` → `1.1.1` |
| `ESTÁGIO` | maturidade da versão: `alpha` (em desenvolvimento, instável), `beta` (funcional, em testes), `rc` (candidata a lançamento). Sem sufixo = versão estável | `1.1.0-alpha` → `1.1.0-beta` → `1.1.0` |

**Versão atual: `1.1.0-alpha`** (definida no `pom.xml`).

Histórico resumido:

| Versão | Conteúdo |
|---|---|
| `1.0.0-alpha` | cadastros (clientes, técnicos, tipos de chamado, recursos, horários), abertura e resolução de chamados com histórico, perfis de acesso (ADMIN, TECNICO, USUARIO) |
| `1.1.0-alpha` | painel administrativo, técnico coloca chamado em andamento, admin edita e cancela chamados |

Os commits seguem o padrão de uma linha com prefixo: `feat:` (nova funcionalidade → sobe o MINOR), `fix:` (correção → sobe o PATCH) e `chore:` (manutenção, sem alterar comportamento).
