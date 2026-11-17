# Cidade em Dia

> Plataforma colaborativa para registrar, acompanhar e priorizar problemas urbanos em São Paulo.

**FIAP · Global Solution 2026.2 · CitizenTech · Engenharia de Software · Domain Driven Design – Java**

## Ficha da CitizenTech

| Campo | Resposta |
|---|---|
| **Nome da CitizenTech** | Cidade em Dia |
| **Cidade e bairro** | São Paulo – SP, região central e bairros próximos (Bela Vista, Liberdade, Vila Mariana, Pinheiros, Mooca e Santana) |
| **Cidadão beneficiado** | Moradores de São Paulo, especialmente pessoas que dependem de espaços públicos bem conservados para a sua mobilidade, segurança e qualidade de vida. |
| **Problema, em uma frase** | A dificuldade de registrar, acompanhar e priorizar problemas de infraestrutura urbana, como buracos, calçadas danificadas, iluminação pública com defeito e descarte irregular de lixo. |
| **Como funciona** | Os moradores registram problemas da cidade informando categoria, bairro, descrição, endereço, coordenadas e um link de foto. Outros moradores confirmam que o problema existe, e o sistema calcula automaticamente um nível de prioridade (`BAIXA`, `MEDIA`, `ALTA` ou `CRITICA`) a partir da quantidade de confirmações, do tempo em aberto e da categoria. Administradores atualizam o status (`PENDENTE`, `EM_ANDAMENTO` ou `RESOLVIDO`), e cada mudança fica registrada em um histórico. |
| **O que cada parte faz** | **Cidadão:** registra ocorrências, envia fotos por meio de links, confirma problemas de outros moradores e acompanha status e histórico.<br>**Plataforma (API RESTful em Java, esta entrega):** organiza os registros, valida as regras, calcula as prioridades, controla as permissões de administrador e expõe os endpoints.<br>**Banco de dados Oracle:** armazena usuários, ocorrências, relatos, histórico e as tabelas de domínio.<br>**Painel administrativo (protótipo):** permite visualizar as ocorrências por prioridade e atualizar o status, consumindo a API.<br>**Inteligência de dados:** analisa as informações das ocorrências e auxilia na previsão do tempo de resolução e na priorização dos problemas. É uma entrega da disciplina de Data Science, não faz parte desta API. |
| **Link do repositório** | https://github.com/marcossilva11/cidade-em-dia |
| **Indicador de impacto** | **Percentual de ocorrências resolvidas:** proporção de ocorrências com status `RESOLVIDO` em relação ao total de ocorrências registradas. |
| **ODS conectados** | **ODS 11 – Cidades e Comunidades Sustentáveis:** cidades mais seguras e bem cuidadas e acesso a espaços públicos seguros e acessíveis (meta 11.7).<br>**ODS 9 – Indústria, Inovação e Infraestrutura:** uso de tecnologia para monitorar e melhorar a infraestrutura urbana (meta 9.1).<br>**ODS 16 – Paz, Justiça e Instituições Eficazes:** participação cidadã, transparência e acompanhamento de demandas públicas (metas 16.6 e 16.7). |

## Links

| | |
|---|---|
| Repositório | https://github.com/marcossilva11/cidade-em-dia |
| Deploy (API no Render) | https://cidade-em-dia.onrender.com |
| Consulta rápida no navegador | https://cidade-em-dia.onrender.com/ocorrencias |

> O endereço raiz do deploy não possui página própria. Para ver a API funcionando, abra `/ocorrencias`.
> O plano gratuito do Render "dorme" após alguns minutos sem uso, então a **primeira chamada pode levar cerca de 1 minuto**.

## Funcionalidades

- Cadastro e login de usuários (senha armazenada com BCrypt).
- Cadastro, consulta, atualização e exclusão de ocorrências.
- Filtro de ocorrências por status e ordenação por prioridade.
- Confirmação de ocorrências por outros moradores (relatos).
- Cálculo automático de prioridade por quantidade de relatos, tempo em aberto e categoria.
- Histórico de mudanças de status, com observação e responsável.
- Alteração de status e recálculo de prioridades restritos a administradores.
- Consulta das tabelas de apoio: categorias, bairros, status e prioridades.

