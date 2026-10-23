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
