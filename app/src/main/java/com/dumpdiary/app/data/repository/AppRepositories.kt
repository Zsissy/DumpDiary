package com.dumpdiary.app.data.repository

import android.content.ContentResolver
import android.net.Uri
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dumpdiary.app.data.local.LogDao
import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferences
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.model.AuthRequestDto
import com.dumpdiary.app.data.model.AppVersionDto
import com.dumpdiary.app.data.model.BowelLogDto
import com.dumpdiary.app.data.model.BowelLogEntity
import com.dumpdiary.app.data.model.AddFriendRequestDto
import com.dumpdiary.app.data.model.FriendProfileDto
import com.dumpdiary.app.data.model.MessageDto
import com.dumpdiary.app.data.model.RefreshRequestDto
import com.dumpdiary.app.data.model.RegisterRequestDto
import com.dumpdiary.app.data.model.ResetPasswordRequestDto
import com.dumpdiary.app.data.model.SendEmailCodeRequestDto
import com.dumpdiary.app.data.model.UpdateProfileRequestDto
import com.dumpdiary.app.data.model.UserProfileEntity
import com.dumpdiary.app.data.model.VerificationPurposeDto
import com.dumpdiary.app.data.model.symptomTags
import com.dumpdiary.app.data.model.toDto
import com.dumpdiary.app.data.model.toEntity
import com.dumpdiary.app.data.remote.DumpDiaryApi
import com.dumpdiary.app.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

data class AppSession(
    val isLoggedIn: Boolean,
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
    val email: String,
    val languageTag: String,
)

data class UserProfileUi(
    val userId: String = "",
    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String? = null,
)

data class AppUpdateUi(
    val versionCode: Int,
    val versionName: String,
    val downloadUrl: String,
    val notes: String,
)

data class FriendUi(
    val userId: String,
    val email: String,
    val displayName: String,
    val avatarUrl: String? = null,
)

data class BowelLogInput(
    val id: String? = null,
    val occurredAt: String,
    val durationSeconds: Int,
    val feeling: String,
    val stoolForm: Int,
    val symptomTags: List<String>,
    val detailNote: String,
)

@Singleton
class AppUpdateRepository @Inject constructor(
    private val api: DumpDiaryApi,
) {
    suspend fun checkForUpdate(): AppUpdateUi? {
        val latest = api.getLatestAppVersion()
        if (latest.versionCode <= com.dumpdiary.app.BuildConfig.APP_VERSION_CODE) return null
        return AppUpdateUi(
            versionCode = latest.versionCode,
            versionName = latest.versionName,
            downloadUrl = latest.downloadPath.toResolvedDownloadUrl(),
            notes = latest.notes,
        )
    }
}

@Singleton
class AuthRepository @Inject constructor(
    private val api: DumpDiaryApi,
    private val preferencesRepository: UserPreferencesRepository,
    private val profileDao: ProfileDao,
    private val logDao: LogDao,
) {
    val sessionFlow: Flow<AppSession> = preferencesRepository.preferences.map { prefs ->
        AppSession(
            isLoggedIn = prefs.accessToken.isNotBlank(),
            accessToken = prefs.accessToken,
            refreshToken = prefs.refreshToken,
            userId = prefs.userId,
            email = prefs.email,
            languageTag = prefs.languageTag,
        )
    }

    suspend fun sendRegisterCode(email: String): MessageDto =
        api.sendEmailCode(SendEmailCodeRequestDto(email, VerificationPurposeDto.REGISTER))

    suspend fun sendResetCode(email: String): MessageDto =
        api.sendEmailCode(SendEmailCodeRequestDto(email, VerificationPurposeDto.RESET_PASSWORD))

    suspend fun register(email: String, password: String, code: String) {
        val response = api.register(RegisterRequestDto(email = email, password = password, code = code))
        persistSession(response.accessToken, response.refreshToken, response.userId, response.email)
        profileDao.upsert(response.profile.toEntity(response.email))
    }

    suspend fun login(email: String, password: String) {
        val response = api.login(AuthRequestDto(email = email, password = password))
        persistSession(response.accessToken, response.refreshToken, response.userId, response.email)
        profileDao.upsert(response.profile.toEntity(response.email))
    }

    suspend fun resetPassword(email: String, code: String, newPassword: String): MessageDto =
        api.resetPassword(ResetPasswordRequestDto(email = email, code = code, newPassword = newPassword))

    suspend fun refreshSession(): Boolean {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.refreshToken.isBlank()) return false
        return runCatching {
            val response = api.refresh(RefreshRequestDto(prefs.refreshToken))
            persistSession(response.accessToken, response.refreshToken, response.userId, response.email)
            profileDao.upsert(response.profile.toEntity(response.email))
            true
        }.getOrDefault(false)
    }

    suspend fun logout() {
        runCatching { api.logout() }
        preferencesRepository.clearSession()
        profileDao.clear()
        logDao.clear()
    }

    private suspend fun persistSession(accessToken: String, refreshToken: String, userId: String, email: String) {
        preferencesRepository.updateSession(
            accessToken = accessToken,
            refreshToken = refreshToken,
            userId = userId,
            email = email,
        )
    }
}