## Regras de negócio

**Prioridade automática**

```
pontos = (relatos × 10) + (dias em aberto × 5) + peso da categoria
```

| Pontos | Prioridade |
|---|---|
| 0 a 14 | BAIXA |
| 15 a 29 | MEDIA |
| 30 a 49 | ALTA |
| 50 ou mais | CRITICA |

| Categoria | Peso |
|---|---|
| Alagamento | 20 |
| Buraco na via, Iluminação pública, Sinalização | 10 |
| Calçada danificada, Lixo e entulho | 5 |

- A prioridade é calculada ao cadastrar, ao editar a ocorrência e a cada novo relato. Como o tempo passa sem eventos, existe um endpoint administrativo de recálculo.
- Ocorrências `RESOLVIDO` mantêm a última prioridade calculada.
- O autor da ocorrência não pode confirmá-la, e cada usuário confirma uma ocorrência uma única vez.

**Status e histórico**

- Toda ocorrência nasce `PENDENTE`, e esse estado inicial já é gravado no histórico.
- Somente usuários `ADMIN` alteram o status (resposta 403 para os demais).
- Só é possível excluir uma ocorrência que ainda não recebeu relatos nem mudança de status.

## Tecnologias utilizadas

- Java 21
- Spring Boot 4.0.9 (Web MVC e Spring Data JPA)
- Oracle Database (driver `ojdbc17`)
- `spring-security-crypto` (somente BCrypt)
- Maven (wrapper `mvnw` incluído)
- Docker e Render (deploy)
- Git e GitHub

## Arquitetura

O projeto é organizado em camadas, dentro de `br.com.cidadeemdia.cidade_em_dia`:

| Pacote | Responsabilidade |
|---|---|
| `model` | Entidades JPA que representam as tabelas do banco |
| `dao` | Acesso e persistência dos dados (Spring Data JPA) |
| `bo` | Regras de negócio e validações |
| `controller` | Recebe e responde às requisições HTTP |
| `dto` | Objetos de entrada e saída da API |
| `exception` | Exceções de negócio e tratamento centralizado de erros |
| `config` | Configurações da aplicação |

## Banco de dados

Os scripts estão na pasta `database`:

| Arquivo | Conteúdo |
|---|---|
| `cidade_em_dia_banco_oracle.sql` | Tabelas, constraints, sequences, índices e dados de `STATUS` e `PRIORIDADE` |
| `dados_teste.sql` | Dados fictícios de teste (bairros, categorias e usuários) |

A aplicação usa `ddl-auto=validate`: ela **não cria** tabelas, apenas confere se o banco corresponde às entidades. Por isso os scripts precisam ser executados antes.

**Dados de teste:** o projeto possui dados fictícios, incluindo um usuário administrador (`admin@cidadeemdia.exemplo`, ID 1) e usuários cidadãos. A senha de todos os usuários criados pelo script `dados_teste.sql` é `123456`.

## Endpoints

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| GET | `/ocorrencias` | Lista ocorrências. Parâmetros opcionais: `ordenarPor` (`prioridade` ou `data`) e `status` | 200 |
| GET | `/ocorrencias/{id}` | Consulta uma ocorrência | 200 |
| POST | `/ocorrencias` | Cadastra uma ocorrência | 201 |
| PUT | `/ocorrencias/{id}` | Atualiza uma ocorrência | 200 |
| DELETE | `/ocorrencias/{id}` | Exclui uma ocorrência sem relatos nem mudanças de status | 204 |
| PUT | `/ocorrencias/{id}/status` | Altera o status (somente `ADMIN`) | 200 |
| GET | `/ocorrencias/{id}/historico` | Lista o histórico de status | 200 |
| POST | `/ocorrencias/{id}/relatos` | Confirma uma ocorrência | 201 |
| POST | `/ocorrencias/recalcular-prioridades?idUsuario=` | Recalcula as prioridades (somente `ADMIN`) | 200 |
| POST | `/usuarios` | Cadastra um cidadão | 201 |
| POST | `/usuarios/login` | Valida e-mail e senha | 200 |
| GET | `/categorias` | Lista categorias | 200 |
| GET | `/bairros` | Lista bairros | 200 |
| GET | `/status` | Lista status | 200 |
| GET | `/prioridades` | Lista prioridades | 200 |

