package com.dumpdiary.backend

import com.dumpdiary.backend.model.AppVersionInfo
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.routes.authRoutes
import com.dumpdiary.backend.routes.friendRoutes
import com.dumpdiary.backend.routes.logRoutes
import com.dumpdiary.backend.routes.profileRoutes
import com.dumpdiary.backend.routes.statsRoutes
import com.dumpdiary.backend.security.AuthenticatedUser
import com.dumpdiary.backend.security.TokenService
import com.dumpdiary.backend.service.AuthService
import com.dumpdiary.backend.service.EmailSender
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.auth.principal
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.defaultheaders.DefaultHeaders
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.http.content.files
import io.ktor.server.http.content.static
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    val dataDir = resolveDataDirectory()
    val store = InMemoryStore(File(dataDir, "store.json"))
    val tokenService = TokenService(store)
    val emailSender = EmailSender()
    val authService = AuthService(store, tokenService, emailSender)
    val uploadDir = File("uploads").apply { mkdirs() }
    val apkDir = resolveApkDirectory()
    val latestVersionCode = System.getenv("DUMPDIARY_LATEST_VERSION_CODE")?.toIntOrNull() ?: 2
    val latestVersionName = System.getenv("DUMPDIARY_LATEST_VERSION_NAME").orEmpty().ifBlank { "1.1" }
    val latestVersionNotes = System.getenv("DUMPDIARY_LATEST_VERSION_NOTES").orEmpty()

    install(DefaultHeaders)
    install(Compression)
    install(PartialContent)
    install(CallLogging)
    install(CORS) {
        anyHost()
        allowHeader("Authorization")
        allowHeader("Content-Type")
        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)
    }
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
            },
        )
    }
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("message" to (cause.message ?: "Bad request")))
        }
        exception<io.ktor.server.plugins.NotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("message" to (cause.message ?: "Not found")))
        }
        exception<IllegalStateException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, mapOf("message" to (cause.message ?: "Conflict")))
        }
        exception<Throwable> { call, cause ->
            this@module.environment.log.error("Unhandled error on ${call.request.path()}", cause)
            call.respond(HttpStatusCode.InternalServerError, mapOf("message" to "Internal server error"))
        }
    }
    install(Authentication) {
        bearer("auth-bearer") {
            authenticate { credential ->
                tokenService.resolveAccessToken(credential.token)?.let { userId ->
                    AuthenticatedUser(userId)
                }
            }
        }
    }

    routing {
        get("/") {
            call.respond(mapOf("service" to "DumpDiary backend", "status" to "ok"))
        }
        get("/app/version") {
            call.respond(
                AppVersionInfo(
                    versionCode = latestVersionCode,
                    versionName = latestVersionName,
                    downloadPath = "/downloads/app-debug.apk",
                    notes = latestVersionNotes,
                ),
            )
        }
        authRoutes(authService)
        authenticate("auth-bearer") {
            route("/me") {
                profileRoutes(store, uploadDir)
            }
            friendRoutes(store)
            logRoutes(store)
            statsRoutes(store)
        }
        static("/uploads") {
            files(uploadDir)
        }
        static("/downloads") {
            files(apkDir)
        }
    }
}

private fun resolveApkDirectory(): File {
    val direct = File("app/build/outputs/apk/debug")
    if (direct.exists()) return direct
    val parent = File("../app/build/outputs/apk/debug")
    if (parent.exists()) return parent
    return direct
}

private fun resolveDataDirectory(): File {
    val direct = File("backend/data")
    if (direct.exists() || direct.parentFile?.exists() == true) return direct.apply { mkdirs() }
    return File("data").apply { mkdirs() }
}
