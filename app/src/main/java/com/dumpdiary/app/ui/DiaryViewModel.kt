package com.dumpdiary.app.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumpdiary.app.data.model.BowelLogEntity
import com.dumpdiary.app.data.model.symptomTags
import com.dumpdiary.app.data.repository.BowelLogInput
import com.dumpdiary.app.data.repository.FriendRepository
import com.dumpdiary.app.data.repository.FriendUi
import com.dumpdiary.app.data.repository.LogRepository
import com.dumpdiary.app.data.repository.ProfileRepository
import com.dumpdiary.app.data.repository.UserProfileUi
import java.time.Duration
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import kotlin.math.ceil
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE
private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

data class DiaryUiState(
    val logs: List<BowelLogEntity> = emptyList(),
    val profile: UserProfileUi? = null,
    val friends: List<FriendUi> = emptyList(),
    val selectedCalendarUserId: String? = null,
    val selectedMonth: YearMonth = YearMonth.now(),
    val formId: String? = null,
    val occurredAt: String = LocalDateTime.now().format(dateTimeFormatter),
    val durationSeconds: String = "600",
    val timerStartedAt: String? = null,
    val feeling: String = "NORMAL",
    val stoolForm: Float = 4f,
    val symptomTags: Set<String> = setOf("Pain-free"),
    val detailNote: String = "",
    val message: String? = null,
)

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val logRepository: LogRepository,
    private val profileRepository: ProfileRepository,
    private val friendRepository: FriendRepository,
) : ViewModel() {
    private val formState = MutableStateFlow(DiaryUiState())

    val uiState: StateFlow<DiaryUiState> = combine(
        logRepository.logsFlow,
        profileRepository.profileFlow,
        friendRepository.friendsFlow,
        formState,
    ) { logs, profile, friends, form ->
        form.copy(logs = logs, profile = profile, friends = friends)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        DiaryUiState(),
    )

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            runCatching { profileRepository.refreshProfile() }
            runCatching { friendRepository.refreshFriends() }
            runCatching { logRepository.syncPendingChanges() }
            runCatching { logRepository.refreshFromRemote() }
        }
    }

    fun moveMonth(offset: Long) {
        formState.update { it.copy(selectedMonth = it.selectedMonth.plusMonths(offset)) }
    }

    fun selectCalendarUser(userId: String?) {
        formState.update { it.copy(selectedCalendarUserId = userId) }
    }

    fun loadForEdit(id: String?) {
        val current = uiState.value.logs.firstOrNull { it.id == id } ?: return
        formState.update {
            it.copy(
                formId = current.id,
                occurredAt = current.occurredAt.take(16),
                durationSeconds = current.durationSeconds.toString(),
                timerStartedAt = null,
                feeling = current.feeling,
                stoolForm = current.stoolForm.toFloat(),
                symptomTags = current.symptomTags().toSet(),
                detailNote = current.detailNote,
            )
        }
    }

    fun resetForm() {
        formState.update {
            it.copy(
                formId = null,
                occurredAt = LocalDateTime.now().format(dateTimeFormatter),
                durationSeconds = "600",
                timerStartedAt = null,
                feeling = "NORMAL",
                stoolForm = 4f,
                symptomTags = setOf("Pain-free"),
                detailNote = "",
            )
        }
    }

    fun updateOccurredAt(value: String) {
        formState.update { it.copy(occurredAt = value) }
    }

    fun startDurationTimer() {
        val now = LocalDateTime.now().format(dateTimeFormatter)
        formState.update {
            it.copy(
                timerStartedAt = now,
                occurredAt = now,
                durationSeconds = "0",
            )
        }
    }

    fun stopDurationTimer() {
        formState.update { current ->
            val startedAt = current.timerStartedAt ?: return@update current
            val duration = runCatching {
                val seconds = ceil(
                    Duration.between(
                        LocalDateTime.parse(startedAt, dateTimeFormatter),
                        LocalDateTime.now(),
                    ).seconds.coerceAtLeast(0).toDouble(),
                ).toInt()
                seconds.coerceAtLeast(1)
            }.getOrDefault(current.durationSeconds.toIntOrNull() ?: 0)
            current.copy(
                timerStartedAt = null,
                durationSeconds = duration.toString(),
            )
        }
    }

    fun updateFeeling(value: String) {
        formState.update { it.copy(feeling = value) }
    }

    fun updateStoolForm(value: Float) {
        formState.update { it.copy(stoolForm = value) }
    }

    fun toggleSymptomTag(value: String) {
        formState.update { current ->
            val updated = current.symptomTags.toMutableSet().apply {
                if (contains(value)) remove(value) else add(value)
            }
            current.copy(symptomTags = updated)
        }
    }

    fun updateDetailNote(value: String) {
        formState.update { it.copy(detailNote = value) }
    }

    fun saveLog() {
        viewModelScope.launch {
            val current = uiState.value
            val message = runCatching {
                val durationSeconds = resolveDurationSeconds(current)
                logRepository.createOrUpdate(
                    BowelLogInput(
                        id = current.formId,
                        occurredAt = current.occurredAt,
                        durationSeconds = durationSeconds,
                        feeling = deriveFeeling(current.symptomTags, current.feeling),
                        stoolForm = current.stoolForm.toInt(),
                        symptomTags = current.symptomTags.toList(),
                        detailNote = current.detailNote,
                    ),
                )
                resetForm()
                "Saved."
            }.getOrElse { it.message ?: "Unable to save." }
            formState.update { it.copy(message = message) }
        }
    }

    fun deleteLog(id: String) {
        viewModelScope.launch {
            val message = runCatching {
                logRepository.markDeleted(id)
                "Deleted."
            }.getOrElse { it.message ?: "Unable to delete." }
            formState.update { it.copy(message = message) }
        }
    }

    fun addFriend(email: String) {
        viewModelScope.launch {
            val message = runCatching {
                require(email.isNotBlank()) { "Friend email is required." }
                friendRepository.addFriend(email).message
            }.getOrElse { it.message ?: "Unable to add friend." }
            runCatching { logRepository.refreshFromRemote() }
            formState.update { it.copy(message = message) }
        }
    }

    fun consumeMessage() {
        formState.update { it.copy(message = null) }
    }

    private fun resolveDurationSeconds(state: DiaryUiState): Int {
        val base = state.durationSeconds.toIntOrNull() ?: 0
        val startedAt = state.timerStartedAt ?: return base
        return runCatching {
            Duration.between(
                LocalDateTime.parse(startedAt, dateTimeFormatter),
                LocalDateTime.now(),
            ).seconds.coerceAtLeast(1).toInt()
        }.getOrDefault(base)
    }
}

private fun deriveFeeling(symptomTags: Set<String>, fallback: String): String {
    return when {
        "Straining" in symptomTags -> "STRAINED"
        "Urgency" in symptomTags -> "URGENT"
        "Pain-free" in symptomTags -> "SMOOTH"
        symptomTags.isNotEmpty() -> "INCOMPLETE"
        else -> fallback
    }
}
