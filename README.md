# Raízes do Nordeste — API Back-End

API REST para gestão de franquias de restaurante, desenvolvida com Java 21 e Spring Boot 3.5.

---

## Requisitos

- Java 21
- Maven 3.9+
- Nenhuma instalação de banco de dados é necessária — o projeto usa H2 em memória

---

## Como configurar

O projeto não exige variáveis de ambiente externas. As configurações estão em
`src/main/resources/application.yaml`.

O único valor sensível é o segredo JWT, definido diretamente no arquivo:

```yaml
jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
  expiration: 86400000
```

Em produção, esse valor deve ser externalizado via variável de ambiente.

---

## Como instalar dependências

```bash
mvn clean install
```

---

## Banco de dados

O projeto usa H2 em memória. O schema é criado automaticamente pelo Hibernate ao subir
a aplicação (`ddl-auto: create-drop`). Não há migrations nem seed — o banco começa vazio
a cada inicialização.

Console H2 disponível em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:raizesnordeste`
- Usuário: `sa`
- Senha: (vazio)

---

## Como iniciar a API

```bash
mvn spring-boot:run
```

A API sobe na porta `8080` por padrão.

---

## Como acessar a documentação (Swagger/OpenAPI)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

O JSON da especificação OpenAPI está disponível em:

```
http://localhost:8080/v3/api-docs
```

---

## Como testar (Insomnia)

A coleção de testes está no repositório:

- `Collection-raizes-do-nordeste.json` — formato Insomnia v4 JSON

Para importar no Insomnia: `File > Import > From File` e selecione o arquivo `.json`.

**Ordem sugerida de execução:**

1. `POST /auth/registro` — cadastrar usuário GERENTE
2. `POST /auth/registro` — cadastrar usuário CLIENTE
3. `POST /auth/login` — fazer login com GERENTE e copiar o token
4. `POST /unidades` — criar uma unidade (token GERENTE)
5. `POST /produtos` — criar um produto (token GERENTE)
6. `POST /cardapio` — vincular produto à unidade com preço (token GERENTE)
7. `POST /estoque/entrada` — adicionar estoque (token GERENTE)
8. `POST /auth/login` — fazer login com CLIENTE e copiar o token
9. `POST /fidelidade/consentimento` — aderir ao programa de pontos (token CLIENTE)
10. `POST /pedidos` — criar pedido (token CLIENTE)
11. `POST /pagamentos` — processar pagamento do pedido (token CLIENTE)
12. `GET /fidelidade/meus-pontos` — verificar pontos acumulados (token CLIENTE)

---

## Repositório

[https://github.com/D2pb8p9/raizes-nordeste-api](https://github.com/D2pb8p9/raizes-nordeste-api)

---

## Estrutura do projeto

```
src/main/java/com/raizesnordeste/api/
├── domain/          → entidades (@Entity) e enums do domínio
├── application/     → serviços (@Service) com regras de negócio
├── infrastructure/  → repositórios JPA, segurança e configurações
└── api/             → controllers, DTOs e tratamento de exceções
```
