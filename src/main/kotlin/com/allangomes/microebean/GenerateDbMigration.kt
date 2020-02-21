package com.allangomes.microebean

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration
import java.io.IOException

object GenerateDbMigration {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val dbMigration = DbMigration.create()
        dbMigration.setPlatform(Platform.POSTGRES)
        dbMigration.generateMigration()
    }
}