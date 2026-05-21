# Debt Manager API

API REST para gerenciamento de dividas parceladas. O projeto permite cadastrar uma divida, gerar suas parcelas automaticamente e listar os registros cadastrados.

## Tecnologias

![Java](https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Wrapper-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)

## Funcionalidades

- Cadastro de dividas com valor total, quantidade de parcelas, categoria e observacoes.
- Geracao automatica das parcelas mensais.
- Distribuicao dos centavos restantes na ultima parcela.
- Listagem das dividas cadastradas.
- Validacao dos dados de entrada.
- Persistencia em PostgreSQL usando Spring Data JPA.

## Como Rodar

### Pre-requisitos

- Java 21+
- Docker e Docker Compose

### 1. Clone o repositorio

```bash
git clone https://github.com/seu-usuario/debt-manager-api.git
cd debt-manager-api
```

### 2. Suba o banco de dados

```bash
docker compose up -d
```

O PostgreSQL sera iniciado com:

| Campo | Valor |
| --- | --- |
| Host | `localhost` |
| Porta | `5432` |
| Banco | `debt_db` |
| Usuario | `postgres` |
| Senha | `1234` |

### 3. Execute a aplicacao

No Windows:

```bash
.\mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

A API ficara disponivel em:

```text
http://localhost:8080
```

## Endpoints

### Criar divida

```http
POST /api/v1/dividas
```

Exemplo de requisicao:

```json
{
  "nome": "Notebook",
  "valorTotal": 3500.00,
  "quantidadeParcelas": 10,
  "dataPrimeiroVencimento": "2026-06-10",
  "categoria": "Eletronicos",
  "observacoes": "Compra parcelada"
}
```

Exemplo de resposta:

```json
{
  "id": "f8dbf187-6c0f-4c93-919f-5f0487d3b5ab",
  "nome": "Notebook",
  "valorTotal": 3500.00,
  "quantidadeParcelas": 10,
  "status": "PENDENTE"
}
```

### Listar dividas

```http
GET /api/v1/dividas
```

Exemplo de resposta:

```json
[
  {
    "id": "f8dbf187-6c0f-4c93-919f-5f0487d3b5ab",
    "nome": "Notebook",
    "valorTotal": 3500.00,
    "quantidadeParcelas": 10,
    "status": "PENDENTE"
  }
]
```

## Validacoes

Ao criar uma divida, a API valida:

| Campo | Regra |
| --- | --- |
| `nome` | Obrigatorio, entre 3 e 100 caracteres |
| `valorTotal` | Obrigatorio e maior que zero |
| `quantidadeParcelas` | Obrigatorio e no minimo 1 |
| `dataPrimeiroVencimento` | Obrigatoria e nao pode estar no passado |

## Estrutura do Projeto

```text
src/main/java/com/leonardo/debt_manager_api
+-- controller
|   +-- DividaController.java
+-- dto
|   +-- DividaRequestDTO.java
|   +-- DividaResponseDTO.java
+-- exceptions
+-- model
|   +-- Divida.java
|   +-- Parcela.java
|   +-- StatusParcela.java
+-- repository
|   +-- DividaRepository.java
|   +-- ParcelaRepository.java
+-- service
    +-- DividaService.java
```

## Testes

Para executar os testes:

```bash
.\mvnw.cmd test
```

No Linux/macOS:

```bash
./mvnw test
```

## Variaveis e Configuracao

As configuracoes principais estao em `src/main/resources/application.yml`.

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/debt_db
    username: postgres
    password: 1234
```

## Status do Projeto

Em desenvolvimento. Funcionalidades atuais cobrem cadastro e listagem de dividas. Proximos passos possiveis:

- Buscar divida por ID.
- Atualizar e remover dividas.
- Marcar parcelas como pagas.
- Calcular status geral da divida com base nas parcelas.
- Adicionar autenticacao.