@Singleton
class ProfileRepository @Inject constructor(
    private val api: DumpDiaryApi,
    private val profileDao: ProfileDao,
    private val preferencesRepository: UserPreferencesRepository,
    private val contentResolver: ContentResolver,
) {
    val profileFlow: Flow<UserProfileUi?> = profileDao.observeProfile().map { entity ->
        entity?.let {
            UserProfileUi(
                userId = it.userId,
                email = it.email,
                displayName = it.displayName,
                avatarUrl = it.avatarUrl,
            )
        }
    }

    suspend fun refreshProfile() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.accessToken.isBlank()) return
        val localProfile = profileDao.getProfile()?.takeIf { it.userId == prefs.userId }
        val resolvedProfile = runCatching {
            val remote = api.getProfile()
            remote.toEntity(email = prefs.email)
        }.getOrElse {
            localProfile ?: prefs.toFallbackProfile()
        }
        profileDao.upsert(resolvedProfile)
    }

    suspend fun updateDisplayName(displayName: String) {
        val prefs = preferencesRepository.preferences.first()
        val updated = api.updateProfile(UpdateProfileRequestDto(displayName))
        profileDao.upsert(updated.toEntity(email = prefs.email))
    }

    suspend fun uploadAvatar(uri: Uri) {
        val prefs = preferencesRepository.preferences.first()
        val bytes = requireNotNull(contentResolver.openInputStream(uri)) { "Unable to open file." }.use { it.readBytes() }
        val mediaType = contentResolver.getType(uri)?.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull()
        val requestBody = bytes.toRequestBody(mediaType)
        val avatarPart = MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody)
        val response = api.uploadAvatar(avatarPart)
        val current = profileDao.getProfile() ?: return
        profileDao.upsert(current.copy(avatarUrl = "${com.dumpdiary.app.BuildConfig.API_BASE_URL.removeSuffix("/")}${response.avatarUrl}"))
    }
}

@Singleton
class FriendRepository @Inject constructor(
    private val api: DumpDiaryApi,
    private val preferencesRepository: UserPreferencesRepository,
) {
    private val _friendsFlow = kotlinx.coroutines.flow.MutableStateFlow<List<FriendUi>>(emptyList())
    val friendsFlow: Flow<List<FriendUi>> = _friendsFlow

    suspend fun refreshFriends() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.accessToken.isBlank()) {
            _friendsFlow.value = emptyList()
            return
        }
        _friendsFlow.value = api.getFriends().map { it.toUi() }
    }

    suspend fun addFriend(email: String): MessageDto {
        val response = api.addFriend(AddFriendRequestDto(email.trim()))
        refreshFriends()
        return response
    }
}

