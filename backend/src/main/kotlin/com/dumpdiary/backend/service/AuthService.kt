package com.dumpdiary.backend.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.dumpdiary.backend.model.AuthRequest
import com.dumpdiary.backend.model.MessageResponse
import com.dumpdiary.backend.model.RefreshRequest
import com.dumpdiary.backend.model.RegisterRequest
import com.dumpdiary.backend.model.ResetPasswordRequest
import com.dumpdiary.backend.model.SendEmailCodeRequest
import com.dumpdiary.backend.model.SessionResponse
import com.dumpdiary.backend.model.UserAccount
import com.dumpdiary.backend.model.UserProfile
import com.dumpdiary.backend.model.VerifyCodeResponse
import com.dumpdiary.backend.model.VerifyEmailCodeRequest
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.repository.VerificationCodeRecord
import com.dumpdiary.backend.security.TokenService
import kotlinx.datetime.Clock
import java.util.UUID
import kotlin.random.Random

class AuthService(
    private val store: InMemoryStore,
    private val tokenService: TokenService,
    private val emailSender: EmailSender,
) {
    fun register(request: RegisterRequest): SessionResponse {
        require(request.email.isNotBlank()) { "Email is required." }
        require(request.password.length >= 8) { "Password must be at least 8 characters." }
        require(verifyCodeInternal(request.email, request.code, request = com.dumpdiary.backend.model.VerificationPurpose.REGISTER)) {
            "Verification code is invalid or expired."
        }
        check(!store.containsAccount(request.email)) { "Email already registered." }
        val now = Clock.System.now().toString()
        val userId = UUID.randomUUID().toString()
        val passwordHash = BCrypt.withDefaults().hashToString(12, request.password.toCharArray())
        val account = UserAccount(
            userId = userId,
            email = request.email,
            passwordHash = passwordHash,
            emailVerified = true,
            status = "ACTIVE",
            createdAt = now,
            updatedAt = now,
        )
        val profile = UserProfile(
            userId = userId,
            displayName = request.email.substringBefore("@"),
            avatarUrl = null,
            updatedAt = now,
        )
        store.putAccount(request.email, account)
        store.putProfile(userId, profile)
        return createSession(account, profile)
    }

    fun login(request: AuthRequest): SessionResponse {
        val account = store.getAccount(request.email) ?: error("Invalid email or password.")
        val verifyResult = BCrypt.verifyer().verify(request.password.toCharArray(), account.passwordHash)
        require(verifyResult.verified) { "Invalid email or password." }
        val profile = store.getProfile(account.userId) ?: error("Profile not found.")
        return createSession(account, profile)
    }

    fun sendEmailCode(request: SendEmailCodeRequest): MessageResponse {
        require(request.email.isNotBlank()) { "Email is required." }
        if (request.purpose == com.dumpdiary.backend.model.VerificationPurpose.RESET_PASSWORD) {
            require(store.containsAccount(request.email)) { "Email is not registered." }
        }
        val code = Random.nextInt(100_000, 999_999).toString()
        store.putVerificationCode("${request.purpose}:${request.email}", VerificationCodeRecord(
            email = request.email,
            code = code,
            purpose = request.purpose,
            expiresAtMillis = System.currentTimeMillis() + 10 * 60 * 1000,
        ))
        val purposeLabel = when (request.purpose) {
            com.dumpdiary.backend.model.VerificationPurpose.REGISTER -> "registration"
            com.dumpdiary.backend.model.VerificationPurpose.RESET_PASSWORD -> "password reset"
        }
        val result = emailSender.sendVerificationCode(
            email = request.email,
            code = code,
            purposeLabel = purposeLabel,
        )
        return MessageResponse(result.detail)
    }

    fun verifyEmailCode(request: VerifyEmailCodeRequest): VerifyCodeResponse =
        VerifyCodeResponse(valid = verifyCodeInternal(request.email, request.code, request.purpose))

    fun resetPassword(request: ResetPasswordRequest): MessageResponse {
        val account = store.getAccount(request.email) ?: error("Email is not registered.")
        require(
            verifyCodeInternal(
                email = request.email,
                code = request.code,
                request = com.dumpdiary.backend.model.VerificationPurpose.RESET_PASSWORD,
            ),
        ) { "Verification code is invalid or expired." }
        val now = Clock.System.now().toString()
        val updated = account.copy(
            passwordHash = BCrypt.withDefaults().hashToString(12, request.newPassword.toCharArray()),
            updatedAt = now,
        )
        store.putAccount(request.email, updated)
        return MessageResponse("Password has been reset.")
    }

    fun refresh(request: RefreshRequest): SessionResponse {
        val accessToken = tokenService.refresh(request.refreshToken) ?: error("Refresh token is invalid.")
        val userId = tokenService.resolveAccessToken(accessToken) ?: error("Unable to resolve user.")
        val account = store.findAccountByUserId(userId) ?: error("User not found.")
        val profile = store.getProfile(userId) ?: error("Profile not found.")
        return SessionResponse(
            accessToken = accessToken,
            refreshToken = request.refreshToken,
            userId = account.userId,
            email = account.email,
            profile = profile,
        )
    }

    fun logout(accessToken: String?, refreshToken: String?): MessageResponse {
        tokenService.revoke(accessToken, refreshToken)
        return MessageResponse("Logged out.")
    }

    private fun createSession(account: UserAccount, profile: UserProfile): SessionResponse =
        SessionResponse(
            accessToken = tokenService.issueAccessToken(account.userId),
            refreshToken = tokenService.issueRefreshToken(account.userId),
            userId = account.userId,
            email = account.email,
            profile = profile,
        )

    private fun verifyCodeInternal(
        email: String,
        code: String,
        request: com.dumpdiary.backend.model.VerificationPurpose,
    ): Boolean {
        val key = "$request:$email"
        val record = store.getVerificationCode(key) ?: return false
        val valid = record.code == code && record.expiresAtMillis >= System.currentTimeMillis()
        if (valid) {
            store.removeVerificationCode(key)
        }
        return valid
    }
}
