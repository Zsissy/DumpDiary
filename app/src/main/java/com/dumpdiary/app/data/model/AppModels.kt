package com.dumpdiary.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val userId: String,
    val displayName: String,
    val avatarUrl: String? = null,
    val updatedAt: String,
)

@Serializable
data class SessionDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val profile: UserProfileDto,
)

@Serializable
data class AuthRequestDto(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String,
    val code: String,
)

@Serializable
data class SendEmailCodeRequestDto(
    val email: String,
    val purpose: VerificationPurposeDto,
)

@Serializable
data class VerifyEmailCodeRequestDto(
    val email: String,
    val code: String,
    val purpose: VerificationPurposeDto,
)

@Serializable
data class ResetPasswordRequestDto(
    val email: String,
    val code: String,
    val newPassword: String,
)

@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)

@Serializable
data class MessageDto(
    val message: String,
)

@Serializable
data class VerifyCodeDto(
    val valid: Boolean,
)

@Serializable
data class UpdateProfileRequestDto(
    val displayName: String,
)

@Serializable
data class AvatarResponseDto(
    val avatarUrl: String,
)

@Serializable
data class AddFriendRequestDto(
    val email: String,
)

@Serializable
data class FriendProfileDto(
    val userId: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String? = null,
)

@Serializable
data class MonthlySummaryDto(
    val month: String,
    val totalCount: Int,
    val activeDays: Int,
)

@Serializable
data class StreakSummaryDto(
    val currentStreakDays: Int,
    val maxStreakDays: Int,
)

@Serializable
data class YearlyTrendPointDto(
    val month: Int,
    val count: Int,
)

@Serializable
data class AppVersionDto(
    val versionCode: Int,
    val versionName: String,
    val downloadPath: String,
    val notes: String = "",
)

@Serializable
enum class VerificationPurposeDto {
    @SerialName("REGISTER")
    REGISTER,

    @SerialName("RESET_PASSWORD")
    RESET_PASSWORD,
}

@Entity(tableName = "profile")
data class UserProfileEntity(
    @PrimaryKey val userId: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String?,
    val updatedAt: String,
)

@Entity(tableName = "bowel_logs")
data class BowelLogEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val occurredAt: String,
    val dateKey: String,
    val durationSeconds: Int,
    val feeling: String,
    val stoolForm: Int,
    val symptomTagsRaw: String,
    val detailNote: String,
    val snapshotDisplayName: String,
    val snapshotAvatarUrl: String?,
    val createdAt: String,
    val updatedAt: String,
    val isDeleted: Boolean,
    val pendingSyncAction: String?,
)

fun UserProfileDto.toEntity(email: String) = UserProfileEntity(
    userId = userId,
    email = email,
    displayName = displayName,
    avatarUrl = avatarUrl,
    updatedAt = updatedAt,
)

fun UserProfileEntity.toDto() = UserProfileDto(
    userId = userId,
    displayName = displayName,
    avatarUrl = avatarUrl,
    updatedAt = updatedAt,
)

fun BowelLogEntity.toDto() = BowelLogDto(
    id = id,
    userId = userId,
    occurredAt = occurredAt,
    dateKey = dateKey,
    durationSeconds = durationSeconds,
    feeling = feeling,
    stoolForm = stoolForm,
    symptomTags = symptomTagsRaw.toSymptomTags(),
    detailNote = detailNote,
    snapshotDisplayName = snapshotDisplayName,
    snapshotAvatarUrl = snapshotAvatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isDeleted = isDeleted,
)

fun BowelLogDto.toEntity(syncAction: String? = null) = BowelLogEntity(
    id = id,
    userId = userId,
    occurredAt = occurredAt,
    dateKey = dateKey,
    durationSeconds = durationSeconds,
    feeling = feeling,
    stoolForm = stoolForm,
    symptomTagsRaw = symptomTags.encodeSymptomTags(),
    detailNote = detailNote,
    snapshotDisplayName = snapshotDisplayName,
    snapshotAvatarUrl = snapshotAvatarUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    isDeleted = isDeleted,
    pendingSyncAction = syncAction,
)

@Serializable
data class BowelLogDto(
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

fun BowelLogEntity.symptomTags(): List<String> = symptomTagsRaw.toSymptomTags()

private fun String.toSymptomTags(): List<String> =
    split("|").map { it.trim() }.filter { it.isNotBlank() }

private fun List<String>.encodeSymptomTags(): String =
    distinct().joinToString("|")