Erros são devolvidos em JSON no formato `{"erro": "mensagem"}`, com os códigos 400, 401, 403, 404, 405, 409 ou 500.

### Exemplo: cadastrar uma ocorrência

`POST /ocorrencias`

```json
{
  "idUsuario": 2,
  "idBairro": 1,
  "idCategoria": 5,
  "titulo": "Alagamento na Rua Treze de Maio",
  "descricao": "Água acumulada na pista após chuva forte.",
  "endereco": "Rua Treze de Maio, 500",
  "latitude": -23.5589,
  "longitude": -46.6478
}
```

Resposta `201`: a ocorrência nasce com `status` `PENDENTE`, `prioridade` `MEDIA` (o peso da categoria Alagamento é 20) e `quantidadeRelatos` igual a 0.

## Como executar localmente

**Pré-requisitos:** Java JDK 21, Git, acesso a um schema Oracle (dados de conexão fornecidos pela FIAP), IntelliJ IDEA (ou outra IDE compatível, ou apenas o terminal) e, opcionalmente, Postman para testar os endpoints.

**1. Clonar o repositório**

```bash
git clone https://github.com/marcossilva11/cidade-em-dia.git
cd cidade-em-dia
```

**2. Criar o banco**

No SQL Developer, conectado ao seu schema Oracle, execute **nesta ordem**:

1. `database/cidade_em_dia_banco_oracle.sql`
2. `database/dados_teste.sql`

**3. Configurar as variáveis de ambiente** (exemplo em PowerShell)

```powershell
$env:DB_URL="jdbc:oracle:thin:@HOST:1521:SID"
$env:DB_USERNAME="seu_usuario"
$env:DB_PASSWORD="sua_senha"
```

Os nomes das variáveis são os mesmos em qualquer sistema operacional. No Linux/macOS, use `export`; no CMD, use `set`; no IntelliJ IDEA, cadastre-as em *Run → Edit Configurations → Environment variables*.

**4. Executar a aplicação**, no mesmo terminal em que as variáveis foram definidas:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux ou macOS
./mvnw spring-boot:run
```

**5. Testar**

Abra `http://localhost:8080/ocorrencias`. Com o banco recém-criado, a resposta é `[]`. Em seguida, use o Postman para cadastrar uma ocorrência com o exemplo acima.

## Deploy

A API já está publicada no Render, com deploy automático a cada commit na branch `main`, e pode ser acessada em https://cidade-em-dia.onrender.com. Como o plano gratuito "dorme" após alguns minutos sem uso, a primeira chamada pode levar cerca de 1 minuto. O deploy usa o `Dockerfile` da raiz do projeto e as variáveis `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`.

## Limitações conhecidas

- O login confere e-mail e senha, mas **não emite token nem sessão**; os demais endpoints são abertos, e a permissão de administrador é verificada pelo `idUsuario` informado.
- Administradores são criados diretamente no banco; o cadastro pela API sempre cria um `CIDADAO`.
- A foto é registrada como link (`foto`); não há upload de arquivos.
- As coordenadas (`latitude` e `longitude`) ficam armazenadas, mas a exibição em mapa está prevista apenas nos protótipos.
- A prioridade só é recalculada em eventos (cadastro, edição e novo relato) e pelo endpoint administrativo de recálculo.
- Os dados disponibilizados no ambiente de demonstração são fictícios.

## Integrantes

| Integrante | RM |
|---|---:|
| Daniel Laureano da Luz | 565899 |
| Lucas Toledo Cortonezi | 563271 |
| Marcos Antonio da Silva | 566451 |
| Paulo Gustavo Pessoa da Silva | 566194 |
| Rone Cruz Santos | 566558 |

**FIAP · Global Solution 2026.2**
