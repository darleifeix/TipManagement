**Tips Management API**

API backend para gerenciamento e controle de apostas esportivas, desenvolvida com Java e Spring Boot.

O sistema permite cadastrar apostas, tipsters, casas de apostas e usuários, além de acompanhar resultados, status, odds e estatísticas.

**Tecnologias utilizadas**

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven
* Lombok
* REST API
* JWT Authentication

**Funcionalidades**

## Usuários

* Cadastro de usuários
* Login/autenticação JWT

## Apostas

* Cadastro de apostas
* Atualização de status
* Controle de odds
* Registro de lucro/prejuízo
* Filtro por status
* Histórico de apostas
* Busca por usuário
* Busca por período

## Tipsters

* Cadastro de tipsters
* Associação de apostas
* Controle/visualização de performance por tipster

## Casas de apostas

* Cadastro de bet houses
* Associação com apostas
* Controle/visualização de performance por casa

## Bancas

* Associação com casa de Apostas
* Controle de saldo por casa 


**Arquitetura do projeto**

O projeto segue uma arquitetura baseada em:

* Controllers
* Services
* Repositories
* DTOs/ Mappers
* Entities
* Exceptions

Estrutura simplificada:

```bash
src/main/java
│
├── controller
├── service
├── repository
├── model
│   ├── dto
        ├── Response
        ├── Request
│   ├── enums
│   └── entities
├── mapper
├── security
└── exception
```

**Status das apostas**

O sistema trabalha com os seguintes status:

* PENDING
* WIN
* LOSS
* VOID


**Conceitos aplicados no projeto**

Este projeto foi desenvolvido para prática e aprofundamento em:

* APIs REST
* Arquitetura backend
* Boas práticas com Spring Boot
* Relacionamentos JPA
* DTO Pattern
* Tratamento de exceções
* Autenticação JWT
* Collections e Stream API
* Separação de responsabilidades
* Persistência de dados


**Objetivo do projeto**

O projeto foi criado com foco em estudos e evolução prática no desenvolvimento backend com Java e Spring Boot, simulando funcionalidades reais de um sistema de gerenciamento de apostas esportivas.

Além disso, o sistema também serve como portfólio para demonstrar conhecimentos em:

* Desenvolvimento backend
* APIs REST
* Modelagem de dados
* Segurança com JWT
* Organização de projetos Java
* Boas práticas de desenvolvimento

