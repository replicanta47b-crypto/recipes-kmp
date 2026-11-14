package com.example.restapi

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            module(messaging = null) // no RabbitMQ in tests
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, Ktor!", response.bodyAsText())
    }

    @Test
    fun ratingOfUnratedRecipeIsEmpty() = testApplication {
        application { module(messaging = null) }
        val response = client.get("/recipes/1/rating")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("\"votes\":0"))
    }

    @Test
    fun ratingCannotBeSentWithoutBroker() = testApplication {
        application { module(messaging = null) }
        val response = client.post("/recipes/1/ratings") {
            contentType(ContentType.Application.Json)
            setBody("""{"userId":1,"rating":5}""")
        }
        assertEquals(HttpStatusCode.ServiceUnavailable, response.status)
    }
}