@Singleton
class LogRepository @Inject constructor(
    private val api: DumpDiaryApi,
    private val logDao: LogDao,
    private val profileDao: ProfileDao,
    private val preferencesRepository: UserPreferencesRepository,
    private val contentResolver: ContentResolver,
    private val workManager: WorkManager,
) {
    val logsFlow: Flow<List<BowelLogEntity>> = logDao.observeActiveLogs()

    suspend fun createOrUpdate(input: BowelLogInput) {
        val prefs = preferencesRepository.preferences.first()
        val profile = resolveProfile(prefs)
        val now = Clock.System.now().toString()
        val occurredAt = input.occurredAt
        val dateKey = occurredAt.take(10)
        val existing = input.id?.let { logDao.getById(it) }
        val entity = BowelLogEntity(
            id = input.id ?: UUID.randomUUID().toString(),
            userId = prefs.userId,
            occurredAt = occurredAt,
            dateKey = dateKey,
            durationSeconds = input.durationSeconds,
            feeling = input.feeling,
            stoolForm = input.stoolForm,
            symptomTagsRaw = input.symptomTags.distinct().joinToString("|"),
            detailNote = input.detailNote,
            snapshotDisplayName = profile.displayName,
            snapshotAvatarUrl = profile.avatarUrl,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
            isDeleted = false,
            pendingSyncAction = "UPSERT",
        )
        logDao.upsert(entity)
        enqueueSync()
    }

    suspend fun markDeleted(id: String) {
        val current = logDao.getById(id) ?: return
        logDao.upsert(current.copy(isDeleted = true, pendingSyncAction = "DELETE", updatedAt = Clock.System.now().toString()))
        enqueueSync()
    }

    suspend fun syncPendingChanges() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.accessToken.isBlank()) return
        logDao.getPendingSyncLogs().forEach { entity ->
            when (entity.pendingSyncAction) {
                "UPSERT" -> {
                    val response = runCatching { api.updateLog(entity.id, entity.toDto()) }
                        .getOrElse { api.createLog(entity.toDto()) }
                    logDao.upsert(response.toEntity(syncAction = null))
                }

                "DELETE" -> {
                    api.deleteLog(entity.id)
                    logDao.upsert(entity.copy(pendingSyncAction = null))
                }
            }
        }
    }

    suspend fun refreshFromRemote() {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.accessToken.isBlank()) return
        val remoteLogs = api.getLogs()
        logDao.upsertAll(remoteLogs.map { it.toEntity(syncAction = null) })
    }

    suspend fun exportOwnLogsToCsv(uri: Uri): Int {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.userId.isBlank()) error("Please log in first.")
        val logs = logDao.getAllLogs()
            .asSequence()
            .filter { it.userId == prefs.userId && !it.isDeleted }
            .sortedBy { it.occurredAt }
            .toList()
        val output = requireNotNull(contentResolver.openOutputStream(uri)) { "Unable to create CSV file." }
        output.bufferedWriter(Charsets.UTF_8).use { writer ->
            writer.write("\uFEFF")
            writer.write(csvHeaders.joinToString(","))
            writer.write("\n")
            logs.forEach { entity ->
                writer.write(
                    listOf(
                        entity.id,
                        entity.occurredAt,
                        entity.dateKey,
                        entity.durationSeconds.toString(),
                        entity.feeling,
                        entity.stoolForm.toString(),
                        entity.symptomTags().joinToString("|"),
                        entity.detailNote,
                        entity.snapshotDisplayName,
                        entity.snapshotAvatarUrl.orEmpty(),
                        entity.createdAt,
                        entity.updatedAt,
                    ).joinToString(",") { it.toCsvField() },
                )
                writer.write("\n")
            }
        }
        return logs.size
    }

    suspend fun importLogsFromCsv(uri: Uri): Int {
        val prefs = preferencesRepository.preferences.first()
        if (prefs.userId.isBlank()) error("Please log in first.")
        val profile = resolveProfile(prefs)
        val input = requireNotNull(contentResolver.openInputStream(uri)) { "Unable to open CSV file." }
        val rows = input.bufferedReader(Charsets.UTF_8).use { it.readText().parseCsvRows() }
        if (rows.isEmpty()) error("CSV is empty.")
        val headerIndex = rows.first()
            .mapIndexed { index, header -> header.removePrefix("\uFEFF").trim().lowercase(Locale.US) to index }
            .toMap()
        if (!headerIndex.containsKey("occurredat")) error("CSV format is missing occurredAt.")

        val now = Clock.System.now().toString()
        val imported = rows.drop(1).mapNotNull { row ->
            if (row.all { it.isBlank() }) return@mapNotNull null

            val occurredAt = row.csvValue("occurredAt", headerIndex)
            if (occurredAt.isBlank()) return@mapNotNull null

            val isDeleted = row.csvValue("isDeleted", headerIndex).toBooleanStrictOrNull()
                ?: (row.csvValue("isDeleted", headerIndex) == "1")
            if (isDeleted) return@mapNotNull null

            val rawSymptomTags = row.csvValue("symptomTags", headerIndex)
                .ifBlank { row.csvValue("symptomTagsRaw", headerIndex) }

            BowelLogEntity(
                id = row.csvValue("id", headerIndex).ifBlank { UUID.randomUUID().toString() },
                userId = prefs.userId,
                occurredAt = occurredAt,
                dateKey = row.csvValue("dateKey", headerIndex).ifBlank { occurredAt.take(10) },
                durationSeconds = row.csvValue("durationSeconds", headerIndex).toIntOrNull() ?: 0,
                feeling = row.csvValue("feeling", headerIndex).ifBlank { "NORMAL" },
                stoolForm = (row.csvValue("stoolForm", headerIndex).toIntOrNull() ?: 4).coerceIn(1, 7),
                symptomTagsRaw = rawSymptomTags.normalizeImportedSymptomTags(),
                detailNote = row.csvValue("detailNote", headerIndex),
                snapshotDisplayName = row.csvValue("snapshotDisplayName", headerIndex).ifBlank { profile.displayName },
                snapshotAvatarUrl = row.csvValue("snapshotAvatarUrl", headerIndex).ifBlank { profile.avatarUrl },
                createdAt = row.csvValue("createdAt", headerIndex).ifBlank { now },
                updatedAt = row.csvValue("updatedAt", headerIndex).ifBlank { now },
                isDeleted = false,
                pendingSyncAction = "UPSERT",
            )
        }

        if (imported.isEmpty()) error("No valid log rows found in CSV.")
        logDao.upsertAll(imported)
        enqueueSync()
        return imported.size
    }

    private fun enqueueSync() {
        workManager.enqueueUniqueWork(
            "dump-diary-sync",
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<SyncWorker>().build(),
        )
    }

    private suspend fun resolveProfile(prefs: UserPreferences): UserProfileEntity {
        profileDao.getProfile()?.takeIf { it.userId == prefs.userId }?.let { return it }
        val resolvedProfile = runCatching {
            api.getProfile().toEntity(email = prefs.email)
        }.getOrElse {
            prefs.toFallbackProfile()
        }
        profileDao.upsert(resolvedProfile)
        return resolvedProfile
    }
}

