# DumpDiary

Monorepo MVP for an Android bowel-tracking app plus a Kotlin backend.

## Modules

- `app`: Android client built with Kotlin, Jetpack Compose, Room, Hilt, DataStore, WorkManager, Retrofit
- `backend`: Ktor service exposing auth, profile, log, and stats endpoints

## Implemented MVP scope

- Email + password registration and login
- Email verification code send/verify for registration and password reset
- Editable profile nickname and avatar upload
- Local-first bowel log storage with pending sync markers
- Calendar-style monthly aggregation
- Monthly totals, streak calculation, yearly trend chart
- Chinese and English string resources

## Run backend

```bash
cp backend/.env.example backend/.env
# fill in your real SMTP settings inside backend/.env
./scripts/start-backend.sh
```

The backend starts on:

- `http://localhost:8080`
- `http://10.12.251.123:8080` for devices on the same Wi-Fi

To stop it:

```bash
./scripts/stop-backend.sh
```

## Run Android app

```bash
./gradlew :app:assembleDebug
```

The Android client currently points to `http://10.12.251.123:8080/`, which works for a real Android phone on the same Wi-Fi as this Mac.

## Notes

- The backend now persists accounts, profiles, logs, friend links, verification codes, and refresh tokens to `backend/data/store.json`.
- Uploaded avatars are stored under `backend/uploads/`.
- The Android app keeps logs and the current profile in Room so users can continue logging offline before sync runs.
- SMTP settings are loaded from environment variables. The easiest path is to copy `backend/.env.example` to `backend/.env`.
