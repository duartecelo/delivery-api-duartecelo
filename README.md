# Delivery Tech API

Sistema de delivery moderno desenvolvido com **Spring Boot** e **Java 21**, projetado para demonstrar boas práticas no desenvolvimento de APIs RESTful.

## 🚀 Tecnologias Utilizadas
- Java 21 LTS
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- H2 Database (em memória)
- Maven

## ⚡ Recursos Modernos
O projeto faz uso de funcionalidades introduzidas nas versões mais recentes do Java:
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching para instanceof (Java 17+)
- Virtual Threads (Java 21)

---

## 🏃‍♂️ Como Executar

### 1. Pré-requisitos
- **JDK 21** instalado ([Download aqui](https://www.oracle.com/java/technologies/downloads/))
- **Maven** 3.6+ instalado ([Download aqui](https://maven.apache.org/download.cgi))
- **Git** instalado

### 2. Clonar o Repositório
```bash
git clone https://github.com/usuario/delivery-tech-api.git
cd delivery-tech-api
```

### 3. Executar a Aplicação
```bash
# Opção 1: Maven
./mvnw spring-boot:run

# Opção 2: Maven (Windows)
mvnw.cmd spring-boot:run

# Opção 3: Compilar e executar JAR
./mvnw clean package
java -jar target/delivery-api-1.0.0.jar
```

### 4. Verificar a Aplicação
```bash
# Abrir no navegador
http://localhost:8080/actuator/health

# Resposta esperada
{
  "status": "UP"
}
```

---

## 📋 Endpoints Principais

### 🏥 Health Check
- **GET** `/actuator/health` – Status da aplicação (status: UP)
- **GET** `/actuator/info` – Informações gerais da API

### 🗄️ Console do Banco H2
- **GET** `/h2-console` – Acessar console H2 para visualizar dados
    - **JDBC URL**: `jdbc:h2:mem:testdb`
    - **User Name**: `sa`
    - **Password**: (deixar em branco)

---

## 🎯 API REST - Recursos Implementados

### Base URL
```
http://localhost:8080/api/v1
```

### 👥 Clientes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/clientes` | Criar novo cliente |
| GET | `/clientes` | Listar clientes ativos |
| GET | `/clientes/{id}` | Buscar cliente por ID |
| GET | `/clientes/email/{email}` | Buscar cliente por e-mail |
| PUT | `/clientes/{id}` | Atualizar cliente |
| PUT | `/clientes/{id}/inativar` | Inativar cliente |
| PUT | `/clientes/{id}/ativar` | Ativar cliente |
| DELETE | `/clientes/{id}` | Deletar cliente |

### 🍽️ Restaurantes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/restaurantes` | Criar novo restaurante |
| GET | `/restaurantes` | Listar todos |
| GET | `/restaurantes/ativos` | Listar ativos |
| GET | `/restaurantes/{id}` | Buscar por ID |
| GET | `/restaurantes/categoria/{categoria}` | Buscar por categoria |
| GET | `/restaurantes/buscar/nome?nome=X` | Buscar por nome |
| PUT | `/restaurantes/{id}` | Atualizar |
| PUT | `/restaurantes/{id}/ativar` | Ativar |
| PUT | `/restaurantes/{id}/inativar` | Inativar |
| DELETE | `/restaurantes/{id}` | Deletar |

### 🍕 Produtos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/produtos` | Criar novo produto |
| GET | `/produtos/{id}` | Buscar por ID |
| GET | `/produtos/restaurante/{id}` | Listar por restaurante |
| GET | `/produtos/restaurante/{id}/disponiveis` | Listar disponíveis |
| GET | `/produtos/restaurante/{id}/indisponíveis` | Listar indisponíveis |
| PUT | `/produtos/{id}` | Atualizar |
| PUT | `/produtos/{id}/ativar` | Ativar |
| PUT | `/produtos/{id}/desativar` | Desativar |
| DELETE | `/produtos/{id}` | Deletar |

### 📦 Pedidos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/pedidos` | Criar novo pedido |
| GET | `/pedidos/{id}` | Buscar por ID |
| GET | `/pedidos/cliente/{id}` | Listar por cliente |
| GET | `/pedidos/status/{status}` | Listar por status |
| PUT | `/pedidos/{id}/confirmar` | Confirmar pedido |
| PUT | `/pedidos/{id}/cancelar` | Cancelar pedido |
| PUT | `/pedidos/{id}/iniciar-preparacao` | Iniciar preparação |
| PUT | `/pedidos/{id}/sair-para-entrega` | Sair para entrega |
| PUT | `/pedidos/{id}/entregar` | Entregar pedido |
| DELETE | `/pedidos/{id}` | Deletar pedido |

---

## 🧪 Testes e Validação

### Teste 1: Criar Cliente (Sucesso)
```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com"
  }'
```

**Resposta Esperada (201 CREATED):**
```json
{
  "mensagem": "Cliente criado com sucesso",
  "dados": {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "ativo": true
  },
  "sucesso": true
}
```

### Teste 2: Validar E-mail Único
```bash
# Tentar criar cliente com mesmo e-mail
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Silva",
    "email": "joao@example.com"
  }'
```

**Resposta Esperada (400 BAD REQUEST):**
```json
{
  "mensagem": "Email já cadastrado no sistema",
  "codigo": 400,
  "timestamp": "2025-10-31T13:45:00"
}
```

### Teste 3: Listar Clientes
```bash
curl -X GET http://localhost:8080/api/v1/clientes
```

**Resposta Esperada (200 OK):**
```json
{
  "mensagem": "Total de 1 clientes encontrados",
  "dados": [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "ativo": true
    }
  ],
  "sucesso": true
}
```

### Teste 4: Criar Restaurante
```bash
curl -X POST http://localhost:8080/api/v1/restaurantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizzaria do João",
    "categoria": "Italiana",
    "avaliacao": 4.5
  }'
```

**Resposta Esperada (201 CREATED):**
```json
{
  "mensagem": "Restaurante criado com sucesso",
  "dados": {
    "id": 1,
    "nome": "Pizzaria do João",
    "categoria": "Italiana",
    "avaliacao": 4.5,
    "ativo": true
  },
  "sucesso": true
}
```

### Teste 5: Validar Avaliação (0-5)
```bash
# Tentar criar com avaliação inválida
curl -X POST http://localhost:8080/api/v1/restaurantes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Restaurante X",
    "categoria": "Italiana",
    "avaliacao": 7.5
  }'
```

**Resposta Esperada (400 BAD REQUEST):**
```json
{
  "mensagem": "Avaliação deve estar entre 0.0 e 5.0",
  "codigo": 400,
  "timestamp": "2025-10-31T13:45:00"
}
```

### Teste 6: Criar Produto
```bash
curl -X POST http://localhost:8080/api/v1/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pizza Margherita",
    "categoria": "Pizza",
    "restaurante": {
      "id": 1
    }
  }'
```

**Resposta Esperada (201 CREATED):**
```json
{
  "mensagem": "Produto criado com sucesso",
  "dados": {
    "id": 1,
    "nome": "Pizza Margherita",
    "categoria": "Pizza",
    "disponivel": true,
    "restaurante": {
      "id": 1,
      "nome": "Pizzaria do João"
    }
  },
  "sucesso": true
}
```

### Teste 7: Criar Pedido
```bash
curl -X POST http://localhost:8080/api/v1/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": {
      "id": 1
    },
    "valorTotal": 150.50
  }'
```

**Resposta Esperada (201 CREATED):**
```json
{
  "mensagem": "Pedido criado com sucesso",
  "dados": {
    "id": 1,
    "cliente": {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "ativo": true
    },
    "status": "PENDENTE",
    "valorTotal": 150.50,
    "dataCriacao": "2025-10-31T13:45:00"
  },
  "sucesso": true
}
```

### Teste 8: Validar Máquina de Estados (Status)
```bash
# Tentar transição inválida (PENDENTE diretamente para ENTREGUE)
curl -X PUT http://localhost:8080/api/v1/pedidos/1/status?novoStatus=ENTREGUE
```

**Resposta Esperada (400 BAD REQUEST):**
```json
{
  "mensagem": "Status PENDENTE só pode transitar para CONFIRMADO ou CANCELADO",
  "codigo": 400,
  "timestamp": "2025-10-31T13:45:00"
}
```

### Teste 9: Transição Válida de Status
```bash
# Confirmar pedido (PENDENTE → CONFIRMADO)
curl -X PUT http://localhost:8080/api/v1/pedidos/1/confirmar
```

**Resposta Esperada (200 OK):**
```json
{
  "mensagem": "Pedido confirmado com sucesso",
  "dados": {
    "id": 1,
    "cliente": {...},
    "status": "CONFIRMADO",
    "valorTotal": 150.50,
    "dataCriacao": "2025-10-31T13:45:00"
  },
  "sucesso": true
}
```

### Teste 10: Validar Cliente Ativo
```bash
# Tentar criar pedido com cliente inativo
# 1. Primeiro, inativar cliente
curl -X PUT http://localhost:8080/api/v1/clientes/1/inativar

# 2. Tentar criar pedido
curl -X POST http://localhost:8080/api/v1/pedidos \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": {
      "id": 1
    },
    "valorTotal": 100.00
  }'
```

**Resposta Esperada (400 BAD REQUEST):**
```json
{
  "mensagem": "Não é possível criar pedido para cliente inativo",
  "codigo": 400,
  "timestamp": "2025-10-31T13:45:00"
}
```

---

## 💾 Verificar Dados no Banco H2

### 1. Acessar Console H2
```
http://localhost:8080/h2-console
```

### 2. Configuração de Conexão
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User Name**: `sa`
- **Password**: (deixar vazio)
- Clique em "Connect"

### 3. Executar Queries para Verificar Dados

**Ver todos os clientes:**
```sql
SELECT * FROM CLIENTES;
```

**Ver todos os restaurantes:**
```sql
SELECT * FROM RESTAURANTES;
```

**Ver todos os produtos:**
```sql
SELECT * FROM PRODUTOS;
```

**Ver todos os pedidos:**
```sql
SELECT * FROM PEDIDOS;
```

**Ver pedidos com status específico:**
```sql
SELECT * FROM PEDIDOS WHERE STATUS = 'CONFIRMADO';
```

**Ver clientes com e-mails:**
```sql
SELECT ID, NOME, EMAIL, ATIVO FROM CLIENTES;
```

---

## 🧪 Testando com Postman ou Insomnia

### Importar Collection

1. **Baixe a collection** (arquivo JSON disponível na documentação)
2. **Abra o Postman/Insomnia**
3. **Clique em "Import"**
4. **Cole o arquivo JSON ou selecione o arquivo**
5. **A collection será carregada com todos os endpoints**
6. **Configure a variável de ambiente** `base_url` para `http://localhost:8080/api/v1`
7. **Comece a testar clicando em "Send"**

---

## 📊 Exemplos de Uso Prático

### Fluxo Completo: Criar Pedido e Acompanhar

```bash
# 1. Criar Cliente
CLIENT_ID=$(curl -s -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva","email":"joao@example.com"}' | jq -r '.dados.id')
echo "Cliente criado: $CLIENT_ID"

# 2. Criar Restaurante
REST_ID=$(curl -s -X POST http://localhost:8080/api/v1/restaurantes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Pizzaria","categoria":"Italiana","avaliacao":4.5}' | jq -r '.dados.id')
echo "Restaurante criado: $REST_ID"

# 3. Criar Produto
PROD_ID=$(curl -s -X POST http://localhost:8080/api/v1/produtos \
  -H "Content-Type: application/json" \
  -d "{\"nome\":\"Pizza\",\"categoria\":\"Pizza\",\"restaurante\":{\"id\":$REST_ID}}" | jq -r '.dados.id')
echo "Produto criado: $PROD_ID"

# 4. Criar Pedido
PEDIDO_ID=$(curl -s -X POST http://localhost:8080/api/v1/pedidos \
  -H "Content-Type: application/json" \
  -d "{\"cliente\":{\"id\":$CLIENT_ID},\"valorTotal\":150.50}" | jq -r '.dados.id')
echo "Pedido criado: $PEDIDO_ID"

# 5. Confirmar Pedido
curl -s -X PUT http://localhost:8080/api/v1/pedidos/$PEDIDO_ID/confirmar | jq '.'

# 6. Iniciar Preparação
curl -s -X PUT http://localhost:8080/api/v1/pedidos/$PEDIDO_ID/iniciar-preparacao | jq '.'

# 7. Sair para Entrega
curl -s -X PUT http://localhost:8080/api/v1/pedidos/$PEDIDO_ID/sair-para-entrega | jq '.'

# 8. Entregar
curl -s -X PUT http://localhost:8080/api/v1/pedidos/$PEDIDO_ID/entregar | jq '.'

# 9. Verificar Status Final
curl -s -X GET http://localhost:8080/api/v1/pedidos/$PEDIDO_ID | jq '.'
```

---

## ✅ Checklist de Testes

- [ ] **Clientes**
    - [ ] Criar cliente com dados válidos
    - [ ] Tentar criar com e-mail duplicado (deve falhar)
    - [ ] Listar clientes ativos
    - [ ] Buscar cliente por ID
    - [ ] Atualizar cliente
    - [ ] Inativar cliente
    - [ ] Ativar cliente

- [ ] **Restaurantes**
    - [ ] Criar restaurante com avaliação válida
    - [ ] Tentar com avaliação fora do range (deve falhar)
    - [ ] Listar restaurantes
    - [ ] Buscar por categoria
    - [ ] Buscar por nome
    - [ ] Ativar/Inativar

- [ ] **Produtos**
    - [ ] Criar produto para restaurante ativo
    - [ ] Tentar criar para restaurante inativo (deve falhar)
    - [ ] Listar por restaurante
    - [ ] Filtrar disponíveis/indisponíveis
    - [ ] Ativar/Desativar disponibilidade

- [ ] **Pedidos**
    - [ ] Criar pedido com cliente ativo
    - [ ] Tentar com cliente inativo (deve falhar)
    - [ ] Confirmar pedido (PENDENTE → CONFIRMADO)
    - [ ] Iniciar preparação (CONFIRMADO → EM_PREPARACAO)
    - [ ] Sair para entrega (EM_PREPARACAO → SAIU_PARA_ENTREGA)
    - [ ] Entregar (SAIU_PARA_ENTREGA → ENTREGUE)
    - [ ] Tentar transição inválida (deve falhar)
    - [ ] Cancelar pedido
    - [ ] Listar por status

- [ ] **Banco de Dados**
    - [ ] Acessar console H2
    - [ ] Verificar dados de Clientes
    - [ ] Verificar dados de Restaurantes
    - [ ] Verificar dados de Produtos
    - [ ] Verificar dados de Pedidos

---

## 🔧 Configuração Padrão

| Configuração | Valor |
|-------------|-------|
| **Porta** | `8080` |
| **Banco de dados** | H2 (em memória) |
| **JDBC URL** | `jdbc:h2:mem:testdb` |
| **Profile ativo** | `development` |
| **Contexto da API** | `/api/v1` |

---

## 📚 Documentação Adicional

Para documentação detalhada, consulte:
- `ENDPOINTS_DOCUMENTATION.md` - Referência completa de endpoints
- `RESUMO_CONTROLLERS.md` - Resumo da implementação
- `GUIA_TESTES.md` - Guia com exemplos avançados
- `ENTREGA_FINAL.md` - Checklist de entrega

---

## 👨‍💻 Desenvolvedor

**Marcelo Duarte de Aguiar**
- Ciência da Computação – UniRitter - 2024/1
- Desenvolvido com JDK 21 e Spring Boot 3.5.7

---

## 📝 Licença

Este projeto é fornecido como exemplo educacional.

---

## 🆘 Troubleshooting

### Erro: Porta 8080 já está em uso
```bash
# Listar processo na porta 8080
lsof -i :8080

# Matar processo
kill -9 <PID>

# Ou mudar a porta em application.properties
# server.port=8081
```

### Erro: H2 Console não abre
```
Acesse: http://localhost:8080/h2-console
Se não funcionar, verifique se a aplicação está rodando
```

### Erro: 400 Bad Request em requisições
- Verifique o formato JSON
- Valide os tipos de dados
- Confirme os campos obrigatórios
- Consulte a mensagem de erro retornada

### Erro: 404 Not Found
- Verifique a URL do endpoint
- Verifique se o recurso existe (ID)
- Confirme o base path é `/api/v1`

---

**Última atualização:** 31 de Outubro de 2025, 01:12 AM -03
**Status:** ✅ Pronto para Testes e Validação