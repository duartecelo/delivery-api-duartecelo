# Delivery Tech API

Sistema de delivery moderno desenvolvido com **Spring Boot** e **Java 21**, projetado para demonstrar boas práticas no desenvolvimento de APIs RESTful.

## 🚀 Tecnologias Utilizadas
- Java 21 LTS
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos
O projeto faz uso de funcionalidades introduzidas nas versões mais recentes do Java:
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching para instanceof (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como Executar
1. Certifique-se de ter o **JDK 21** instalado.
2. Clone este repositório:
git clone https://github.com/usuario/delivery-tech-api.git
3. Acesse o diretório do projeto e execute:
./mvnw spring-boot:run
4. Abra no navegador: [http://localhost:8080/health](http://localhost:8080/health)

## 📋 Endpoints Principais
- **GET /health** – Retorna o status da aplicação e a versão do Java em uso.
- **GET /info** – Exibe informações gerais da API.
- **GET /h2-console** – Acesso ao console do banco H2 em memória.

## 🔧 Configuração Padrão
- Porta: `8080`
- Banco de dados: H2
- Profile ativo: `development`

## 👨‍💻 Desenvolvedor
Marcelo Duarte de Aguiar – Ciência da Computação – UniRitter - 2024/1 
Desenvolvido com JDK 21 e Spring Boot 3.5.7