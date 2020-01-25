package com.allangomes.microebean.controllers

import com.allangomes.microebean.boot.io
import io.ebean.DB
import io.micronaut.context.annotation.Parameter
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

@Controller("coroutine")
class CoroutineController {

    companion object {
        val log = LoggerFactory.getLogger(CoroutineController::class.java)
    }

    fun sql(delay: Int): String {
        log.info("fetching")
        val res = DB.sqlQuery("SELECT pg_sleep($delay), current_database();").findOne()
        val dbname = res?.getString("current_database") ?: ""
        log.info(dbname)
        return dbname
    }

    @Get("/async/{delay}")
    suspend fun index(@PathVariable(defaultValue = "10") delay: Int): String {
        return withContext(io) { sql(delay) }
    }

    @Get("/sync/{delay}")
    fun sync(@PathVariable(defaultValue = "10") delay: Int): String {
        return sql(delay)
    }

}