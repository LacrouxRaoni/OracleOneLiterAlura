# 📚 LiterAlura - Sistema de Gerenciamento de Livros

![Java](https://img.shields.io/badge/Java-17+-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-purple)
![Oracle ONE](https://img.shields.io/badge/Oracle_ONE-Challenge-orange)

## 📌 Visão Geral
Projeto desenvolvido como parte do **desafio Alura/Oracle ONE**, criando uma aplicação CLI completa para busca e catalogação de livros utilizando a API Gutendex, com armazenamento em banco de dados PostgreSQL.

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### Configuração
#### 1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/literalura.git
cd literalura

Configure o banco de dados:

Crie um banco chamado literalura_banco

Edite o arquivo application.properties:
```


#### 2.  Configure o banco de dados:

-   Crie um banco chamado literalura_banco

-   Edite o arquivo application.properties:

```
properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

#### 3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

## 🖥️ Menu Interativo

```text
=== Menu LiterAlura ===
1) Buscar livros pelo título
2) Listar livros registrados
3) Listar autores registrados
4) Listar autores vivos em um ano
5) Listar livros por idioma
0) Sair
```

## 🏗️ Estrutura do Código

```java
// Entidade principal de Livros
@Entity
@Table(name = "books")
public class LiterAluraBooksEntity {
    @Id
    private Long id;  // ID da API Gutendex
    private String title;
    private String language;
    private Integer downloadCount;
    @ManyToOne
    private LiterAluraAuthorEntity author;
    // Getters e Setters
}
```

## 🔍 Funcionalidades Avançadas

### Busca na API Gutendex
```java
public LiterAluraBooksEntity searchAndSaveBook(String title) throws IOException {
    String url = "https://gutendex.com/books/?search=" + URLEncoder.encode(title, "UTF-8");
    JsonNode response = restTemplate.getForObject(url, JsonNode.class);
    // Processamento dos dados...
}
```

### Consultas Especiais

```java
// Autores vivos em determinado ano
@Query("SELECT a FROM LiterAluraAuthorEntity a WHERE (a.birthYear <= :year) AND (a.deathYear IS NULL OR a.deathYear >= :year)")
List<LiterAluraAuthorEntity> findAliveInYear(@Param("year") int year);
```

## 📦 Dependências Principais
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
    </dependency>
</dependencies>
```