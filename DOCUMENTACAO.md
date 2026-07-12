# Labor — documentação do projeto

> **Projeto em desenvolvimento**
>
> O sistema está em construção e pode sofrer alterações frequentes em endpoints, regras de negócio, DTOs, entidades, fluxos de tela e estrutura de banco.

## 1. O que é o Labor

O **Labor** é uma aplicação backend em **Java 21** com **Spring Boot**, voltada para fluxo de vagas de emprego entre **empresas** e **candidatos**.

Ele centraliza funcionalidades como:
- autenticação com **JWT**;
- cadastro de empresa, candidato e administrador;
- criação e listagem de vagas;
- curtida/candidatura em vagas;
- seleção de candidatos pela empresa;
- geração de agendas/schedules;
- criação automática de chat entre empresa e candidato selecionado;
- troca de mensagens em tempo real com **WebSocket/STOMP**;
- avaliação entre usuários.

Em resumo, o projeto tenta cobrir o ciclo completo:

1. usuário se cadastra;
2. empresa publica uma vaga;
3. candidato demonstra interesse;
4. empresa seleciona candidatos;
5. o sistema gera schedule;
6. um chat é criado para a comunicação;
7. as partes trocam mensagens;
8. avaliações podem ser registradas.

---

## 2. Stack utilizada

- **Java 21**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Security**
- **Spring Data JPA**
- **Flyway**
- **PostgreSQL**
- **JWT** com `java-jwt`
- **Spring WebSocket / STOMP**
- **Thymeleaf** para a tela de chat
- **SpringDoc OpenAPI / Swagger UI**

---

## 3. Estrutura do projeto

### `br.net.labor.LaborApplication`
Classe principal da aplicação. Também ativa o agendamento com `@EnableScheduling`.

### `config`
Contém a infraestrutura do sistema:
- `SecurityConfig`: regras de acesso e proteção das rotas;
- `SecurityFilter`: interceptação e validação do JWT;
- `TokenService`: geração e validação do token;
- `JWTUserData`: dados do usuário autenticado no contexto de segurança;
- `WebSocketConfig`: configuração do broker e endpoint STOMP.

### `controller`
Camada REST e WebSocket da aplicação. É onde ficam os endpoints expostos para o frontend ou para consumo externo.

### `service`
Contém a regra de negócio principal.

### `repository`
Interfaces do Spring Data JPA para acesso ao banco.

### `model`
Contém as entidades e os DTOs do domínio:
- `user`
- `typeUser`
- `jobs`
- `schedule`
- `candidateApplication`
- `chat`
- `hate`/`rate`
- `dto`

### `resources`
Arquivos de configuração, migrações Flyway e templates.

---

## 4. Perfis e domínio principal

O sistema trabalha com três perfis principais:
- **ADMIN**
- **COMPANY**
- **CANDIDATE**

A entidade base é `User`, que concentra o login, o papel e dados comuns de autenticação.

A partir dela surgem os perfis específicos:
- `Company`
- `Candidate`

Esses perfis têm informações próprias, mas continuam ligados ao usuário base.

---

## 5. Entidades principais

## 5.1 `User`
Tabela: `users`

Responsável por:
- e-mail;
- senha;
- telefone;
- role;
- dados de autenticação do Spring Security.

Também implementa `UserDetails`.

---

## 5.2 `Company`
Tabela: `company`

Representa a empresa cadastrada no sistema.

Possui:
- nome da empresa;
- CNPJ;
- descrição;
- segmento/indústria;
- foto;
- status;
- vínculo com `User`;
- lista de vagas;
- lista de schedules;
- lista de chats.

---

## 5.3 `Candidate`
Tabela: `candidate`

Representa o candidato do sistema.

Possui:
- username;
- CPF;
- data de nascimento;
- foto;
- status;
- nome real;
- vínculo com `User`;
- aplicações/candidaturas;
- schedules;
- chats.

---

## 5.4 `JobVacancies`
Tabela: `job_vacancies`

Representa a vaga publicada pela empresa.

Possui:
- título;
- habilidade desejada;
- valor da remuneração;
- horário inicial;
- horário final;
- data da vaga;
- descrição;
- empresa dona da vaga;
- aplicações de candidatos;
- schedules ligados à vaga.

---

## 5.5 `CandidateApplication`
Tabela: `candidate_applications`

Representa a candidatura/interesse de um candidato em uma vaga.

Possui:
- candidato;
- vaga;
- status da candidatura;
- data/hora da candidatura.

