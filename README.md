## 📚 README.md: Jecommerce - Sistema de E-commerce com Spring Boot e JPA

Este projeto é uma aplicação de back-end simples para um sistema de e-commerce, construído com **Spring Boot** e utilizando o **JPA/Hibernate** para persistência de dados. Durante o desenvolvimento, enfrentamos e superamos desafios comuns de mapeamento de entidades e *seeding* (população inicial) do banco de dados.

-----

## 🎯 O Projeto: Jecommerce

### O Porquê (The Why)

O objetivo principal deste projeto foi construir um modelo de domínio sólido para uma plataforma de e-commerce, focando na correta estruturação das entidades, seus relacionamentos e na persistência de dados utilizando o ecossistema Spring Boot com o banco de dados em memória **H2**.

### Modelo de Domínio e ORM

O sistema é baseado em um modelo de domínio clássico de e-commerce que inclui as seguintes entidades: **User**, **Order**, **Product**, **Category**, **OrderItem** e **Payment**.

O diagrama abaixo ilustra as entidades e seus relacionamentos (ORM - Mapeamento Objeto-Relacional):

<br>

As entidades foram cuidadosamente criadas, incluindo:

  * **`User.java`**: Representa um cliente no sistema.
  * **`Order.java`**: Representa um pedido realizado, com um status (`OrderStatus`) e uma referência ao cliente (`client`).
  * **`OrderItemPK.java` e `OrderItem.java`**: Utilizam uma chave composta (`@EmbeddedId`) para mapear a relação muitos-para-muitos entre `Order` e `Product`.

### Entidades Java (Exemplo)

Aqui está um exemplo da entidade `Order` e a classe de chave composta `OrderItemPK` que representam os relacionamentos.

**Entidade `Order.java`:**

**Chave Composta `OrderItemPK.java`:**

-----

## 🚀 Desafios e Resultados

Durante a fase de inicialização e *seeding* do banco de dados, enfrentamos alguns desafios comuns de configuração do Hibernate e de integridade referencial.

### 1\. Desafio: Inconsistência do Esquema (Schema Inconsistency)

O principal desafio foi a falha no *seeding* (população inicial) dos dados definidos no arquivo `import.sql`. O Hibernate, ao criar as tabelas a partir das entidades, não estava gerando algumas colunas esperadas pelo script de inserção.

```bash
# Erro comum: Coluna não encontrada
org.h2.jdbc.JdbcSQLSyntaxErrorException: Column "PHONE" not found; 
# ... ou
org.h2.jdbc.JdbcSQLSyntaxErrorException: Column "BIRTH_DATE" not found; 
```

**Resultado:**
O problema foi resolvido garantindo a **sincronização exata** entre o nome da propriedade na entidade Java (ex: `phone`, `birthDate`) e o nome da coluna no script SQL, e, quando necessário, utilizando a anotação `@Column(name = "nome_da_coluna")` para forçar o mapeamento.

### 2\. Desafio: Violação de Integridade Referencial (Foreign Key Violation)

Outro erro que surgiu foi a falha ao inserir pedidos (`tb_order`), pois a chave estrangeira (`client_id`) referia-se a usuários que ainda não tinham sido persistidos no banco.

```bash
# Erro de Foreign Key
org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: Referential integrity constraint violation: [...]
```

**Resultado:**
Este erro foi uma **consequência** da falha anterior. Uma vez corrigida a inserção dos usuários (`tb_user`), a tabela `tb_order` pôde ser populada com sucesso, demonstrando que a ordem de execução dos `INSERTs` e a integridade do esquema foram restauradas.

### Aplicação Rodando no Terminal (Inicialização)

Após as correções, a aplicação inicia corretamente, inicializando o servidor Tomcat embutido e o banco de dados H2.

### Acesso ao Console H2

A aplicação está configurada para expor o console do H2 na URL `/h2-console`, conforme as propriedades em `application-test.properties`.

### Dados Persistidos

A prova final do sucesso foi a visualização dos dados do *seeding* no console do H2, como a tabela `TB_ORDER_ITEM` sendo preenchida:

-----

## ✨ Melhorias e Próximos Passos

1.  **Segurança da Senha:** Implementar a criptografia de senhas (ex: usando `BCryptPasswordEncoder` do Spring Security) em vez de armazená-las em texto simples.
2.  **Transição para o MySQL:** Embora o H2 seja ótimo para desenvolvimento, o projeto estava pronto para ser migrado para o MySQL/PostgreSQL (que foi testado em outras tentativas), usando o XAMPP ou outro servidor de banco de dados externo.
3.  **Implementação de Serviços (CRUD):** Criar a camada de serviços e *controllers* REST para expor as operações CRUD (Criação, Leitura, Atualização e Deleção) das entidades.
