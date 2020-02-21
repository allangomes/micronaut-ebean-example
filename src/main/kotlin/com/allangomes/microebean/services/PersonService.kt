package com.allangomes.microebean.services

import com.allangomes.microebean.models.Person
import io.micronaut.context.annotation.Value
import javax.inject.Singleton

@Singleton
class PersonService {

    fun list(): MutableList<Person> {
        return Person.all()
    }

}