A aplicação controla o fluxo de:
- `APPLIED`
- `SELECTED`
- e outros status definidos no enum `ApplicationStatus`.

---

## 5.6 `Schedule`
Tabela: `schedule`

Representa o agendamento gerado para uma vaga/seleção.

Possui:
- empresa;
- candidato(s) vinculado(s) ao agendamento;
- vaga;
- data de trabalho;
- horário de início;
- horário de fim;
- pagamento.

Esse agendamento é a base para a criação automática do chat entre empresa e candidato.

---

## 5.7 `ChatModel`
Tabela: `chat`

Representa a conversa entre empresa e candidato.

Possui:
- empresa;
- candidato;
- lista de mensagens.

---

## 5.8 `Message`
Tabela: `message`

Representa uma mensagem enviada no chat.

Possui:
- chat;
- conteúdo;
- horário do envio;
- remetente.

---

## 5.9 `Rating`
Tabela: `rating`

Representa a avaliação registrada por um usuário.

Possui:
- nota;
- descrição da avaliação;
- quem enviou a avaliação;
- usuário avaliado.

---

## 6. Fluxos do sistema

## 6.1 Autenticação

O sistema possui login com JWT.

### O que acontece
1. o usuário se cadastra como candidato, empresa ou admin;
2. ele faz login com e-mail e senha;
3. o backend autentica via Spring Security;
4. um JWT é gerado;
5. o token é usado nas requisições protegidas.

### Endpoints
- `POST /api/auth/login`
- `POST /api/auth/enterprise/register`
- `POST /api/auth/candidate/register`
- `POST /api/auth/admin/register`

---

## 6.2 Vagas

A empresa autenticada cria vagas com base no seu usuário logado.

### O que o sistema faz
- valida se a empresa está autenticada;
- busca a empresa pelo e-mail do usuário logado;
- cria a vaga com os dados recebidos;
- salva no banco;
- retorna a vaga criada.

### Endpoints
- `POST /api/jobsVacancies`
- `GET /api/jobsVacancies`
- `DELETE /api/jobsVacancies/{id}`

---

## 6.3 Curtir / candidatar-se a uma vaga

O candidato autenticado pode demonstrar interesse em uma vaga.

### O que acontece
1. o sistema identifica o candidato pelo e-mail do usuário logado;
2. busca a vaga pelo ID;
3. cria uma `CandidateApplication`;
4. define o status como `APPLIED`;
5. grava a data/hora da candidatura;
6. salva a candidatura no banco.

### Endpoint
- `POST /api/likeInJobs/{id}`

### Retorno
O sistema devolve um DTO com:
- nome da vaga;
- nome do candidato.

---

## 6.4 Seleção de candidatos pela empresa

A empresa pode marcar uma candidatura como selecionada.

### O que acontece
- a aplicação é buscada pelo ID;
- o status é alterado para `SELECTED`;
- a mudança é persistida;
- a lista de candidatos selecionados pode ser consultada.

### Endpoints
- `POST /api/likeInCandidates/{id}`
- `GET /api/likeInCandidates`

---

## 6.5 Geração de schedule

Quando há candidatos selecionados para uma vaga, o sistema gera schedules.

### O que o service faz
1. busca a vaga;
2. busca as candidaturas com status `SELECTED`;
3. cria um `Schedule` para cada candidato selecionado;
4. vincula empresa, candidato, vaga, data, horário e pagamento;
5. salva os schedules;
6. gera os chats automaticamente para os candidatos envolvidos.

### Endpoint
- `GET /api/schedule/{id}`

### Consulta por vaga
- `GET /api/schedule/job/{id}`

---

## 6.6 Chat entre empresa e candidato

Depois da geração do schedule, o sistema cria chats para permitir comunicação.

### Como o chat funciona
- existe uma tela Thymeleaf (`chats.html`);
- a tela lista as conversas disponíveis;
- ao clicar em uma conversa, o chat é aberto;
- as mensagens históricas são carregadas;
- novas mensagens podem ser enviadas em tempo real.

### APIs de chat
- `GET /api/chat/user/{userId}` → lista os chats de um usuário;
- `GET /api/messages/{chatId}` → retorna as mensagens do chat.

### WebSocket/STOMP
- endpoint WebSocket: `/chat`
- destino de aplicação: `/app`
- broker simples: `/topic`
- envio de mensagem: `/app/chat/send`
- tópico de broadcast por chat: `/topic/chat/{chatId}`

### Comportamento da interface
A página `chats.html` faz o seguinte:
- carrega a lista de conversas;
- exibe a outra parte da conversa;
- assina o tópico do chat selecionado;
- permite enviar mensagem com Enter ou botão.

