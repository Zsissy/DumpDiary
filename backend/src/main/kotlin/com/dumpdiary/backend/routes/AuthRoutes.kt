package com.dumpdiary.backend.routes

import com.dumpdiary.backend.model.AuthRequest
import com.dumpdiary.backend.model.MessageResponse
import com.dumpdiary.backend.model.RefreshRequest
import com.dumpdiary.backend.model.RegisterRequest
import com.dumpdiary.backend.model.ResetPasswordRequest
import com.dumpdiary.backend.model.SendEmailCodeRequest
import com.dumpdiary.backend.model.VerifyEmailCodeRequest
import com.dumpdiary.backend.service.AuthService
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun io.ktor.server.routing.Route.authRoutes(authService: AuthService) {
    route("/auth") {
        post("/register") {
            call.respond(authService.register(call.receive<RegisterRequest>()))
        }
        post("/login") {
            call.respond(authService.login(call.receive<AuthRequest>()))
        }
        post("/send-email-code") {
            call.respond(authService.sendEmailCode(call.receive<SendEmailCodeRequest>()))
        }
        post("/verify-email-code") {
            call.respond(authService.verifyEmailCode(call.receive<VerifyEmailCodeRequest>()))
        }
        post("/reset-password") {
            call.respond(authService.resetPassword(call.receive<ResetPasswordRequest>()))
        }
        post("/refresh") {
            call.respond(authService.refresh(call.receive<RefreshRequest>()))
        }
        post("/logout") {
            val refreshToken = call.request.headers["X-Refresh-Token"]
            val accessToken = call.request.headers["Authorization"]?.removePrefix("Bearer ")?.trim()
            call.respond(authService.logout(accessToken, refreshToken))
        }
    }
}
