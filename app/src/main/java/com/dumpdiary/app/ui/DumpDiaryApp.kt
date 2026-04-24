package com.dumpdiary.app.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.dumpdiary.app.R
import com.dumpdiary.app.data.model.BowelLogEntity
import com.dumpdiary.app.data.model.symptomTags
import com.dumpdiary.app.data.repository.FriendUi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlinx.coroutines.delay

private val editorDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
private val displayDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
private val displayTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
private val timeValueFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.US)

private data class StoolOption(
    val value: Int,
    val labelRes: Int,
    val circleColor: Color,
    val contentColor: Color,
    val icon: ImageVector,
)

private data class TabItem(
    val labelRes: Int,
    val icon: @Composable () -> Unit,
)

private data class CalendarCellData(
    val date: LocalDate,
    val inCurrentMonth: Boolean,
    val logs: List<BowelLogEntity>,
    val isHighlighted: Boolean,
)

private val calendarLoggedDayBackground = Color(0xFFF4E5CC)

private data class TrendPoint(
    val label: String,
    val value: Int,
)

private val stoolOptions = listOf(
    StoolOption(1, R.string.hard_lumps, Color(0xFFFEE7D3), Color(0xFF9A5B15), Icons.Default.Texture),
    StoolOption(2, R.string.lumpy_sausage, Color(0xFFF4DEC8), Color(0xFF8B5E34), Icons.Default.Grain),
    StoolOption(3, R.string.cracked, Color(0xFFF6E1BF), Color(0xFF8D5E13), Icons.Default.Grain),
    StoolOption(4, R.string.smooth, Color(0xFFF1D9D0), Color(0xFF8A685E), Icons.Default.Opacity),
    StoolOption(5, R.string.soft_blobs, Color(0xFFFAEDB9), Color(0xFF8A7006), Icons.Default.Cloud),
    StoolOption(6, R.string.mushy, Color(0xFFDDEBFF), Color(0xFF1A5FB4), Icons.Default.Opacity),
    StoolOption(7, R.string.liquid, Color(0xFFD8F4F6), Color(0xFF136B73), Icons.Default.Waves),
)

private val symptomItems = listOf(
    "Straining" to R.string.straining,
    "Pain-free" to R.string.pain_free,
    "Bloating" to R.string.bloating,
    "Blood" to R.string.blood,
    "Urgency" to R.string.urgency,
)