---

## 6.7 Avaliação

O sistema permite avaliar usuários/empresas.

### O que acontece
- o usuário autenticado envia uma nota e descrição;
- o destinatário da avaliação é buscado pelo ID;
- a avaliação é salva com o nome de quem enviou.

### Endpoints
- `POST /api/rating/{id}`
- `GET /api/rating`

---

## 7. Endpoints resumidos

## Autenticação
- `POST /api/auth/login`
- `POST /api/auth/enterprise/register`
- `POST /api/auth/candidate/register`
- `POST /api/auth/admin/register`

## Vagas
- `POST /api/jobsVacancies`
- `GET /api/jobsVacancies`
- `DELETE /api/jobsVacancies/{id}`

## Curtidas / candidatura
- `POST /api/likeInJobs/{id}`
- `POST /api/likeInCandidates/{id}`
- `GET /api/likeInCandidates`

## Schedule
- `GET /api/schedule/{id}`
- `GET /api/schedule/job/{id}`

## Chat
- `GET /api/chat/user/{userId}`
- `GET /api/messages/{chatId}`
- WebSocket: `/chat`
- STOMP send: `/app/chat/send`
- tópico: `/topic/chat/{chatId}`

## Avaliação
- `POST /api/rating/{id}`
- `GET /api/rating`

---

## 8. Banco de dados e Flyway

O projeto usa **Flyway** para versionar o schema do banco.

As migrações ficam em:
- `src/main/resources/db/migration`

### O que existe hoje no fluxo de migrações
As versões cobrem:
- criação de usuários;
- endereços;
- emails;
- candidatos;
- empresas;
- vagas;
- candidaturas;
- avaliações;
- schedules;
- chat;
- alterações posteriores nas tabelas de chat e schedule.

---

## 9. Segurança

A aplicação usa **JWT** e trabalha com sessão `stateless`.

### Ideia geral
- rotas públicas ficam liberadas;
- rotas protegidas exigem token;
- o filtro de segurança valida o JWT e monta o contexto do usuário autenticado;
- os controllers usam o usuário logado para descobrir empresa/candidato.

### Observações
- as roles seguem o padrão `ROLE_ADMIN`, `ROLE_COMPANY` e `ROLE_CANDIDATE`;
- o projeto já usa `AuthenticationPrincipal` em alguns controllers.

---

## 10. Configuração da aplicação

O arquivo principal de configuração é:
- `src/main/resources/application.properties`

Lá ficam:
- URL do PostgreSQL;
- usuário e senha do banco via variáveis de ambiente;
- configuração do Hibernate;
- chave secreta do JWT.

### Variáveis esperadas
- `DATABASE_NAME`
- `DATABASE_PASSWORD`

> **Importante:** não é recomendado commitar credenciais reais em projetos de produção.

---

## 11. Como executar

### Pré-requisitos
- Java 21
- Maven Wrapper
- PostgreSQL rodando localmente

### Subir a aplicação
```powershell
cd D:\felipeCoisas\JAVA\labor
.\mvnw.cmd spring-boot:run
```

### Gerar o build
```powershell
cd D:\felipeCoisas\JAVA\labor
.\mvnw.cmd -DskipTests package
```

---

## 12. Observações importantes

- O projeto está **em evolução constante**.
- Alguns nomes de pacotes e classes refletem a história do desenvolvimento e podem ser padronizados depois.
- Existem fluxos já implementados, mas ainda há espaço para refinar DTOs, validações, erros padronizados e testes automatizados.
- A parte de chat já funciona com lista de conversas, histórico e envio em tempo real.

---

## 13. Resumo do que o projeto já faz hoje

O Labor já cobre:
- cadastro e login de usuários;
- criação de empresa e candidato;
- publicação de vagas;
- candidatura/interesse em vagas;
- seleção de candidatos;
- geração de agenda/schedule;
- criação automática de chat;
- envio e carregamento de mensagens;
- avaliação entre usuários.

---

## 14. Próximos passos recomendados

Se o projeto continuar crescendo, os próximos passos naturais são:
- telas mais completas para visualizar candidatura e status;
- filtros de vagas;
- melhoria no chat com paginação e ordenação;
- padronização de respostas e erros;
- testes de integração para autenticação, vagas e chat;
- documentação de exemplos de request/response para cada endpoint.

---

## 15. Nota final

Este documento foi escrito para refletir o estado atual do projeto e pode precisar de atualização conforme novas features forem adicionadas.

