package com.allangomes.microebean.boot

import io.ebean.config.ServerConfig
import io.ebean.config.TenantDataSourceProvider
import io.ebean.datasource.DataSourceConfig
import io.ebean.datasource.DataSourcePool
import io.ebean.datasource.core.Factory
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import javax.inject.Singleton
import javax.sql.DataSource

@Singleton
class DataSourceProvider(private val serverConfig: ServerConfig) : TenantDataSourceProvider {

    private val cache = ConcurrentHashMap<Any, DataSource>()

    override fun dataSource(tenantId: Any): DataSource {
        return getOrCreate(tenantId)
    }

    private fun getOrCreate(tenantId: Any): DataSource {
        return cache.computeIfAbsent(tenantId) { createAndMigrateDataSource(tenantId) }
    }

    override fun shutdown(deregisterDriver: Boolean) {
        log.info("Shutdown all DataSources")
        val keys = cache.keys
        keys.forEach(Consumer { tenantId: Any? -> shutdownDataSource(cache.remove(tenantId), deregisterDriver) })
    }

    private fun shutdownDataSource(dataSource: DataSource?, deregisterDriver: Boolean) {
        if (dataSource is DataSourcePool) {
            dataSource.shutdown(deregisterDriver)
        }
    }

    private fun createAndMigrateDataSource(tenantId: Any): DataSource {
        val dataSource = createDataSource(tenantId)
        return serverConfig.runDbMigration(dataSource)
    }

    private fun createDataSource(tenantId: Any): DataSource {
        log.info("Create DataSource for tenantId:{}", tenantId)
        val config = DataSourceConfig()

        config.driver = serverConfig.dataSourceConfig.driver
        config.url = "jdbc:postgresql://localhost:5432/$tenantId"
        config.username = "ag"
        config.password = ""
        return Factory().createPool(tenantId.toString(), config)
    }

    companion object {
        private val log = LoggerFactory.getLogger(DataSourceProvider::class.java)
    }

}