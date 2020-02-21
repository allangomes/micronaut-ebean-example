package com.allangomes.microebean.models

import com.allangomes.microebean.models.query.QPerson
import io.ebean.Finder
import io.ebean.Model
import io.ebean.annotation.DbDefault
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
open class Person : Model()  {

    @Id
    var id: Long = 0

    var name: String? = ""

    @DbDefault("1000-01-02")
    lateinit var dateBirth: LocalDate

    companion object Find : Finder<Long, Person>(Person::class.java) {

        fun byName(name: String) = QPerson()
                    .name.equalTo(name)
                    .findOne()
    }

}
