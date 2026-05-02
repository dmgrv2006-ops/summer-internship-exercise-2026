# Task Scheduling System - Summer Internship Exercise

## O Problema

Numa empresa moderna, há sempre muito trabalho a fazer e nem sempre de forma independente. Algumas tarefas só podem começar depois de outras estarem concluídas — e gerir essas dependências manualmente é uma fonte de caos.

O teu desafio é construir um sistema de agendamento de tarefas que as execute pela ordem correta, respeitando as dependências entre elas.

Cada tarefa:
* Tem um identificador único
* Tem uma prioridade
* Pode depender de outras tarefas
* Tem um estado (`PENDING`, `IN_PROGRESS`, `COMPLETED`)

O sistema deve garantir que:
* Uma tarefa só é executada depois de todas as suas dependências estarem concluídas
* Configurações inválidas (ex: dependências circulares) são detetadas e tratadas adequadamente

## Regras

* Uma tarefa sem dependências pode ser executada imediatamente.
* Uma tarefa só pode transitar para `IN_PROGRESS` se todas as suas dependências estiverem `COMPLETED`.
* Quando múltiplas tarefas estão elegíveis para execução em simultâneo, a prioridade determina a ordem (valores mais baixos têm maior prioridade).
* Dependências circulares são inválidas e devem ser detetadas quando necessário.
* Referências a tarefas inexistentes são inválidas e devem ser detetadas.

## Exemplo

Dado o seguinte conjunto de tarefas:

```
Tarefa A (sem dependências, prioridade 2)
Tarefa B (depende de A, prioridade 2)
Tarefa C (depende de A, prioridade 1)
Tarefa D (depende de B e C, prioridade 3)
```

Uma ordem de execução válida seria: `A → C → B → D`

(C antes de B porque ambas dependem apenas de A estar concluída, e C tem maior prioridade)

## O que é preciso fazer?

* Implementar o método `getExecutionOrder` em `TaskSchedulerService`.
* Implementar o método `getEligibleTasks` em `TaskSchedulerService`.
* Implementar mais testes em `TaskSchedulerServiceTest`

Vai ser valorizado: o facto de fazerem testes extras, assim como, a arquitetura escolhida, a deteção de configurações inválidas e o facto do código estar escrito de forma percetível e organizada.

## Como correr os testes

* Correr `./mvnw test`

## Dúvidas

Quaisquer dúvidas que tenhas, cria um issue aqui no github :)
