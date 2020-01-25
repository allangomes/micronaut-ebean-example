# micronaut-ebean-example
Micronaut + Ebean + Kotlin + Multitenancy + Coroutines

Este é projeto modelo utilizando as melhores tecnologias de 2020

## Tecnologias

### Micronaut
Micronaut é um framework concorrente do spring-boot que tem varias vantagens sobre seu concorrente

- Resolve as injeções de dependencia em tempo de compilação
- Extremamente mais leve
- A aplicação sobe questão de milissegundos
- Não bloqueante e Reativo
- Tem varios modulos prontos para se integrar com:
  - Filas (Rabbit/Kafka)
  - Discovery (Consul,Eureka)
  - Cache (Redis,MemCache)
  - Server (AWS Lambda)
  - Banco de dados
  - Suporte a multitenancy (Nada fácil quando se trabalha com threads non-blocking)
  - mais em: https://micronaut.io/
  
###  Ebean
Ebean é um ORM concorrente do Hibernate

- Suas consultas são 10^1000000x melhores que a do Hibernante
- Select + 1 (aqui não)
- Se integra muito bem com o Kotlin
- Gera automaticamente as migrations
- Tem suporte a multitenant mesmo em aplicações non-blocking
- mais em: https://ebean.io/

### Kotlin
Kotlin é uma linguagem interopativel com Java com features que não existem no java

- Sintaxe Amigável
- Sem Get e Set o///
- **Coroutines** (programação assíncrona)
- Extensions Methods and Props
- Delegates
- Type inference
- Global var and functions
- mais em: https://kotlinlang.org/



## Detalhes técnicos

### multitenancy

É bem comum vermos aplicações hoje dia que precisa se conectar com varios bancos, um para cada cliente,
normalmente utilizando o Spring-Boot é algo mais simples precisando apenas criar um ThreadMap e setar as 
configurações daquele tenant em um Http Filter.

Mas e quando temos alguns pools de threads compartilhados e trabalhamos de forma completamente asyncrona?
de fato não existe uma solução muito simples, mas felizmente o micronaut já nos dar um suporte para 
implementarmos essa solução.

passo 1:
```gradle
implementation "io.micronaut:micronaut-multitenancy"
```

passo 2:
```yml
micronaut.multitenancy.tenantresolver.httpheader.enabled: true
```

e pronto, agora como usar?

```http
GET /coroutine/async/5 HTTP/1.1
Host: localhost:9950
tenantId: cliente
```

e como recuperar esse tenant em thread safe?
```kotlin
@Inject
lateinit var tenantResolver: TenantResolver
...
tenantResolver.resolveTenantIdentifier()
```

como se integrar com o banco de dados, no caso o Ebean?

Implemente um singleton ou um bean de `io.ebean.config.CurrentTenantProvider`
```kotlin
@Bean
fun currentTenant(
        tenantResolver: TenantResolver,
        @Value("\${datasource.default:db}") default: String
) = CurrentTenantProvider {
    return@CurrentTenantProvider try {
        tenantResolver.resolveTenantIdentifier() // <---- A magica acontece aqui
    } catch (e: Exception) {
        default
    }
}
```

```kotlin
@Bean
fun config(currentTenant: CurrentTenantProvider) = ServerConfig().apply {
    currentTenantProvider = currentTenant
    ...
}
```

## Setup

Em breve
  

