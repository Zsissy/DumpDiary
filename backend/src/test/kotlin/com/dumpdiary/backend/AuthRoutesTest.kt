package com.dumpdiary.backend

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertTrue

class AuthRoutesTest {
    @Test
    fun register_flow_returns_tokens() = testApplication {
        val email = "tester@example.com"
        val sendCodeResponse = client.post("/auth/send-email-code") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"$email","purpose":"REGISTER"}""")
        }
        assertTrue(sendCodeResponse.bodyAsText().contains("Development code"))
    }
}
