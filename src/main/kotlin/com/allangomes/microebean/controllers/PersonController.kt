package com.allangomes.microebean.controllers

import com.allangomes.microebean.models.Person
import com.allangomes.microebean.services.PersonService
import io.ebean.config.CurrentTenantProvider
import io.micronaut.http.annotation.*
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Controller("/person")
class PersonController(
        val personService: PersonService
) {

    @Get
    fun index(): MutableList<Person> = personService.list()

    @Get("/byName")
    fun byName(@QueryValue("name") name: String): Person? {
        return Person.byName(name)
    }

    @Post
    fun save(@Body person: Person): CompletableFuture<Person> {
        return CompletableFuture.supplyAsync {
            person.save()
            return@supplyAsync person
        }
    }

}