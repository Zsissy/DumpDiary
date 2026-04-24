package com.dumpdiary.backend.repository

import com.dumpdiary.backend.model.BowelLog
import com.dumpdiary.backend.model.FriendProfile
import com.dumpdiary.backend.model.UserAccount
import com.dumpdiary.backend.model.UserProfile
import com.dumpdiary.backend.model.VerificationPurpose
import java.io.File
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class VerificationCodeRecord(
    val email: String,
    val code: String,
    val purpose: VerificationPurpose,
    val expiresAtMillis: Long,
)

@Serializable
data class RefreshTokenRecord(
    val userId: String,
    val token: String,
)

@Serializable
private data class StoreSnapshot(
    val accountsByEmail: Map<String, UserAccount> = emptyMap(),
    val profilesByUserId: Map<String, UserProfile> = emptyMap(),
    val verificationCodes: Map<String, VerificationCodeRecord> = emptyMap(),
    val refreshTokens: Map<String, RefreshTokenRecord> = emptyMap(),
    val logsByUserId: Map<String, Map<String, BowelLog>> = emptyMap(),
    val friendsByUserId: Map<String, Set<String>> = emptyMap(),
)

class InMemoryStore(
    private val storeFile: File,
) {
    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val lock = Any()

    private val accountsByEmail = ConcurrentHashMap<String, UserAccount>()
    private val profilesByUserId = ConcurrentHashMap<String, UserProfile>()
    private val verificationCodes = ConcurrentHashMap<String, VerificationCodeRecord>()
    private val refreshTokens = ConcurrentHashMap<String, RefreshTokenRecord>()
    private val accessTokens = ConcurrentHashMap<String, String>()
    private val logsByUserId = ConcurrentHashMap<String, ConcurrentHashMap<String, BowelLog>>()
    private val friendsByUserId = ConcurrentHashMap<String, MutableSet<String>>()

    init {
        loadFromDisk()
    }

    fun containsAccount(email: String): Boolean = accountsByEmail.containsKey(email)

    fun getAccount(email: String): UserAccount? = accountsByEmail[email]

    fun putAccount(email: String, account: UserAccount) {
        accountsByEmail[email] = account
        persist()
    }

    fun findAccountByUserId(userId: String): UserAccount? =
        accountsByEmail.values.firstOrNull { it.userId == userId }

    fun getProfile(userId: String): UserProfile? = profilesByUserId[userId]

    fun putProfile(userId: String, profile: UserProfile) {
        profilesByUserId[userId] = profile
        persist()
    }

    fun putVerificationCode(key: String, record: VerificationCodeRecord) {
        verificationCodes[key] = record
        persist()
    }

    fun getVerificationCode(key: String): VerificationCodeRecord? = verificationCodes[key]

    fun removeVerificationCode(key: String) {
        verificationCodes.remove(key)
        persist()
    }

    fun putAccessToken(token: String, userId: String) {
        accessTokens[token] = userId
    }

    fun resolveAccessToken(token: String): String? = accessTokens[token]

    fun removeAccessToken(token: String?) {
        if (token == null) return
        accessTokens.remove(token)
    }

    fun putRefreshToken(token: String, record: RefreshTokenRecord) {
        refreshTokens[token] = record
        persist()
    }

    fun getRefreshToken(token: String): RefreshTokenRecord? = refreshTokens[token]

    fun removeRefreshToken(token: String?) {
        if (token == null) return
        refreshTokens.remove(token)
        persist()
    }

    fun userLogs(userId: String): ConcurrentHashMap<String, BowelLog> =
        logsByUserId.computeIfAbsent(userId) { ConcurrentHashMap() }

    fun upsertLog(userId: String, log: BowelLog) {
        userLogs(userId)[log.id] = log
        persist()
    }

    fun getLog(userId: String, logId: String): BowelLog? = userLogs(userId)[logId]

    fun userFriends(userId: String): MutableSet<String> =
        friendsByUserId.computeIfAbsent(userId) { Collections.synchronizedSet(mutableSetOf()) }

    fun addFriendship(userId: String, friendUserId: String) {
        require(userId != friendUserId) { "You cannot add yourself." }
        userFriends(userId).add(friendUserId)
        userFriends(friendUserId).add(userId)
        persist()
    }

    fun resolveUserIdByEmail(email: String): String? = accountsByEmail[email]?.userId

    fun visibleLogs(userId: String): List<BowelLog> =
        (listOf(userId) + userFriends(userId).toList())
            .distinct()
            .flatMap { friendId -> userLogs(friendId).values }

    fun visibleFriendProfiles(userId: String): List<FriendProfile> =
        userFriends(userId)
            .mapNotNull { friendId ->
                val account = accountsByEmail.values.firstOrNull { it.userId == friendId } ?: return@mapNotNull null
                val profile = profilesByUserId[friendId] ?: return@mapNotNull null
                FriendProfile(
                    userId = friendId,
                    email = account.email,
                    displayName = profile.displayName,
                    avatarUrl = profile.avatarUrl,
                )
            }
            .sortedBy { it.displayName.lowercase() }

    private fun loadFromDisk() {
        if (!storeFile.exists()) return
        runCatching {
            val snapshot = json.decodeFromString<StoreSnapshot>(storeFile.readText())
            accountsByEmail.putAll(snapshot.accountsByEmail)
            profilesByUserId.putAll(snapshot.profilesByUserId)
            verificationCodes.putAll(snapshot.verificationCodes)
            refreshTokens.putAll(snapshot.refreshTokens)
            logsByUserId.putAll(
                snapshot.logsByUserId.mapValues { (_, logs) ->
                    ConcurrentHashMap(logs)
                },
            )
            friendsByUserId.putAll(
                snapshot.friendsByUserId.mapValues { (_, friends) ->
                    Collections.synchronizedSet(friends.toMutableSet())
                },
            )
        }
    }

    private fun persist() {
        synchronized(lock) {
            storeFile.parentFile?.mkdirs()
            val snapshot = StoreSnapshot(
                accountsByEmail = accountsByEmail.toMap(),
                profilesByUserId = profilesByUserId.toMap(),
                verificationCodes = verificationCodes.toMap(),
                refreshTokens = refreshTokens.toMap(),
                logsByUserId = logsByUserId.mapValues { (_, logs) -> logs.toMap() },
                friendsByUserId = friendsByUserId.mapValues { (_, friends) -> friends.toSet() },
            )
            storeFile.writeText(json.encodeToString(StoreSnapshot.serializer(), snapshot))
        }
    }
}
