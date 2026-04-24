package com.dumpdiary.backend.routes

import com.dumpdiary.backend.model.AvatarResponse
import com.dumpdiary.backend.model.UpdateProfileRequest
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.security.AuthenticatedUser
import io.ktor.http.ContentDisposition
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.Route
import kotlinx.datetime.Clock
import java.io.File
import java.util.UUID

fun Route.profileRoutes(store: InMemoryStore, uploadDir: File) {
    get("/profile") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        call.respond(store.getProfile(userId) ?: error("Profile not found."))
    }

    put("/profile") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val current = store.getProfile(userId) ?: error("Profile not found.")
        val request = call.receiveNullable<UpdateProfileRequest>() ?: error("Body is required.")
        val updated = current.copy(displayName = request.displayName, updatedAt = Clock.System.now().toString())
        store.putProfile(userId, updated)
        call.respond(updated)
    }

    post("/avatar") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val current = store.getProfile(userId) ?: error("Profile not found.")
        var uploadedFileUrl: String? = null
        val multipart = call.receiveMultipart()
        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val safeName = part.originalFileName?.substringAfterLast("/")?.substringAfterLast("\\") ?: "avatar.jpg"
                val fileName = "${UUID.randomUUID()}-$safeName"
                val file = File(uploadDir, fileName)
                part.streamProvider().use { input ->
                    file.outputStream().buffered().use { output -> input.copyTo(output) }
                }
                uploadedFileUrl = "/uploads/$fileName"
            }
            part.dispose()
        }
        val avatarUrl = requireNotNull(uploadedFileUrl) { "Avatar file is required." }
        val updated = current.copy(avatarUrl = avatarUrl, updatedAt = Clock.System.now().toString())
        store.putProfile(userId, updated)
        call.respond(AvatarResponse(avatarUrl = avatarUrl))
    }
}
