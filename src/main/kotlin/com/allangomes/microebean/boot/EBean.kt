package com.allangomes.microebean.boot

import io.ebean.EbeanServerFactory
import io.ebean.config.CurrentTenantProvider
import io.ebean.config.ServerConfig
import io.ebean.config.TenantMode
import io.ebean.config.dbplatform.postgres.PostgresPlatform
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Value
import io.micronaut.multitenancy.tenantresolver.TenantResolver

@Factory
class EBean {

    @Bean
    fun currentTenant(
            tenantResolver: TenantResolver,
            @Value("\${datasource.default:db}") default: String
    ) = CurrentTenantProvider {
        return@CurrentTenantProvider try {
            tenantResolver.resolveTenantIdentifier()
        } catch (e: Exception) {
            default
        }
    }

    @Bean
    fun config(currentTenant: CurrentTenantProvider) = ServerConfig().apply {
        loadFromProperties()
        tenantMode = TenantMode.DB
        databasePlatform = PostgresPlatform()
        currentTenantProvider = currentTenant
    }

    @Context
    class Init(config: ServerConfig, dataSourceProvider: DataSourceProvider) {

        init {
            config.tenantDataSourceProvider = dataSourceProvider
            EbeanServerFactory.create(config)
        }

    }

}