private fun UserPreferences.toFallbackProfile(): UserProfileEntity {
    val fallbackName = email.substringBefore("@").ifBlank { "Daily Rhythm" }
    return UserProfileEntity(
        userId = userId,
        email = email,
        displayName = fallbackName,
        avatarUrl = null,
        updatedAt = Clock.System.now().toString(),
    )
}

private fun FriendProfileDto.toUi(): FriendUi =
    FriendUi(
        userId = userId,
        email = email,
        displayName = displayName,
        avatarUrl = avatarUrl?.let(::resolveAvatarUrl),
    )

private fun resolveAvatarUrl(rawUrl: String): String =
    if (rawUrl.startsWith("http://") || rawUrl.startsWith("https://")) {
        rawUrl
    } else {
        "${com.dumpdiary.app.BuildConfig.API_BASE_URL.removeSuffix("/")}${rawUrl}"
    }

private fun String.toResolvedDownloadUrl(): String =
    if (startsWith("http://") || startsWith("https://")) {
        this
    } else {
        "${com.dumpdiary.app.BuildConfig.API_BASE_URL.removeSuffix("/")}/${removePrefix("/")}"
    }

private val csvHeaders = listOf(
    "id",
    "occurredAt",
    "dateKey",
    "durationSeconds",
    "feeling",
    "stoolForm",
    "symptomTags",
    "detailNote",
    "snapshotDisplayName",
    "snapshotAvatarUrl",
    "createdAt",
    "updatedAt",
)

private fun String.toCsvField(): String {
    val escaped = replace("\"", "\"\"")
    return if (escaped.any { it == ',' || it == '"' || it == '\n' || it == '\r' }) {
        "\"$escaped\""
    } else {
        escaped
    }
}

private fun List<String>.csvValue(name: String, headerIndex: Map<String, Int>): String =
    headerIndex[name.lowercase(Locale.US)]
        ?.let { index -> getOrNull(index) }
        ?.trim()
        .orEmpty()

private fun String.normalizeImportedSymptomTags(): String =
    split("|", ";")
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinct()
        .joinToString("|")

private fun String.parseCsvRows(): List<List<String>> {
    if (isBlank()) return emptyList()
    val rows = mutableListOf<List<String>>()
    val currentRow = mutableListOf<String>()
    val currentField = StringBuilder()
    var index = 0
    var inQuotes = false

    while (index < length) {
        when (val char = this[index]) {
            '"' -> {
                if (inQuotes && getOrNull(index + 1) == '"') {
                    currentField.append('"')
                    index++
                } else {
                    inQuotes = !inQuotes
                }
            }

            ',' -> {
                if (inQuotes) {
                    currentField.append(char)
                } else {
                    currentRow += currentField.toString()
                    currentField.clear()
                }
            }

            '\n' -> {
                if (inQuotes) {
                    currentField.append(char)
                } else {
                    currentRow += currentField.toString()
                    currentField.clear()
                    rows += currentRow.toList()
                    currentRow.clear()
                }
            }

            '\r' -> Unit

            else -> currentField.append(char)
        }
        index++
    }

    if (currentField.isNotEmpty() || currentRow.isNotEmpty()) {
        currentRow += currentField.toString()
        rows += currentRow.toList()
    }

    return rows
}
