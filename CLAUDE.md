# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Monorepo for a bowel-tracking app with 4 modules:

- **`app/`** — Android client: Kotlin, Jetpack Compose, Room, Hilt, DataStore, WorkManager, Retrofit
- **`backend/`** — Ktor (Netty) service: Kotlin, file-based JSON persistence, Bearer auth
- **`backend-node/`** — Express.js service: interchangeable with the Ktor backend, designed for Docker/NAS deployment
- **`dump-web/`** — React 19 web app: Vite, Supabase (optional cloud mode), GitHub Pages deployment

## Build & run

### Android app

```bash
./gradlew :app:assembleDebug          # build debug APK
./gradlew :app:exportReleaseApk       # build signed release APK
```

The Android client resolves its API base URL from the Gradle property `dumpDiaryApiBaseUrl` (or falls back to `https://placeholder.invalid/`). The OkHttp interceptor in `AppModule.kt` rewrites the URL at runtime based on the user's saved server address stored in DataStore.

### Ktor backend

```bash
# Configure SMTP first:
cp backend/.env.example backend/.env
# Edit backend/.env with real SMTP settings

./scripts/start-backend.sh            # starts on :8080
./scripts/stop-backend.sh
# Or directly:
./gradlew :backend:run
```

### Node backend

```bash
cd backend-node
cp .env.example .env
npm install
npm start                             # Express on :8080
# Docker:
docker compose up -d --build
```

### Web app (dump-web)

```bash
cd dump-web
cp .env.example .env                  # fill in Supabase URL + anon key for cloud mode
npm install
npm run dev                           # Vite dev server
npm run build                         # production build
npm run lint                          # ESLint
```

## Architecture

### Android app (`app/`)

**Navigation**: Single-Activity Compose app. `DumpDiaryApp.kt` hosts a `NavHost` with routes: `login`, `register`, `forgot`, `home`, `editor`, `settings`. `MainActivity.kt` sets the system bar colors and language (`MainViewModel` holds `serverBaseUrl`).

**Data layer** (all in `app/src/main/java/com/dumpdiary/app/data/`):
- `remote/DumpDiaryApi.kt` — Retrofit interface matching the Ktor backend's API surface
- `local/AppDatabase.kt` — Room DB with two DAOs: `ProfileDao` (single-row profile cache) and `LogDao` (all bowel logs, including soft-deleted and pending-sync rows)
- `local/UserPreferencesRepository.kt` — DataStore for session tokens, language, and server URL
- `model/AppModels.kt` — Room entities (`BowelLogEntity`, `UserProfileEntity`) and DTOs; sync state is tracked via `pendingSyncAction` (`"UPSERT"` / `"DELETE"`)
- `repository/AppRepositories.kt` — single file containing all repositories:
  - `AuthRepository` — login/register/refresh/logout, persists session to DataStore + profile to Room
  - `ProfileRepository` — profile CRUD + avatar upload
  - `FriendRepository` — in-memory `StateFlow<List<FriendUi>>`
  - `LogRepository` — local-first log CRUD: writes local Room row with `pendingSyncAction`, then enqueues `SyncWorker`. Handles CSV export/import
  - `AppUpdateRepository` — checks `/app/version` against `BuildConfig.APP_VERSION_CODE`

**DI**: `AppModule.kt` provides OkHttp (with auth header injection + 401 auto-refresh authenticator), Retrofit, Room, WorkManager. All ViewModels use `@HiltViewModel`.

**Sync**: `SyncWorker` (`worker/SyncWorker.kt`) is a `CoroutineWorker` that calls profile refresh → friend refresh → push pending changes → pull remote logs, in order. Enqueued as a unique work chain (`ExistingWorkPolicy.REPLACE`) after every local mutation.

**ViewModels**:
- `AuthViewModel` — login/register/password reset flows, server URL validation, app update check
- `DiaryViewModel` — manages `DiaryUiState` (calendar month, form fields, timer, log list). `derivedFeeling` maps symptom tags to a feeling enum
- `SettingsViewModel` — profile editing, server switching, CSV import/export, language toggle
- `MainViewModel` — app-level state (session, language, update prompt)

### Ktor backend (`backend/`)

Entry point: `Application.kt` → `embeddedServer(Netty, port=8080)`.

**Layers**:
- `routes/` — `AuthRoutes`, `ProfileRoutes`, `LogRoutes`, `FriendRoutes`, `StatsRoutes` (Ktor route extensions)
- `service/` — `AuthService` (registration/login/password reset logic), `EmailSender` (SMTP via Jakarta Mail, falls back to dev-mode console output if unconfigured)
- `security/TokenService` — UUID-based access/refresh tokens; access tokens are memory-only, refresh tokens persist to `store.json`
- `repository/InMemoryStore` — `ConcurrentHashMap`-based store, snapshotted to `backend/data/store.json` on every mutation. Contains accounts, profiles, logs, friendships, verification codes, refresh tokens
- `model/Models.kt` — shared DTOs with `@Serializable`

**Auth flow**: Bearer token auth via Ktor `Authentication` plugin. The `authenticator` in `AppModule.kt` handles 401 → refresh → retry transparently on the client.

**Friend data sharing**: `InMemoryStore.visibleLogs(userId)` returns the user's own logs plus all friends' logs. Friendships are bidirectional.

### Node backend (`backend-node/`)

Single-file Express server (`src/server.js`) with the same API surface as the Ktor backend. Uses `bcryptjs` for password hashing, `nodemailer` for email, `multer` for avatar uploads. Persistence via `src/store.js` (JSON file, same schema).

### Web app (`dump-web/`)

React 19 SPA with `react-router-dom` (HashRouter, deployed to GitHub Pages at `/DumpDiary/` base path). Has its own auth system:
- `AuthContext` — username/password login with admin approval workflow. Admin hardcoded in `ADMIN_USERNAME`/`ADMIN_PASSWORD`. Supports local mode (`localStorage`) or cloud mode (Supabase `app_users` table)
- `DumpDiaryContext` — shared bowel log state synced via "sync rooms" (room code = pair code hash). Local persistence via `localStorage` + `IndexedDB`; cloud sync via Supabase `app_sync_rooms` table with debounced writes (450ms) and polling reads (3.2s interval)
- Pages: `LoginPage`, `RecordPage`, `StatsPage`, `SettingsPage`, `AdminPage` (user approval dashboard)
- Charts: ECharts 6 via `StatsCharts.jsx` (Bristol type distribution, hour distribution, frequency trend)

### Shared conventions

- All date/time strings use ISO format (`yyyy-MM-dd'T'HH:mm`, `yyyy-MM-dd`)
- Bowel log `dateKey` = first 10 chars of `occurredAt` (the date portion)
- Symptom tags stored as pipe-delimited strings in Room, deserialized to `List<String>` at the API/UI boundary
- Latest app version is served from env vars (`DUMPDIARY_LATEST_VERSION_CODE`/`_NAME`/`_NOTES`) on both backends
- Android app uses `kotlinx.datetime.Clock.System.now()` for timestamps; backends use `Instant.now()` or `new Date().toISOString()`
