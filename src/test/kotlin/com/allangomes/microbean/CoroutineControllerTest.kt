package com.allangomes.microbean

import ch.tutteli.atrium.api.cc.en_GB.isGreaterThan
import ch.tutteli.atrium.api.cc.en_GB.isLessThan
import ch.tutteli.atrium.api.cc.en_GB.toBe
import ch.tutteli.atrium.verbs.expect
import io.micronaut.http.HttpRequest.GET
import io.micronaut.http.MediaType
import io.micronaut.http.client.DefaultHttpClient
import io.micronaut.http.client.HttpClient
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.net.URL
import java.util.concurrent.Executors


object CoroutineControllerSpek : Spek({

    describe("CoroutineController Suite") {

        val client: HttpClient = DefaultHttpClient(URL("http://localhost:9950"))

        it("test /async must be lest than 8s") { runBlocking {

            val startTime = System.currentTimeMillis()

            val jobs = withContext(Executors.newFixedThreadPool(2).asCoroutineDispatcher()) {

                listOf(
                        launch {
                            val result = client.retrieve(
                                    GET<String>("/coroutine/async/4")
                                            .accept(MediaType.TEXT_PLAIN_TYPE)
                                            .headers {
                                                it.add("tenantId", "db")
                                            }
                            ).awaitFirstOrDefault("")

                            expect(result) {
                                toBe("db")
                            }
                        }.apply { start() },
                        launch {
                            val result = client.retrieve(
                                    GET<String>("/coroutine/async/4")
                                            .accept(MediaType.TEXT_PLAIN_TYPE)
                                            .headers {
                                                it.add("tenantId", "db2")
                                            }
                            ).awaitFirstOrDefault("")

                            expect(result) {
                                toBe("db2")
                            }
                        }.apply { start() }
                )
            }

            jobs.joinAll()

            expect(System.currentTimeMillis() - startTime) {
                isLessThan(8000L)
            }
        }}

        it("test /sync must be grater than 8s") { runBlocking {

            val startTime = System.currentTimeMillis()

            val jobs = withContext(Executors.newFixedThreadPool(2).asCoroutineDispatcher()) {

                listOf(
                        launch {
                            val result = client.retrieve(
                                    GET<String>("/coroutine/sync/4")
                                            .accept(MediaType.TEXT_PLAIN_TYPE)
                                            .headers {
                                                it.add("tenantId", "db")
                                            }
                            ).awaitFirstOrDefault("")

                            expect(result) {
                                toBe("db")
                            }
                        }.apply { start() },
                        launch {
                            val result = client.retrieve(
                                    GET<String>("/coroutine/sync/4")
                                            .accept(MediaType.TEXT_PLAIN_TYPE)
                                            .headers {
                                                it.add("tenantId", "db2")
                                            }
                            ).awaitFirstOrDefault("")

                            expect(result) {
                                toBe("db2")
                            }
                        }.apply { start() }
                )
            }

            jobs.joinAll()

            expect(System.currentTimeMillis() - startTime) {
                isGreaterThan(8000L)
            }
        }}



        afterGroup {
            client.close()
        }
    }
})