package com.allangomes.microebean.controllers

import com.allangomes.microebean.models.Person
import io.micronaut.http.annotation.*
import javax.inject.Singleton

@Singleton
@Controller("/person")
open class EquipmentController {

    @Get
    open fun index(): MutableList<Person> {
        return Person.all()
    }

    @Get("/byName")
    open fun byName(@QueryValue("name") name: String): Person? {
        return Person.byName(name)
    }


    @Post
    open fun save(@Body person: Person): Person {
        person.save()
        return person
    }

}