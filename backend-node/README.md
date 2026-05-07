# DumpDiary Node Backend

Node.js version of the DumpDiary backend, designed for Docker-based deployment on a server or NAS.

## Stack

- Node.js 20
- Express
- bcryptjs
- nodemailer
- JSON file persistence

## Features

- Email/password register/login
- Email verification code send/verify/reset password
- Profile nickname update and avatar upload
- Friend sharing
- Log CRUD
- Monthly/streak/yearly stats
- Static file serving for `uploads/` and `downloads/`

## Local run

```bash
cd backend-node
cp .env.example .env
npm install
npm start
```

## Docker run

```bash
cd backend-node
cp .env.example .env
docker compose up -d --build
```

## Mounted directories

- `./data` -> persisted JSON store
- `./uploads` -> avatar files
- `./downloads` -> APK files for in-app update

## For FeiNiao NAS

Use the `docker-compose.yml` in this folder directly:

1. Upload the `backend-node/` folder to the NAS
2. Fill `.env`
3. Put your release APK into `downloads/`
4. Start with Docker Compose in the NAS container UI or terminal

Recommended reverse proxy:

- `https://your-domain.com` -> `http://nas-ip:8080`

Then point the Android app to:

```text
https://your-domain.com/
```
