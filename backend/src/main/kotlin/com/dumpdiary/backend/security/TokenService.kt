package com.dumpdiary.backend.security

import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.repository.RefreshTokenRecord
import io.ktor.server.auth.Principal
import java.util.UUID

data class AuthenticatedUser(val userId: String) : Principal

class TokenService(private val store: InMemoryStore) {
    fun issueAccessToken(userId: String): String {
        val token = "access-${UUID.randomUUID()}"
        store.putAccessToken(token, userId)
        return token
    }

    fun issueRefreshToken(userId: String): String {
        val token = "refresh-${UUID.randomUUID()}"
        store.putRefreshToken(token, RefreshTokenRecord(userId = userId, token = token))
        return token
    }

    fun resolveAccessToken(token: String): String? = store.resolveAccessToken(token)

    fun refresh(refreshToken: String): String? = store.getRefreshToken(refreshToken)?.userId?.let(::issueAccessToken)

    fun revoke(accessToken: String?, refreshToken: String?) {
        store.removeAccessToken(accessToken)
        store.removeRefreshToken(refreshToken)
    }
}
