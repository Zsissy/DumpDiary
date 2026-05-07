package com.dumpdiary.app.data.repository

import com.dumpdiary.app.data.local.ProfileDao
import com.dumpdiary.app.data.local.UserPreferencesRepository
import com.dumpdiary.app.data.model.SupabaseUser
import com.dumpdiary.app.data.model.UserProfileEntity
import com.dumpdiary.app.data.remote.SupabaseApi
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import javax.inject.Inject
import javax.inject.Singleton

data class SupabaseSession(
    val userId: String,
    val username: String,
    val nickname: String,
    val role: String,
    val avatar: String,
    val matchCode: String,
    val serverBaseUrl: String,
    val anonKey: String,
)

@Singleton
class SupabaseAuthRepository @Inject constructor(
    private val api: SupabaseApi,
    private val preferencesRepository: UserPreferencesRepository,
    private val profileDao: ProfileDao,
) {
    companion object {
        const val ADMIN_USERNAME = "小茭"
        const val ADMIN_PASSWORD = "zxq121800"
    }

    suspend fun login(username: String, password: String): SupabaseSession {
        val normalizedUsername = username.trim()

        if (normalizedUsername == ADMIN_USERNAME && password == ADMIN_PASSWORD) {
            val session = findOrCreateAdminSession()
            persistSession(session)
            return session
        }

        val users = api.getUserByUsername(username = "eq.$normalizedUsername")
        val user = users.firstOrNull()
            ?: error("用户名或密码错误")

        if (user.password != password) {
            error("用户名或密码错误")
        }

        when (user.status) {
            "pending" -> error("账号待管理员审核，请稍后再试。")
            "rejected" -> error("账号审核未通过，请联系管理员。")
            "approved" -> {} // ok
            else -> error("账号状态异常，请联系管理员。")
        }

        val prefs = preferencesRepository.preferences.first()
        val session = SupabaseSession(
            userId = user.id,
            username = user.username,
            nickname = user.nickname.ifBlank { user.username },
            role = user.role,
            avatar = user.avatar,
            matchCode = user.matchCode,
            serverBaseUrl = prefs.serverBaseUrl,
            anonKey = prefs.supabaseAnonKey,
        )
        persistSession(session)
        return session
    }

    private suspend fun persistSession(session: SupabaseSession) {
        preferencesRepository.saveSupabaseSession(
            userId = session.userId,
            username = session.username,
            matchCode = session.matchCode,
        )
        profileDao.upsert(
            UserProfileEntity(
                userId = session.userId,
                email = session.username,
                displayName = session.nickname,
                avatarUrl = session.avatar,
                updatedAt = Clock.System.now().toString(),
            )
        )
    }

    suspend fun register(
        username: String,
        password: String,
        nickname: String = "",
        matchCode: String = "",
        avatar: String = "",
    ) {
        val normalizedUsername = username.trim()
        val normalizedNickname = nickname.trim().ifBlank { normalizedUsername }

        require(normalizedUsername.isNotBlank()) { "请填写用户名。" }
        require(password.isNotBlank()) { "请填写密码。" }

        if (normalizedUsername == ADMIN_USERNAME) {
            error("该用户名已被占用。")
        }

        val existing = api.getUserByUsername(username = "eq.$normalizedUsername")
        if (existing.isNotEmpty()) {
            error("该用户名已被注册。")
        }

        val now = Clock.System.now().toString()
        val user = SupabaseUser(
            username = normalizedUsername,
            nickname = normalizedNickname,
            password = password,
            role = "member",
            status = "pending",
            avatar = avatar,
            matchCode = matchCode.trim(),
            createdAt = now,
        )
        api.insertUser(user)
    }

    suspend fun getAllUsers(): List<SupabaseUser> = api.getAllUsers()

    suspend fun reviewUser(id: String, action: String, reviewedBy: String) {
        val targetStatus = when (action) {
            "approved" -> "approved"
            else -> "rejected"
        }
        val now = Clock.System.now().toString()
        val updates = SupabaseUser(
            status = targetStatus,
            reviewedAt = now,
            reviewedBy = reviewedBy,
        )
        api.updateUserById(id = "eq.$id", user = updates)
    }

    suspend fun updateUserField(id: String, field: String, value: String) {
        val updates = when (field) {
            "nickname" -> SupabaseUser(nickname = value)
            "avatar" -> SupabaseUser(avatar = value)
            "password" -> SupabaseUser(password = value)
            "match_code" -> SupabaseUser(matchCode = value)
            else -> error("Unknown field: $field")
        }
        api.updateUserById(id = "eq.$id", user = updates)
    }

    suspend fun updateNickname(userId: String, nickname: String) {
        val realUserId = resolveAdminUserId(userId)
        api.updateUserById(id = "eq.$realUserId", user = SupabaseUser(nickname = nickname))
    }

    suspend fun updateMatchCode(userId: String, matchCode: String) {
        val realUserId = resolveAdminUserId(userId)
        api.updateUserById(id = "eq.$realUserId", user = SupabaseUser(matchCode = matchCode))
    }

    suspend fun updatePassword(userId: String, newPassword: String) {
        val realUserId = resolveAdminUserId(userId)
        api.updateUserById(id = "eq.$realUserId", user = SupabaseUser(password = newPassword))
    }

    suspend fun updateAvatar(userId: String, avatar: String) {
        val realUserId = resolveAdminUserId(userId)
        api.updateUserById(id = "eq.$realUserId", user = SupabaseUser(avatar = avatar))
    }

    private suspend fun resolveAdminUserId(userId: String): String {
        if (userId != "admin") return userId
        // Admin needs a real UUID from the database
        val users = api.getUserByUsername(username = "eq.$ADMIN_USERNAME")
        val adminUser = users.firstOrNull()
        if (adminUser != null) return adminUser.id
        // Auto-create admin user if not exists
        val now = Clock.System.now().toString()
        api.insertUser(
            SupabaseUser(
                username = ADMIN_USERNAME,
                nickname = ADMIN_USERNAME,
                password = ADMIN_PASSWORD,
                role = "admin",
                status = "approved",
                matchCode = "",
                createdAt = now,
                reviewedAt = now,
                reviewedBy = "system",
            )
        )
        val newUsers = api.getUserByUsername(username = "eq.$ADMIN_USERNAME")
        return newUsers.firstOrNull()?.id ?: userId
    }

    private suspend fun findOrCreateAdminSession(): SupabaseSession {
        val prefs = preferencesRepository.preferences.first()
        // Try to find existing admin user in database
        val existingUsers = api.getUserByUsername(username = "eq.$ADMIN_USERNAME")
        val existingAdmin = existingUsers.firstOrNull()
        if (existingAdmin != null) {
            return SupabaseSession(
                userId = existingAdmin.id,
                username = existingAdmin.username,
                nickname = existingAdmin.nickname.ifBlank { existingAdmin.username },
                role = existingAdmin.role,
                avatar = existingAdmin.avatar,
                matchCode = existingAdmin.matchCode,
                serverBaseUrl = prefs.serverBaseUrl,
                anonKey = prefs.supabaseAnonKey,
            )
        }
        // Auto-create admin user in database
        val now = Clock.System.now().toString()
        val adminUser = SupabaseUser(
            username = ADMIN_USERNAME,
            nickname = ADMIN_USERNAME,
            password = ADMIN_PASSWORD,
            role = "admin",
            status = "approved",
            matchCode = "",
            createdAt = now,
            reviewedAt = now,
            reviewedBy = "system",
        )
        api.insertUser(adminUser)
        // Fetch the newly created user to get the UUID
        val newUsers = api.getUserByUsername(username = "eq.$ADMIN_USERNAME")
        val newAdmin = newUsers.firstOrNull()
            ?: error("Failed to create admin user.")
        return SupabaseSession(
            userId = newAdmin.id,
            username = newAdmin.username,
            nickname = newAdmin.nickname.ifBlank { newAdmin.username },
            role = newAdmin.role,
            avatar = newAdmin.avatar,
            matchCode = newAdmin.matchCode,
            serverBaseUrl = prefs.serverBaseUrl,
            anonKey = prefs.supabaseAnonKey,
        )
    }
}
