package com.dumpdiary.backend.routes

import com.dumpdiary.backend.model.AddFriendRequest
import com.dumpdiary.backend.model.MessageResponse
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.security.AuthenticatedUser
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Route.friendRoutes(store: InMemoryStore) {
    get("/friends") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        call.respond(store.visibleFriendProfiles(userId))
    }

    post("/friends") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val request = call.receive<AddFriendRequest>()
        require(request.email.isNotBlank()) { "Friend email is required." }
        val friendUserId = store.resolveUserIdByEmail(request.email) ?: error("Friend email is not registered.")
        store.addFriendship(userId, friendUserId)
        call.respond(MessageResponse("Friend connected."))
    }
}
