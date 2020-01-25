package com.allangomes.microebean.models

import com.allangomes.microebean.models.query.QPerson
import io.ebean.Finder
import io.ebean.Model
import io.micronaut.core.annotation.Introspected
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Introspected
open class Person : Model()  {

    @Id
    var id: Long = 0

    var name: String? = null


    companion object Find : Finder<Long, Person>(Person::class.java) {

        fun byName(name: String) = QPerson()
                    .name.equalTo(name)
                    .findOne()

    }

}
