package com.dumpdiary.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    val userId: String,
    val email: String,
    val passwordHash: String,
    val emailVerified: Boolean,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
data class UserProfile(
    val userId: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val updatedAt: String,
)

@Serializable
data class BowelLog(
    val id: String,
    val userId: String,
    val occurredAt: String,
    val dateKey: String,
    val durationSeconds: Int,
    val feeling: String,
    val stoolForm: Int,
    val symptomTags: List<String> = emptyList(),
    val detailNote: String,
    val snapshotDisplayName: String,
    val snapshotAvatarUrl: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean = false,
)

@Serializable
data class SessionResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val profile: UserProfile,
)

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val code: String,
)

@Serializable
data class SendEmailCodeRequest(
    val email: String,
    val purpose: VerificationPurpose,
)

@Serializable
data class VerifyEmailCodeRequest(
    val email: String,
    val code: String,
    val purpose: VerificationPurpose,
)

@Serializable
data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPassword: String,
)

@Serializable
data class RefreshRequest(
    val refreshToken: String,
)

@Serializable
data class MessageResponse(
    val message: String,
)

@Serializable
data class VerifyCodeResponse(
    val valid: Boolean,
)

@Serializable
data class UpdateProfileRequest(
    val displayName: String,
)

@Serializable
data class AvatarResponse(
    val avatarUrl: String,
)

@Serializable
data class AddFriendRequest(
    val email: String,
)

@Serializable
data class FriendProfile(
    val userId: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String? = null,
)

@Serializable
data class MonthlySummary(
    val month: String,
    val totalCount: Int,
    val activeDays: Int,
)

@Serializable
data class StreakSummary(
    val currentStreakDays: Int,
    val maxStreakDays: Int,
)

@Serializable
data class YearlyTrendPoint(
    val month: Int,
    val count: Int,
)

@Serializable
data class AppVersionInfo(
    val versionCode: Int,
    val versionName: String,
    val downloadPath: String,
    val notes: String = "",
)

@Serializable
enum class VerificationPurpose {
    REGISTER,
    RESET_PASSWORD,
}
