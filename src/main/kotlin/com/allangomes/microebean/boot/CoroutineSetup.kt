package com.allangomes.microebean.boot

import io.micronaut.context.annotation.Context
import io.micronaut.scheduling.TaskExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.ExecutorService
import javax.annotation.PostConstruct
import javax.inject.Inject
import javax.inject.Named


var io = Dispatchers.IO
    private set


@Context
class Init {

    @Named(TaskExecutors.IO)
    @Inject
    lateinit var ioExecutor: ExecutorService

    @PostConstruct
    fun after() {
        io = ioExecutor.asCoroutineDispatcher()
    }

}