# Tabela de Estados da Conta

| Estado Atual   | Evento Recebido   | Novo Estado   | Ação/Observação                                   |
|---------------|-------------------|---------------|---------------------------------------------------|
| PENDING_KYC    | KycApproved       | APPROVED      | Conta aprovada, gera evento AccountOpened         |
| PENDING_KYC    | KycRejected       | REJECTED      | Conta rejeitada, gera evento AccountRejected      |
| APPROVED       | KycApproved       | APPROVED      | Ignorado (idempotente)                            |
| APPROVED       | KycRejected       | APPROVED      | Ignorado (idempotente)                            |
| REJECTED       | KycApproved       | REJECTED      | Ignorado (idempotente)                            |
| REJECTED       | KycRejected       | REJECTED      | Ignorado (idempotente)                            |

**Observações:**
- Só transita de `PENDING_KYC` para `APPROVED` ou `REJECTED`.
- Qualquer evento recebido após estado final é ignorado (idempotência).
- Idempotência: Inbox → (EventId + ClientId), Outbox → (EventId), TTL memória 3000ms.
- O produtor nunca consome seu próprio evento (exceto se explicitamente permitido).

---

# Matriz de Falhas

| Cenário                              | Possível Falha                | Estratégia de Mitigação                |
|--------------------------------------|-------------------------------|----------------------------------------|
| Evento duplicado                     | Processamento repetido        | Idempotência por estado e EventId      |
| Evento fora de ordem                 | Estado inconsistente          | Validar transição antes de aplicar     |
| Timeout na entrega do evento         | Perda de evento               | Retry e TTL na inbox/outbox            |
| Produtor consumindo próprio evento   | Loop infinito                 | Bloquear consumo pelo produtor         |
| Falha de persistência                | Perda de estado               | Transação e persistência confiável     |
| Evento de KYC após estado final      | Estado inválido               | Ignorar evento (idempotência)          |
| Falha de deduplicação                | Estado duplicado              | TTL e chave composta na inbox/outbox   |