private val homeTabs = listOf(
    TabItem(R.string.log) { Icon(Icons.Default.Edit, contentDescription = null) },
    TabItem(R.string.calendar) { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
    TabItem(R.string.stats) { Icon(Icons.Default.ShowChart, contentDescription = null) },
)

@Composable
fun DumpDiaryApp(
    state: MainUiState,
    mainViewModel: MainViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    diaryViewModel: DiaryViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val diaryState by diaryViewModel.uiState.collectAsStateWithLifecycle()
    val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.session.isLoggedIn) {
        if (state.session.isLoggedIn) {
            navController.navigate("home") {
                popUpTo(0)
            }
        } else {
            navController.navigate("login") {
                popUpTo(0)
            }
        }
    }

    LaunchedEffect(authState.message, diaryState.message, settingsState.message) {
        val message = authState.message ?: diaryState.message ?: settingsState.message
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            authViewModel.consumeMessage()
            diaryViewModel.consumeMessage()
            settingsViewModel.consumeMessage()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (state.session.isLoggedIn) "home" else "login",
            modifier = Modifier.padding(paddingValues),
        ) {
            composable("login") {
                LoginScreen(
                    uiState = authState,
                    onLogin = authViewModel::login,
                    onNavigateRegister = { navController.navigate("register") },
                    onNavigateForgot = { navController.navigate("forgot") },
                    onDismissUpdate = authViewModel::dismissUpdate,
                )
            }
            composable("register") {
                RegisterScreen(
                    uiState = authState,
                    onSendCode = authViewModel::sendRegisterCode,
                    onRegister = authViewModel::register,
                    onBack = { navController.popBackStack() },
                )
            }
            composable("forgot") {
                ForgotPasswordScreen(
                    uiState = authState,
                    onSendCode = authViewModel::sendResetCode,
                    onReset = authViewModel::resetPassword,
                    onBack = { navController.popBackStack() },
                )
            }
            composable("home") {
                HomeScreen(
                    state = state,
                    diaryState = diaryState,
                    onSaveLog = diaryViewModel::saveLog,
                    onAddFriend = diaryViewModel::addFriend,
                    onEdit = {
                        diaryViewModel.loadForEdit(it)
                        navController.navigate("editor")
                    },
                    onRefresh = diaryViewModel::refresh,
                    onMoveMonth = diaryViewModel::moveMonth,
                    onSelectCalendarUser = diaryViewModel::selectCalendarUser,
                    onOccurredAtChange = diaryViewModel::updateOccurredAt,
                    onStartDurationTimer = diaryViewModel::startDurationTimer,
                    onStopDurationTimer = diaryViewModel::stopDurationTimer,
                    onStoolFormChange = diaryViewModel::updateStoolForm,
                    onToggleSymptom = diaryViewModel::toggleSymptomTag,
                    onDetailsChange = diaryViewModel::updateDetailNote,
                    onOpenSettings = { navController.navigate("settings") },
                    onLanguageChange = {
                        mainViewModel.updateLanguage(it)
                        settingsViewModel.updateLanguage(it)
                    },
                )
            }
            composable("editor") {
                EditorScaffold(
                    state = diaryState,
                    onOccurredAtChange = diaryViewModel::updateOccurredAt,
                    onStartDurationTimer = diaryViewModel::startDurationTimer,
                    onStopDurationTimer = diaryViewModel::stopDurationTimer,
                    onStoolFormChange = diaryViewModel::updateStoolForm,
                    onToggleSymptom = diaryViewModel::toggleSymptomTag,
                    onDetailsChange = diaryViewModel::updateDetailNote,
                    onSave = {
                        diaryViewModel.saveLog()
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable("settings") {
                SettingsScreen(
                    state = state,
                    settingsState = settingsState,
                    onUpdateDisplayName = settingsViewModel::updateDisplayName,
                    onUploadAvatar = settingsViewModel::uploadAvatar,
                    onExportLogs = settingsViewModel::exportLogs,
                    onImportLogs = settingsViewModel::importLogs,
                    onLanguageChange = {
                        mainViewModel.updateLanguage(it)
                        settingsViewModel.updateLanguage(it)
                    },
                    onLogout = settingsViewModel::logout,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
private fun LoginScreen(
    uiState: AuthUiState,
    onLogin: (String, String) -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateForgot: () -> Unit,
    onDismissUpdate: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    uiState.updateInfo?.let { updateInfo ->
        AlertDialog(
            onDismissRequest = onDismissUpdate,
            title = { Text(stringResource(R.string.update_available_title, updateInfo.versionName)) },
            text = {
                Text(
                    if (updateInfo.notes.isBlank()) {
                        stringResource(R.string.update_available_message)
                    } else {
                        updateInfo.notes
                    },
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        uriHandler.openUri(updateInfo.downloadUrl)
                        onDismissUpdate()
                    },
                ) {
                    Text(stringResource(R.string.download_update))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissUpdate) {
                    Text(stringResource(R.string.later))
                }
            },
        )
    }

    AuthScreenContainer(title = stringResource(R.string.login)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLogin(email, password) }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.login))
        }
        TextButton(onClick = onNavigateForgot) { Text(stringResource(R.string.forgot_password)) }
        TextButton(onClick = onNavigateRegister) { Text(stringResource(R.string.register)) }
        if (uiState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun RegisterScreen(
    uiState: AuthUiState,
    onSendCode: (String) -> Unit,
    onRegister: (String, String, String) -> Unit,
    onBack: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    AuthScreenContainer(title = stringResource(R.string.register)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text(stringResource(R.string.verification_code)) },
                modifier = Modifier.weight(1f),
            )
            Button(onClick = { onSendCode(email) }, modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(stringResource(R.string.send_code))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(R.string.confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { if (password == confirmPassword) onRegister(email, password, code) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.register))
        }
        TextButton(onClick = onBack) { Text(stringResource(R.string.login)) }
        if (uiState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun ForgotPasswordScreen(
    uiState: AuthUiState,
    onSendCode: (String) -> Unit,
    onReset: (String, String, String) -> Unit,
    onBack: () -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    AuthScreenContainer(title = stringResource(R.string.reset_password)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                label = { Text(stringResource(R.string.verification_code)) },
                modifier = Modifier.weight(1f),
            )
            Button(onClick = { onSendCode(email) }, modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(stringResource(R.string.send_code))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text(stringResource(R.string.confirm_password)) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { if (password == confirmPassword) onReset(email, code, password) },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(R.string.reset_password))
        }
        TextButton(onClick = onBack) { Text(stringResource(R.string.login)) }
        if (uiState.isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun AuthScreenContainer(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

@Composable
private fun HomeScreen(
    state: MainUiState,
    diaryState: DiaryUiState,
    onSaveLog: () -> Unit,
    onAddFriend: (String) -> Unit,
    onEdit: (String) -> Unit,
    onRefresh: () -> Unit,
    onMoveMonth: (Long) -> Unit,
    onSelectCalendarUser: (String?) -> Unit,
    onOccurredAtChange: (String) -> Unit,
    onStartDurationTimer: () -> Unit,
    onStopDurationTimer: () -> Unit,
    onStoolFormChange: (Float) -> Unit,
    onToggleSymptom: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onOpenSettings: () -> Unit,
    onLanguageChange: (String) -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar(
                profile = state.profile,
                languageTag = state.languageTag,
                onLanguageChange = onLanguageChange,
                onOpenSettings = onOpenSettings,
            )
        },
        bottomBar = {
            HomeBottomBar(selectedTab = selectedTab, onSelect = { selectedTab = it })
        },
    ) { paddingValues ->
        when (selectedTab) {
            0 -> RecordEntryContent(
                modifier = Modifier.padding(paddingValues),
                state = diaryState,
                onOccurredAtChange = onOccurredAtChange,
                onStartDurationTimer = onStartDurationTimer,
                onStopDurationTimer = onStopDurationTimer,
                onStoolFormChange = onStoolFormChange,
                onToggleSymptom = onToggleSymptom,
                onDetailsChange = onDetailsChange,
                onSave = onSaveLog,
                title = stringResource(R.string.new_entry),
                subtitle = stringResource(R.string.new_entry_subtitle),
            )

            1 -> CalendarScreen(
                modifier = Modifier.padding(paddingValues),
                state = diaryState,
                currentUserId = state.session.userId,
                onAddFriend = onAddFriend,
                onEdit = onEdit,
                onRefresh = onRefresh,
                onMoveMonth = onMoveMonth,
                onSelectCalendarUser = onSelectCalendarUser,
            )

            else -> StatsScreen(
                modifier = Modifier.padding(paddingValues),
                logs = diaryState.logs,
                selectedMonth = diaryState.selectedMonth,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    profile: com.dumpdiary.app.data.repository.UserProfileUi?,
    languageTag: String,
    onLanguageChange: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.95f),
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileAvatar(
                    avatarUrl = profile?.avatarUrl,
                    displayName = profile?.displayName ?: "?",
                    modifier = Modifier.size(42.dp),
                )
                Text(
                    text = stringResource(R.string.daily_rhythm),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
        },
        actions = {
            LanguageToggle(
                languageTag = languageTag,
                onLanguageChange = onLanguageChange,
            )
            IconButton(onClick = onOpenSettings) {
                Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
            }
        },
    )
}

@Composable
private fun LanguageToggle(
    languageTag: String,
    onLanguageChange: (String) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp),
            )
            LanguageToggleItem(
                text = "EN",
                selected = languageTag == "en",
                onClick = { onLanguageChange("en") },
            )
            LanguageToggleItem(
                text = "中文",
                selected = languageTag == "zh-CN",
                onClick = { onLanguageChange("zh-CN") },
            )
        }
    }
}

@Composable
private fun LanguageToggleItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.clickable(onClick = onClick),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun HomeBottomBar(
    selectedTab: Int,
    onSelect: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f),
            shape = RoundedCornerShape(32.dp),
            tonalElevation = 10.dp,
            shadowElevation = 18.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                homeTabs.forEachIndexed { index, item ->
                    val selected = selectedTab == index
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .clickable { onSelect(index) },
                        color = if (selected) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                        contentColor = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurfaceVariant,
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            item.icon()
                            Text(
                                text = stringResource(item.labelRes),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditorScaffold(
    state: DiaryUiState,
    onOccurredAtChange: (String) -> Unit,
    onStartDurationTimer: () -> Unit,
    onStopDurationTimer: () -> Unit,
    onStoolFormChange: (Float) -> Unit,
    onToggleSymptom: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.formId == null) stringResource(R.string.add_record) else stringResource(R.string.edit_record),
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { paddingValues ->
        RecordEntryContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onOccurredAtChange = onOccurredAtChange,
            onStartDurationTimer = onStartDurationTimer,
            onStopDurationTimer = onStopDurationTimer,
            onStoolFormChange = onStoolFormChange,
            onToggleSymptom = onToggleSymptom,
            onDetailsChange = onDetailsChange,
            onSave = onSave,
            title = if (state.formId == null) stringResource(R.string.new_entry) else stringResource(R.string.edit_record),
            subtitle = stringResource(R.string.new_entry_subtitle),
            contentPadding = PaddingValues(start = 24.dp, top = 8.dp, end = 24.dp, bottom = 28.dp),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecordEntryContent(
    state: DiaryUiState,
    onOccurredAtChange: (String) -> Unit,
    onStartDurationTimer: () -> Unit,
    onStopDurationTimer: () -> Unit,
    onStoolFormChange: (Float) -> Unit,
    onToggleSymptom: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    contentPadding: PaddingValues = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 24.dp),
) {
    val context = LocalContext.current
    val parsedOccurredAt = parseDateTimeOrNull(state.occurredAt) ?: LocalDateTime.now()
    var nowMillis by remember(state.timerStartedAt) { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(state.timerStartedAt) {
        if (state.timerStartedAt != null) {
            while (true) {
                nowMillis = System.currentTimeMillis()
                delay(1_000)
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PickerInputCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.date),
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    displayValue = formatDateDisplay(state.occurredAt),
                    selectionValue = parsedOccurredAt.toLocalDate().toString(),
                    onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val chosenDate = LocalDate.of(year, month + 1, dayOfMonth).toString()
                                onOccurredAtChange(mergeDateAndTime(chosenDate, parsedOccurredAt.toLocalTime().format(timeValueFormatter)))
                            },
                            parsedOccurredAt.year,
                            parsedOccurredAt.monthValue - 1,
                            parsedOccurredAt.dayOfMonth,
                        ).show()
                    },
                    buttonText = stringResource(R.string.choose_date),
                )
                PickerInputCard(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.time),
                    icon = { Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    displayValue = formatTimeDisplay(state.occurredAt),
                    selectionValue = parsedOccurredAt.toLocalTime().format(timeValueFormatter),
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                val chosenTime = String.format(Locale.US, "%02d:%02d", hourOfDay, minute)
                                onOccurredAtChange(mergeDateAndTime(parsedOccurredAt.toLocalDate().toString(), chosenTime))
                            },
                            parsedOccurredAt.hour,
                            parsedOccurredAt.minute,
                            DateFormat.is24HourFormat(context),
                        ).show()
                    },
                    buttonText = stringResource(R.string.choose_time),
                )
            }
        }
        item {
            DurationTrackerCard(
                modifier = Modifier.fillMaxWidth(),
                durationText = buildTrackedDurationLabel(
                    durationValue = state.durationSeconds,
                    timerStartedAt = state.timerStartedAt,
                    nowMillis = nowMillis,
                ),
                isRunning = state.timerStartedAt != null,
                onStart = onStartDurationTimer,
                onStop = onStopDurationTimer,
            )
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = stringResource(R.string.consistency),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(R.string.bristol_scale),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = 3,
                ) {
                    stoolOptions.filterNot { it.value == 4 }.forEach { option ->
                        StoolOptionCard(
                            option = option,
                            selected = option.value == state.stoolForm.roundToInt(),
                            onSelect = { onStoolFormChange(option.value.toFloat()) },
                            modifier = Modifier.weight(1f, fill = true),
                        )
                    }
                }
                stoolOptions.firstOrNull { it.value == 4 }?.let { smoothOption ->
                    StoolOptionCard(
                        option = smoothOption,
                        selected = smoothOption.value == state.stoolForm.roundToInt(),
                        onSelect = { onStoolFormChange(smoothOption.value.toFloat()) },
                        modifier = Modifier.fillMaxWidth(),
                        fullWidth = true,
                    )
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.symptoms_details),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    symptomItems.forEach { (tagKey, labelRes) ->
                        val selected = state.symptomTags.contains(tagKey)
                        FilterChip(
                            selected = selected,
                            onClick = { onToggleSymptom(tagKey) },
                            label = { Text(stringResource(labelRes)) },
                            leadingIcon = if (selected) {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp)) }
                            } else {
                                null
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.secondary,
                            ),
                        )
                    }
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.journal_notes),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                OutlinedTextField(
                    value = state.detailNote,
                    onValueChange = onDetailsChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 148.dp),
                    placeholder = { Text(stringResource(R.string.journal_placeholder)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    ),
                    shape = RoundedCornerShape(24.dp),
                )
            }
        }
        item {
            GradientActionButton(text = stringResource(R.string.log_activity), onClick = onSave)
        }
    }
}

