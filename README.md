# Sistema de Microsserviços - Travel System

Este projeto implementa um sistema de microsserviços para compra de passagens aéreas usando Java Spring Boot e Docker.

## Arquitetura

O sistema é composto por 4 microsserviços:

1. **IMDTravel** (Porta 8080) - Serviço principal que orquestra a compra de passagens
2. **AirlinesHub** (Porta 8081) - Gerencia informações de voos e vendas
3. **Exchange** (Porta 8082) - Fornece taxas de conversão de moedas
4. **Fidelity** (Porta 8083) - Gerencia pontos de fidelidade

## Fluxo do Sistema

1. Usuário → IMDTravel: POST `/api/buyTicket`
2. IMDTravel → AirlinesHub: GET `/api/flight` (consulta voo)
3. IMDTravel → Exchange: GET `/api/convert` (obtém taxa de câmbio)
4. IMDTravel → AirlinesHub: POST `/api/sell` (vende passagem)
5. IMDTravel → Fidelity: POST `/api/bonus` (adiciona pontos de fidelidade)

## Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.6+
- Docker e Docker Compose

### Execução Rápida
```bash

# Linux/Mac
chmod +x build-and-run.sh
./build-and-run.sh
```

### Execução Manual

1. **Build dos microsserviços:**
```bash
mvn clean package -DskipTests
```

2. **Executar com Docker Compose:**
```bash
docker-compose up --build
```

## Testando o Sistema

### Exemplo de Requisição
```bash
curl -X POST http://localhost:8080/api/buyTicket \
  -H "Content-Type: application/json" \
  -d '{
    "flight": "AA123",
    "day": "2024-01-15",
    "user": 123
  }'
```

### Resposta Esperada
```json
{
  "success": true,
  "transactionId": "uuid-da-transacao",
  "message": "Ticket purchased successfully"
}
```

## Endpoints Disponíveis

### IMDTravel (8080)
- `POST /api/buyTicket` - Compra uma passagem

### AirlinesHub (8081)
- `GET /api/flight?flight={flight}&day={day}` - Consulta informações do voo
- `POST /api/sell` - Vende uma passagem

### Exchange (8082)
- `GET /api/convert` - Obtém taxa de conversão USD para BRL

### Fidelity (8083)
- `POST /api/bonus` - Adiciona pontos de fidelidade