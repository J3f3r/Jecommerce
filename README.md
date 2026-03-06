Jecommerce - Spring Boot API 🚀
Este projeto é uma aplicação back-end robusta para um sistema de e-commerce, construída com Spring Boot 3.4.2 e Java 17/21. A aplicação utiliza JPA/Hibernate para persistência de dados e foi migrada de um ambiente de teste (H2) para um ambiente de produção simulado com PostgreSQL rodando em Docker.

🎯 O Projeto
O objetivo principal foi construir um modelo de domínio sólido, tratando relacionamentos complexos, segurança com OAuth2/JWT e garantindo a integridade referencial em um banco de dados relacional externo.

Modelo de Domínio e ORM
O sistema baseia-se em um modelo clássico de e-commerce:

User & Role: Controle de acesso e perfis de usuário.

Product & Category: Relacionamento muitos-para-muitos.

Order & OrderItem: Gerenciamento de pedidos com chaves compostas (@EmbeddedId).

Payment: Registro de transações financeiras.

🛠️ Tecnologias Utilizadas
Java 21 / Spring Boot 3.4.2

Spring Data JPA (Persistência)

Spring Security (OAuth2 & JWT)

PostgreSQL (Banco de Dados)

Docker & Docker Compose (Containerização)

Postman (Testes de API)

🚀 Desafios Superados (Change Log)
Diferente da versão inicial, o projeto evoluiu para resolver problemas reais de infraestrutura e código moderno:

1. Migração para PostgreSQL via Docker
Desafio: Conflitos de criação de banco e persistência entre o Docker e o Spring.
Solução: - Implementação do docker-compose.yml para subir o container do Postgres.

Criação manual do banco jecommercedb para isolar os metadados do container da aplicação.

Configuração do application-dev.properties com ddl-auto=none para maior controle do esquema via scripts SQL.

2. Correção de Named Parameters (@Param)
Desafio: Erro java.lang.IllegalStateException: For queries with named parameters you need to provide names....
Solução: Com a atualização para o Spring Boot 3.2+, o compilador Java pode omitir nomes de parâmetros. A solução foi tornar explícito o mapeamento nos Repositories:

3. Otimização do Repositório (Git)
Desafio: Lentidão e arquivos pesados devido ao rastreamento da pasta de dados do Docker (.data/).
Solução: Configuração rigorosa do .gitignore e limpeza do cache do Git para manter o repositório leve, contendo apenas código-fonte.

⚙️ Como Executar o Projeto
Subir o Banco de Dados:
Certifique-se de ter o Docker instalado e rode:
docker-compose up -d

Configurar o Banco no pgAdmin:
Crie o banco de dados chamado jecommercedb no servidor localhost:5433.

Rodar a Aplicação:
Importe o projeto no STS (Spring Tool Suite) e execute como Spring Boot App.

Testar no Postman:

Realize o login em POST /oauth2/token para obter o Bearer Token.

Utilize o token para acessar os demais endpoints protegidos.

✨ Próximos Passos
Bean Validation: Adicionar validações customizadas nos DTOs.

Tratamento de Exceções: Refinar o ResourceExceptionHandler para capturar erros específicos de banco.

Testes Automatizados: Implementar testes de integração com JUnit e Mockito