@Composable
private fun PickerInputCard(
    label: String,
    displayValue: String,
    selectionValue: String,
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(154.dp)
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = label.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                icon()
                Text(
                    text = displayValue,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)),
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = selectionValue,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = buttonText,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun DurationTrackerCard(
    durationText: String,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.duration),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = durationText,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.size(12.dp))
            GradientActionButton(
                text = if (isRunning) stringResource(R.string.stop_timer) else stringResource(R.string.start_timer),
                onClick = if (isRunning) onStop else onStart,
                compact = true,
            )
        }
    }
}

@Composable
private fun StoolOptionCard(
    option: StoolOption,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    fullWidth: Boolean = false,
) {
    Surface(
        modifier = modifier.clickable(onClick = onSelect),
        color = if (selected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        shape = RoundedCornerShape(24.dp),
        tonalElevation = if (selected) 4.dp else 0.dp,
        border = if (selected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
    ) {
        if (fullWidth) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 72.dp)
                    .padding(horizontal = 18.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(option.circleColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.onSecondary else option.contentColor,
                        modifier = Modifier.size(26.dp),
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = option.value.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = stringResource(option.labelRes),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (selected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(option.circleColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = if (selected) MaterialTheme.colorScheme.onSecondary else option.contentColor,
                        modifier = Modifier.size(24.dp),
                    )
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(16.dp)
                                .background(MaterialTheme.colorScheme.secondary, CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                }
                Text(
                    text = option.value.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = stringResource(option.labelRes),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun GradientActionButton(
    text: String,
    onClick: () -> Unit,
    compact: Boolean = false,
) {
    Box(
        modifier = Modifier
            .then(if (compact) Modifier else Modifier.fillMaxWidth())
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF705A52), Color(0xFF634E47)),
                ),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = if (compact) 18.dp else 0.dp, vertical = if (compact) 14.dp else 18.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CalendarScreen(
    modifier: Modifier = Modifier,
    state: DiaryUiState,
    currentUserId: String,
    onAddFriend: (String) -> Unit,
    onEdit: (String) -> Unit,
    onRefresh: () -> Unit,
    onMoveMonth: (Long) -> Unit,
    onSelectCalendarUser: (String?) -> Unit,
) {
    val selectedCalendarUserId = state.selectedCalendarUserId
        ?.takeIf { selectedId -> state.friends.any { it.userId == selectedId } }
        ?: currentUserId
    val monthLogs = state.logs.filter {
        it.userId == selectedCalendarUserId && it.dateKey.startsWith(state.selectedMonth.toString())
    }
    val activeDays = monthLogs.map { it.dateKey }.distinct().size
    val daysInScope = if (state.selectedMonth == YearMonth.now()) {
        LocalDate.now().dayOfMonth.coerceAtMost(state.selectedMonth.lengthOfMonth())
    } else {
        state.selectedMonth.lengthOfMonth()
    }
    val averagePerDay = if (daysInScope == 0) 0.0 else monthLogs.size.toDouble() / daysInScope.toDouble()
    val consistencyScore = if (daysInScope == 0) 0 else ((activeDays.toDouble() / daysInScope.toDouble()) * 100).roundToInt()
    val streak = calculateStreak(state.logs.filter { !it.isDeleted && it.userId == selectedCalendarUserId })
    val insight = buildCalendarInsight(monthLogs = monthLogs, currentStreak = streak.first)
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            CalendarHeader(
                month = state.selectedMonth,
                onPrevious = { onMoveMonth(-1) },
                onNext = { onMoveMonth(1) },
            )
        }
        item {
            CalendarOwnerToggle(
                currentUserId = currentUserId,
                selectedUserId = selectedCalendarUserId,
                friends = state.friends,
                onSelect = onSelectCalendarUser,
            )
        }
        item {
            CalendarMonthCard(
                month = state.selectedMonth,
                logs = monthLogs,
                currentUserId = currentUserId,
                onEdit = onEdit,
            )
        }
        item {
            CalendarSummaryHero(score = consistencyScore)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                CalendarMetricCard(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    },
                    value = monthLogs.size.toString(),
                    label = stringResource(R.string.total_logs_this_month),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                CalendarMetricCard(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    value = String.format(Locale.getDefault(), "%.1f", averagePerDay),
                    label = stringResource(R.string.logs_per_day_average),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        item {
            InsightCard(
                title = stringResource(R.string.weekly_insight),
                text = insight,
            )
        }
        item {
            FilledTonalButton(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.refresh))
            }
        }
        item {
            FriendSharePanel(
                friends = state.friends,
                onAddFriend = onAddFriend,
            )
        }
    }
}

@Composable
private fun CalendarOwnerToggle(
    currentUserId: String,
    selectedUserId: String,
    friends: List<FriendUi>,
    onSelect: (String?) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item {
            CalendarOwnerChip(
                label = stringResource(R.string.me),
                selected = selectedUserId == currentUserId,
                onClick = { onSelect(null) },
            )
        }
        items(friends, key = { it.userId }) { friend ->
            CalendarOwnerChip(
                label = friend.displayName.ifBlank { friend.email },
                selected = selectedUserId == friend.userId,
                onClick = { onSelect(friend.userId) },
            )
        }
    }
}

@Composable
private fun CalendarOwnerChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.White,
        tonalElevation = if (selected) 2.dp else 0.dp,
        shadowElevation = if (selected) 2.dp else 0.dp,
        border = if (selected) {
            null
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
        },
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CalendarHeader(
    month: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = stringResource(R.string.health_rhythm),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = formatCalendarMonthTitle(month),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalendarNavButton(icon = Icons.Default.ChevronLeft, onClick = onPrevious)
            CalendarNavButton(icon = Icons.Default.ChevronRight, onClick = onNext)
        }
    }
}

@Composable
private fun CalendarNavButton(
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(42.dp)
            .clickable(onClick = onClick),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun CalendarSummaryHero(score: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 4.dp)
                    .size(92.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), CircleShape),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(R.string.consistency_score),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = consistencyTitle(score),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$score%",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(R.string.monthly_peak),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.65f),
                    )
                }
            }
        }
    }
}

