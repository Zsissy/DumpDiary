package com.dumpdiary.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- Auth models (matching dump-web app_users table) ---

@Serializable
data class SupabaseUser(
    val id: String = "",
    val username: String = "",
    val nickname: String = "",
    val password: String = "",
    val role: String = "member",
    val status: String = "pending",
    val avatar: String = "",
    @SerialName("match_code")
    val matchCode: String = "",
    @SerialName("created_at")
    val createdAt: String = "",
    @SerialName("reviewed_at")
    val reviewedAt: String = "",
    @SerialName("reviewed_by")
    val reviewedBy: String = "",
)

@Serializable
data class SupabaseLoginResult(
    val user: SupabaseUser,
)

// --- Room models (matching dump-web app_sync_rooms table) ---

@Serializable
data class SupabaseRoom(
    @SerialName("room_code")
    val roomCode: String = "",
    @SerialName("bowel_logs")
    val bowelLogs: List<SupabaseBowelLog> = emptyList(),
    @SerialName("updated_at")
    val updatedAt: String = "",
)

@Serializable
data class SupabaseBowelLog(
    val id: String = "",
    @SerialName("userid")  // Supabase column names from dump-web
    val userId: String = "",
    val date: String = "",
    val time: String = "",
    @SerialName("durationseconds")
    val durationSeconds: Int = 0,
    @SerialName("bristoltype")
    val bristolType: Int = 4,
    val symptoms: List<String> = emptyList(),
    val notes: String = "",
    @SerialName("createdat")
    val createdAt: String = "",
    @SerialName("updatedat")
    val updatedAt: String = "",
)

// Mapping between dump-web log format and Android entity format

fun SupabaseBowelLog.toBowelLogEntity(currentUserId: String): BowelLogEntity {
    val occurredAt = buildString {
        append(date)
        if (time.isNotBlank()) {
            append("T")
            append(if (time.length == 5) "$time:00" else time)
        } else {
            append("T00:00")
        }
    }
    return BowelLogEntity(
        id = id,
        userId = userId.ifBlank { currentUserId },
        occurredAt = occurredAt,
        dateKey = date,
        durationSeconds = durationSeconds,
        feeling = "NORMAL",
        stoolForm = bristolType.coerceIn(1, 7),
        symptomTagsRaw = symptoms.distinct().joinToString("|"),
        detailNote = notes,
        snapshotDisplayName = "",
        snapshotAvatarUrl = null,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = false,
        pendingSyncAction = null,
    )
}

fun BowelLogEntity.toSupabaseBowelLog(): SupabaseBowelLog {
    val date = dateKey
    val time = try {
        occurredAt.substring(11, 16)
    } catch (_: Exception) {
        "00:00"
    }
    return SupabaseBowelLog(
        id = id,
        userId = userId,
        date = date,
        time = time,
        durationSeconds = durationSeconds,
        bristolType = stoolForm,
        symptoms = symptomTags().ifEmpty { listOf("Pain-free") },
        notes = detailNote,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )
}
