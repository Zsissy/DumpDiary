package com.dumpdiary.backend.routes

import com.dumpdiary.backend.model.BowelLog
import com.dumpdiary.backend.model.MessageResponse
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.security.AuthenticatedUser
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import kotlinx.datetime.Clock

fun Route.logRoutes(store: InMemoryStore) {
    get("/logs") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val logs = store.visibleLogs(userId).sortedByDescending { it.occurredAt }
        call.respond(logs)
    }

    post("/logs") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val log = call.receive<BowelLog>()
        require(log.userId == userId) { "User mismatch." }
        store.upsertLog(userId, log)
        call.respond(log)
    }

    put("/logs/{id}") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val id = call.parameters["id"] ?: error("Missing log id.")
        val log = call.receive<BowelLog>()
        require(log.id == id) { "Log id mismatch." }
        require(log.userId == userId) { "User mismatch." }
        val updated = log.copy(updatedAt = Clock.System.now().toString())
        store.upsertLog(userId, updated)
        call.respond(store.getLog(userId, id)!!)
    }

    delete("/logs/{id}") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val id = call.parameters["id"] ?: error("Missing log id.")
        val current = store.getLog(userId, id) ?: error("Log not found.")
        val deleted = current.copy(isDeleted = true, updatedAt = Clock.System.now().toString())
        store.upsertLog(userId, deleted)
        call.respond(MessageResponse("Deleted"))
    }
}
