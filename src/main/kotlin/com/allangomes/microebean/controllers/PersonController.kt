package com.allangomes.microebean.controllers

import com.allangomes.microebean.models.Person
import io.micronaut.http.annotation.*
import java.util.concurrent.CompletableFuture
import javax.inject.Singleton

@Singleton
@Controller("/person")
class PersonController {

    @Get
    fun index(): MutableList<Person> {
        return Person.all()
    }

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