@Composable
private fun CalendarMetricCard(
    value: String,
    label: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .height(156.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            icon()
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = contentColor,
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor.copy(alpha = 0.78f),
                )
            }
        }
    }
}

@Composable
private fun InsightCard(
    title: String,
    text: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            2.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
        ),
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = CircleShape,
            ) {
                Box(
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LogCard(
    log: BowelLogEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(log.occurredAt.replace("T", " "), fontWeight = FontWeight.SemiBold)
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = null) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = null) }
                }
            }
            Text(
                text = "${log.snapshotDisplayName} | Bristol ${log.stoolForm} | ${buildTrackedDurationLabel(log.durationSeconds.toString(), null, 0L)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (log.symptomTags().isNotEmpty()) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    log.symptomTags().forEach { tag ->
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(999.dp),
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    }
                }
            }
            if (log.detailNote.isNotBlank()) {
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                Text(log.detailNote, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun StatsScreen(
    modifier: Modifier = Modifier,
    logs: List<BowelLogEntity>,
    selectedMonth: YearMonth,
) {
    val activeLogs = logs.filter { !it.isDeleted }
    var range by rememberSaveable { mutableStateOf(StatsRange.Month) }
    val monthlyLogs = activeLogs.filter { it.dateKey.startsWith(selectedMonth.toString()) }
    val monthlyTrend = remember(monthlyLogs, selectedMonth) { calculateWeeklyTrend(monthlyLogs, selectedMonth) }
    val yearlyTrend = remember(activeLogs, selectedMonth) { calculateYearlyTrend(activeLogs, selectedMonth.year) }
    val scopeLogs = if (range == StatsRange.Month) monthlyLogs else activeLogs.filter {
        LocalDate.parse(it.dateKey).year == selectedMonth.year
    }
    val trendPoints = if (range == StatsRange.Month) monthlyTrend else yearlyTrend
    val totalCount = scopeLogs.size
    val averageValue = if (trendPoints.isEmpty()) 0.0 else trendPoints.map { it.value }.average()
    val trendDelta = calculateTrendDelta(trendPoints)
    val peakTime = calculatePeakTimeLabel(scopeLogs)
    val insightText = buildStatsInsight(scopeLogs, range, peakTime)
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        item {
            StatsRangeToggle(
                selectedRange = range,
                onSelect = { range = it },
            )
        }
        item {
            StatsTrendCard(
                title = if (range == StatsRange.Month) stringResource(R.string.daily_flow) else stringResource(R.string.yearly_flow),
                subtitle = stringResource(R.string.activity_volume),
                trendDelta = trendDelta,
                points = trendPoints,
            )
        }
        item {
            PeakInsightCard(peakTime = peakTime)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StatsMetricBento(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    title = stringResource(R.string.total_logs),
                    value = totalCount.toString(),
                    suffix = if (range == StatsRange.Month) stringResource(R.string.monthly) else selectedMonth.year.toString(),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                )
                StatsMetricBento(
                    modifier = Modifier.weight(1f),
                    icon = {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    },
                    title = if (range == StatsRange.Month) stringResource(R.string.avg_weekly) else stringResource(R.string.avg_monthly),
                    value = String.format(Locale.getDefault(), "%.1f", averageValue),
                    suffix = if (range == StatsRange.Month) stringResource(R.string.visits) else stringResource(R.string.logs),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        item {
            InsightCard(
                title = stringResource(R.string.consistency_is_key),
                text = insightText,
            )
        }
    }
}

private enum class StatsRange {
    Month,
    Year,
}

@Composable
private fun StatsRangeToggle(
    selectedRange: StatsRange,
    onSelect: (StatsRange) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
        shape = RoundedCornerShape(999.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            StatsRange.entries.forEach { range ->
                val selected = range == selectedRange
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(999.dp))
                        .clickable { onSelect(range) },
                    color = if (selected) Color.White else Color.Transparent,
                    tonalElevation = if (selected) 2.dp else 0.dp,
                    shadowElevation = if (selected) 4.dp else 0.dp,
                ) {
                    Text(
                        text = if (range == StatsRange.Month) stringResource(R.string.month) else stringResource(R.string.year),
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsTrendCard(
    title: String,
    subtitle: String,
    trendDelta: Int,
    points: List<TrendPoint>,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                TrendDeltaChip(delta = trendDelta)
            }
            StatsLineChart(points = points)
        }
    }
}

@Composable
private fun TrendDeltaChip(delta: Int) {
    val sign = if (delta >= 0) "+" else ""
    Surface(
        shape = RoundedCornerShape(999.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = Icons.Default.ShowChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(16.dp),
            )
            Text(
                text = "$sign$delta%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    }
}

@Composable
private fun PeakInsightCard(peakTime: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp,
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 18.dp)
                    .size(92.dp),
            )
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = stringResource(R.string.peak_insight),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Text(
                    text = stringResource(R.string.most_active_at, peakTime),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(R.string.consistent_patterns_detected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
                )
            }
        }
    }
}

@Composable
private fun StatsMetricBento(
    title: String,
    value: String,
    suffix: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        tonalElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .height(156.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.55f),
            ) {
                Box(
                    modifier = Modifier.padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    icon()
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor.copy(alpha = 0.75f),
                )
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = contentColor,
                    )
                    Text(
                        text = suffix,
                        style = MaterialTheme.typography.bodySmall,
                        color = contentColor.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: MainUiState,
    settingsState: SettingsUiState,
    onUpdateDisplayName: (String) -> Unit,
    onUploadAvatar: (Uri) -> Unit,
    onExportLogs: (Uri) -> Unit,
    onImportLogs: (Uri) -> Unit,
    onLanguageChange: (String) -> Unit,
    onLogout: () -> Unit,
    onBack: () -> Unit,
) {
    var nickname by rememberSaveable(state.profile?.displayName) { mutableStateOf(state.profile?.displayName.orEmpty()) }
    val avatarLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) onUploadAvatar(uri)
    }
    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) onExportLogs(uri)
    }
    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) onImportLogs(uri)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                start = 24.dp,
                top = paddingValues.calculateTopPadding() + 12.dp,
                end = 24.dp,
                bottom = 32.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    tonalElevation = 3.dp,
                    shadowElevation = 8.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ProfileAvatar(
                            avatarUrl = state.profile?.avatarUrl,
                            displayName = state.profile?.displayName ?: "?",
                            modifier = Modifier.size(88.dp),
                        )
                        Text(state.session.email)
                        OutlinedButton(onClick = {
                            avatarLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }) {
                            Text(stringResource(R.string.choose_avatar))
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text(stringResource(R.string.nickname)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            item {
                Button(onClick = { onUpdateDisplayName(nickname) }, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.save))
                }
            }
            item {
                Text(stringResource(R.string.language), style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { onLanguageChange("zh-CN") }) { Text("中文") }
                    OutlinedButton(onClick = { onLanguageChange("en") }) { Text("EN") }
                }
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    tonalElevation = 3.dp,
                    shadowElevation = 6.dp,
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.data_portability),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text = stringResource(R.string.data_portability_hint),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            OutlinedButton(
                                onClick = {
                                    exportLauncher.launch("dump-diary-${LocalDate.now()}.csv")
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.export_csv))
                            }
                            Button(
                                onClick = {
                                    importLauncher.launch(arrayOf("text/csv", "text/comma-separated-values", "application/csv", "*/*"))
                                },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(stringResource(R.string.import_csv))
                            }
                        }
                    }
                }
            }
            item {
                OutlinedButton(onClick = onLogout, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.logout))
                }
            }
            item {
                if (settingsState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun ProfileAvatar(
    avatarUrl: String?,
    displayName: String,
    modifier: Modifier = Modifier,
) {
    if (avatarUrl != null) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            modifier = modifier
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
        )
    } else {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = displayName.take(1).uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun CalendarMonthCard(
    month: YearMonth,
    logs: List<BowelLogEntity>,
    currentUserId: String,
    onEdit: (String) -> Unit,
) {
    val cells = remember(month, logs) { buildCalendarCells(month, logs) }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach { day ->
                    Text(day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }
            cells.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { cell ->
                        CalendarDayCell(
                            modifier = Modifier.weight(1f),
                            cell = cell,
                            onOpenLatest = {
                                cell.logs
                                    .filter { it.userId == currentUserId }
                                    .maxByOrNull { it.occurredAt }
                                    ?.id
                                    ?.let(onEdit)
                            },
                            canOpen = cell.logs.any { it.userId == currentUserId },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    cell: CalendarCellData,
    onOpenLatest: () -> Unit,
    canOpen: Boolean,
    modifier: Modifier = Modifier,
) {
    val hasLogs = cell.logs.isNotEmpty()
    val backgroundColor = when {
        hasLogs -> calendarLoggedDayBackground
        else -> Color.Transparent
    }
    val textColor = when {
        hasLogs -> MaterialTheme.colorScheme.primary
        cell.inCurrentMonth -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f)
    }
    Column(
        modifier = modifier
            .height(66.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .clickable(enabled = hasLogs && canOpen, onClick = onOpenLatest)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = cell.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (cell.isHighlighted || hasLogs) FontWeight.Bold else FontWeight.SemiBold,
            color = textColor,
        )
        if (hasLogs) {
            PoopCountIndicator(
                count = cell.logs.size,
                tint = MaterialTheme.colorScheme.primary,
            )
        } else {
            Spacer(modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun FriendSharePanel(
    friends: List<com.dumpdiary.app.data.repository.FriendUi>,
    onAddFriend: (String) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = stringResource(R.string.share_with_friends),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(R.string.shared_logs_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(R.string.friend_email)) },
                    placeholder = { Text(stringResource(R.string.friend_email_hint)) },
                    singleLine = true,
                )
                Button(
                    onClick = {
                        onAddFriend(email)
                        email = ""
                    },
                ) {
                    Text(stringResource(R.string.add_friend))
                }
            }
            Text(
                text = stringResource(R.string.connected_friends),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (friends.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_friends_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    friends.forEach { friend ->
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                ProfileAvatar(
                                    avatarUrl = friend.avatarUrl,
                                    displayName = friend.displayName,
                                    modifier = Modifier.size(42.dp),
                                )
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = friend.displayName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                    Text(
                                        text = friend.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.shared_now),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PoopCountIndicator(
    count: Int,
    tint: Color,
) {
    FlowRow(
        modifier = Modifier.heightIn(min = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(1.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        maxItemsInEachRow = 3,
    ) {
        repeat(count) {
            Text(
                text = "💩",
                fontSize = 10.sp,
                lineHeight = 10.sp,
                color = tint,
            )
        }
    }
}

@Composable
private fun StatsLineChart(points: List<TrendPoint>) {
    val max = (points.maxOfOrNull { it.value } ?: 1).coerceAtLeast(1)
    val lineColor = MaterialTheme.colorScheme.primary
    val endColor = MaterialTheme.colorScheme.tertiary
    val pointColor = MaterialTheme.colorScheme.primary
    val gridColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(210.dp)) {
            if (points.isEmpty()) return@Canvas
            val horizontalPadding = 22.dp.toPx()
            val topPadding = 18.dp.toPx()
            val bottomPadding = 22.dp.toPx()
            val chartWidth = size.width - horizontalPadding * 2
            val chartHeight = size.height - topPadding - bottomPadding
            val stepX = if (points.size > 1) chartWidth / (points.size - 1) else 0f

            repeat(3) { index ->
                val y = topPadding + (chartHeight / 2f) * index
                drawLine(
                    color = gridColor,
                    start = Offset(horizontalPadding, y),
                    end = Offset(size.width - horizontalPadding, y),
                    strokeWidth = 2f,
                )
            }

            val offsets = points.mapIndexed { index, point ->
                Offset(
                    x = horizontalPadding + (stepX * index),
                    y = topPadding + chartHeight - ((point.value / max.toFloat()) * chartHeight),
                )
            }

            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(offsets.first().x, offsets.first().y)
                for (index in 1 until offsets.size) {
                    val previous = offsets[index - 1]
                    val current = offsets[index]
                    val controlX = (previous.x + current.x) / 2f
                    cubicTo(
                        controlX,
                        previous.y,
                        controlX,
                        current.y,
                        current.x,
                        current.y,
                    )
                }
            }
            drawPath(
                path = path,
                brush = Brush.horizontalGradient(listOf(lineColor, endColor)),
                style = Stroke(width = 7f, cap = StrokeCap.Round),
            )
            offsets.forEach { offset ->
                drawCircle(color = Color.White, radius = 8f, center = offset)
                drawCircle(color = pointColor, radius = 5f, center = offset)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            points.forEach { point ->
                Text(
                    text = point.label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

private fun calculateWeeklyTrend(
    logs: List<BowelLogEntity>,
    month: YearMonth,
): List<TrendPoint> {
    val weeklyCounts = mutableMapOf<Int, Int>()
    for (week in 1..5) weeklyCounts[week] = 0
    logs.forEach { log ->
        val date = LocalDate.parse(log.dateKey)
        if (YearMonth.from(date) == month) {
            val week = ((date.dayOfMonth - 1) / 7) + 1
            weeklyCounts[week] = (weeklyCounts[week] ?: 0) + 1
        }
    }
    return weeklyCounts
        .entries
        .filter { it.key <= 4 || it.value > 0 }
        .map { TrendPoint(label = "WK ${it.key}", value = it.value) }
}

private fun calculateYearlyTrend(logs: List<BowelLogEntity>, year: Int): List<TrendPoint> {
    val counts = (1..12).associateWith { 0 }.toMutableMap()
    logs.filter { LocalDate.parse(it.dateKey).year == year }.forEach { log ->
        val month = LocalDate.parse(log.dateKey).monthValue
        counts[month] = (counts[month] ?: 0) + 1
    }
    return (1..12).map { month ->
        TrendPoint(
            label = java.time.Month.of(month).getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
            value = counts[month] ?: 0,
        )
    }
}

private fun calculateTrendDelta(points: List<TrendPoint>): Int {
    if (points.size < 2) return 0
    val previous = points[points.lastIndex - 1].value
    val current = points.last().value
    if (previous == 0) return if (current == 0) 0 else 100
    return (((current - previous).toDouble() / previous.toDouble()) * 100).roundToInt()
}

private fun calculatePeakTimeLabel(logs: List<BowelLogEntity>): String {
    val peakHour = logs
        .mapNotNull { parseDateTimeOrNull(it.occurredAt) }
        .groupingBy { it.hour }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
        ?: 9
    val peakMinute = logs
        .mapNotNull { parseDateTimeOrNull(it.occurredAt) }
        .filter { it.hour == peakHour }
        .groupingBy { (it.minute / 10) * 10 }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
        ?: 30
    return LocalDateTime.of(2026, 1, 1, peakHour, peakMinute)
        .format(displayTimeFormatter)
}

@Composable
private fun buildStatsInsight(
    logs: List<BowelLogEntity>,
    range: StatsRange,
    peakTime: String,
): String {
    if (logs.isEmpty()) return stringResource(R.string.stats_insight_empty)
    val morningLogs = logs.count {
        parseDateTimeOrNull(it.occurredAt)?.hour?.let { hour -> hour in 6..11 } == true
    }
    val ratio = ((morningLogs.toDouble() / logs.size.toDouble()) * 100).roundToInt()
    return if (range == StatsRange.Month) {
        stringResource(R.string.stats_insight_morning, ratio, peakTime)
    } else {
        stringResource(R.string.stats_insight_year, logs.size, peakTime)
    }
}

private fun calculateStreak(logs: List<BowelLogEntity>): Pair<Int, Int> {
    val dates = logs.map { LocalDate.parse(it.dateKey) }.distinct().sorted()
    if (dates.isEmpty()) return 0 to 0
    var maxStreak = 1
    var current = 1
    for (index in 1 until dates.size) {
        current = if (dates[index - 1].plusDays(1) == dates[index]) current + 1 else 1
        maxStreak = maxOf(maxStreak, current)
    }
    var tail = 1
    for (index in dates.lastIndex downTo 1) {
        if (dates[index - 1].plusDays(1) == dates[index]) tail++ else break
    }
    val lastDate = dates.last()
    val currentStreak = if (lastDate == LocalDate.now() || lastDate == LocalDate.now().minusDays(1)) tail else 0
    return currentStreak to maxStreak
}

private fun buildCalendarCells(
    month: YearMonth,
    logs: List<BowelLogEntity>,
): List<CalendarCellData> {
    val logsByDate = logs.groupBy { LocalDate.parse(it.dateKey) }
    val firstDay = month.atDay(1)
    val leadingDays = firstDay.dayOfWeek.value % 7
    val gridStart = firstDay.minusDays(leadingDays.toLong())
    val lastLoggedDate = logs.maxByOrNull { it.occurredAt }?.let { LocalDate.parse(it.dateKey) }
    val highlightedDate = when {
        month == YearMonth.now() -> LocalDate.now()
        lastLoggedDate?.let { YearMonth.from(it) == month } == true -> lastLoggedDate
        else -> month.atDay(1)
    }
    return List(42) { index ->
        val date = gridStart.plusDays(index.toLong())
        CalendarCellData(
            date = date,
            inCurrentMonth = YearMonth.from(date) == month,
            logs = logsByDate[date].orEmpty(),
            isHighlighted = date == highlightedDate,
        )
    }
}

private fun stoolIconForLogs(logs: List<BowelLogEntity>): ImageVector {
    if (logs.isEmpty()) return Icons.Default.Texture
    val dominantForm = logs.groupingBy { it.stoolForm }.eachCount().maxByOrNull { it.value }?.key ?: 4
    return stoolOptionForForm(dominantForm)?.icon ?: Icons.Default.Texture
}

private fun stoolOptionForForm(form: Int): StoolOption? =
    stoolOptions.minByOrNull { kotlin.math.abs(it.value - form) }

@Composable
private fun consistencyTitle(score: Int): String = when {
    score >= 75 -> stringResource(R.string.excellent)
    score >= 45 -> stringResource(R.string.steady)
    else -> stringResource(R.string.building_rhythm)
}

@Composable
private fun buildCalendarInsight(
    monthLogs: List<BowelLogEntity>,
    currentStreak: Int,
): String {
    if (monthLogs.isEmpty()) return stringResource(R.string.insight_general, 0)
    val weekendLogs = monthLogs.count {
        when (LocalDate.parse(it.dateKey).dayOfWeek) {
            java.time.DayOfWeek.SATURDAY, java.time.DayOfWeek.SUNDAY -> true
            else -> false
        }
    }
    val weekdayLogs = monthLogs.size - weekendLogs
    return when {
        weekendLogs > weekdayLogs -> stringResource(R.string.insight_weekend)
        currentStreak >= 3 -> stringResource(R.string.insight_streak, currentStreak)
        else -> stringResource(R.string.insight_general, monthLogs.size)
    }
}

private fun formatCalendarMonthTitle(month: YearMonth): String =
    month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } +
        " " + month.year

private fun formatDateDisplay(occurredAt: String): String =
    parseDateTimeOrNull(occurredAt)?.format(displayDateFormatter) ?: occurredAt.substringBefore("T")

private fun formatTimeDisplay(occurredAt: String): String =
    parseDateTimeOrNull(occurredAt)?.format(displayTimeFormatter) ?: occurredAt.substringAfter("T", "09:00")

private fun parseDateTimeOrNull(value: String): LocalDateTime? =
    runCatching { LocalDateTime.parse(value, editorDateTimeFormatter) }.getOrNull()

private fun mergeDateAndTime(date: String, time: String): String {
    val safeDate = date.ifBlank { LocalDate.now().toString() }
    val safeTime = time.ifBlank { "09:00" }
    return "$safeDate" + "T" + safeTime
}

private fun buildTrackedDurationLabel(
    durationValue: String,
    timerStartedAt: String?,
    nowMillis: Long,
): String {
    if (timerStartedAt == null) {
        return formatDuration(durationValue.toIntOrNull() ?: 0)
    }
    val startedAt = parseDateTimeOrNull(timerStartedAt) ?: return formatDuration(durationValue.toIntOrNull() ?: 0)
    val elapsedMillis = nowMillis - startedAt.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    val seconds = (elapsedMillis.coerceAtLeast(0) / 1_000L).toInt()
    return formatDuration(seconds)
}

private fun formatDuration(totalSeconds: Int): String {
    val safeSeconds = totalSeconds.coerceAtLeast(0)
    val minutes = safeSeconds / 60
    val seconds = safeSeconds % 60
    return "${minutes}m ${seconds}s"
}
