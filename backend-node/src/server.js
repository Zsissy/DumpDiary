import "dotenv/config";
import cors from "cors";
import express from "express";
import morgan from "morgan";
import multer from "multer";
import bcrypt from "bcryptjs";
import fs from "node:fs";
import path from "node:path";
import { randomInt, randomUUID } from "node:crypto";
import { fileURLToPath } from "node:url";
import { Store } from "./store.js";
import { TokenService } from "./token-service.js";
import { EmailSender } from "./email-sender.js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const app = express();

const port = Number(process.env.PORT ?? "8080");
const dataDir = process.env.DATA_DIR ?? path.resolve(__dirname, "../data");
const uploadDir = process.env.UPLOAD_DIR ?? path.resolve(__dirname, "../uploads");
const downloadDir = process.env.DOWNLOAD_DIR ?? path.resolve(__dirname, "../downloads");
const storeFile = path.join(dataDir, "store.json");
const store = new Store(storeFile);
const tokenService = new TokenService(store);
const emailSender = new EmailSender();

const latestVersionCode = Number(process.env.DUMPDIARY_LATEST_VERSION_CODE ?? "2");
const latestVersionName = process.env.DUMPDIARY_LATEST_VERSION_NAME || "1.1";
const latestVersionNotes = process.env.DUMPDIARY_LATEST_VERSION_NOTES || "";
const latestDownloadPath = process.env.DUMPDIARY_DOWNLOAD_PATH || `/downloads/dumpdiary-${latestVersionName}.apk`;

fs.mkdirSync(dataDir, { recursive: true });
fs.mkdirSync(uploadDir, { recursive: true });
fs.mkdirSync(downloadDir, { recursive: true });

const upload = multer({
  storage: multer.diskStorage({
    destination: (_req, _file, cb) => cb(null, uploadDir),
    filename: (_req, file, cb) => {
      const safeOriginalName = path.basename(file.originalname || "avatar.jpg").replace(/[^\w.-]/g, "_");
      cb(null, `${randomUUID()}-${safeOriginalName}`);
    }
  }),
  limits: { fileSize: 10 * 1024 * 1024 },
  fileFilter: (_req, file, cb) => {
    if (file.mimetype.startsWith("image/")) {
      cb(null, true);
    } else {
      cb(new Error("Only image files are allowed."));
    }
  }
});

app.use(cors());
app.use(express.json({ limit: "2mb" }));
app.use(morgan("combined"));
app.use("/uploads", express.static(uploadDir));
app.use("/downloads", express.static(downloadDir));

class HttpError extends Error {
  constructor(status, message) {
    super(message);
    this.status = status;
  }
}

function badRequest(message) {
  throw new HttpError(400, message);
}

function conflict(message) {
  throw new HttpError(409, message);
}

function unauthorized(message) {
  throw new HttpError(401, message);
}

function notFound(message) {
  throw new HttpError(404, message);
}

function nowIso() {
  return new Date().toISOString();
}

function verificationKey(purpose, email) {
  return `${purpose}:${email}`;
}

function requireBody(value, message = "Body is required.") {
  if (!value || typeof value !== "object") badRequest(message);
  return value;
}

function getAuthUserId(req) {
  const header = req.headers.authorization ?? "";
  const token = header.startsWith("Bearer ") ? header.slice(7).trim() : "";
  if (!token) unauthorized("Authorization is required.");
  const userId = tokenService.resolveAccessToken(token);
  if (!userId) unauthorized("Access token is invalid.");
  return userId;
}

function verifyCodeInternal(email, code, purpose) {
  const key = verificationKey(purpose, email);
  const record = store.getVerificationCode(key);
  if (!record) return false;
  const valid = record.code === code && record.expiresAtMillis >= Date.now();
  if (valid) {
    store.removeVerificationCode(key);
  }
  return valid;
}

function createSession(account, profile) {
  return {
    accessToken: tokenService.issueAccessToken(account.userId),
    refreshToken: tokenService.issueRefreshToken(account.userId),
    userId: account.userId,
    email: account.email,
    profile
  };
}

function calculateStreak(logs) {
  const sortedDates = Array.from(new Set(logs.filter((log) => !log.isDeleted).map((log) => log.dateKey))).sort();
  if (sortedDates.length === 0) {
    return { currentStreakDays: 0, maxStreakDays: 0 };
  }

  let maxStreak = 1;
  let running = 1;
  for (let index = 1; index < sortedDates.length; index += 1) {
    const previous = new Date(`${sortedDates[index - 1]}T00:00:00Z`);
    const current = new Date(`${sortedDates[index]}T00:00:00Z`);
    const diffDays = (current.getTime() - previous.getTime()) / (24 * 60 * 60 * 1000);
    running = diffDays === 1 ? running + 1 : 1;
    maxStreak = Math.max(maxStreak, running);
  }

  let endingStreak = 1;
  for (let index = sortedDates.length - 1; index > 0; index -= 1) {
    const previous = new Date(`${sortedDates[index - 1]}T00:00:00Z`);
    const current = new Date(`${sortedDates[index]}T00:00:00Z`);
    const diffDays = (current.getTime() - previous.getTime()) / (24 * 60 * 60 * 1000);
    if (diffDays === 1) {
      endingStreak += 1;
    } else {
      break;
    }
  }

  const today = new Date();
  const todayKey = today.toISOString().slice(0, 10);
  const yesterdayKey = new Date(today.getTime() - 24 * 60 * 60 * 1000).toISOString().slice(0, 10);
  const lastDateKey = sortedDates[sortedDates.length - 1];
  return {
    currentStreakDays: lastDateKey === todayKey || lastDateKey === yesterdayKey ? endingStreak : 0,
    maxStreakDays: maxStreak
  };
}

function asyncRoute(handler) {
  return (req, res, next) => Promise.resolve(handler(req, res, next)).catch(next);
}

app.get("/", (_req, res) => {
  res.json({ service: "DumpDiary backend (Node.js)", status: "ok" });
});

app.get("/app/version", (_req, res) => {
  res.json({
    versionCode: latestVersionCode,
    versionName: latestVersionName,
    downloadPath: latestDownloadPath,
    notes: latestVersionNotes
  });
});

app.post("/auth/register", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  const email = String(body.email || "").trim();
  const password = String(body.password || "");
  const code = String(body.code || "").trim();

  if (!email) badRequest("Email is required.");
  if (password.length < 8) badRequest("Password must be at least 8 characters.");
  if (!verifyCodeInternal(email, code, "REGISTER")) badRequest("Verification code is invalid or expired.");
  if (store.containsAccount(email)) conflict("Email already registered.");

  const now = nowIso();
  const userId = randomUUID();
  const passwordHash = await bcrypt.hash(password, 12);
  const account = {
    userId,
    email,
    passwordHash,
    emailVerified: true,
    status: "ACTIVE",
    createdAt: now,
    updatedAt: now
  };
  const profile = {
    userId,
    displayName: email.split("@")[0],
    avatarUrl: null,
    updatedAt: now
  };

  store.putAccount(email, account);
  store.putProfile(userId, profile);
  res.json(createSession(account, profile));
}));

app.post("/auth/login", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  const email = String(body.email || "").trim();
  const password = String(body.password || "");
  const account = store.getAccount(email);
  if (!account) unauthorized("Invalid email or password.");
  const verified = await bcrypt.compare(password, account.passwordHash);
  if (!verified) unauthorized("Invalid email or password.");
  const profile = store.getProfile(account.userId);
  if (!profile) notFound("Profile not found.");
  res.json(createSession(account, profile));
}));

app.post("/auth/send-email-code", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  const email = String(body.email || "").trim();
  const purpose = String(body.purpose || "").trim();

  if (!email) badRequest("Email is required.");
  if (!["REGISTER", "RESET_PASSWORD"].includes(purpose)) badRequest("Verification purpose is invalid.");
  if (purpose === "RESET_PASSWORD" && !store.containsAccount(email)) badRequest("Email is not registered.");

  const code = String(randomInt(100000, 1000000));
  store.putVerificationCode(verificationKey(purpose, email), {
    email,
    code,
    purpose,
    expiresAtMillis: Date.now() + 10 * 60 * 1000
  });

  const purposeLabel = purpose === "REGISTER" ? "registration" : "password reset";
  const result = await emailSender.sendVerificationCode(email, code, purposeLabel);
  res.json({ message: result.detail });
}));

app.post("/auth/verify-email-code", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  res.json({
    valid: verifyCodeInternal(String(body.email || "").trim(), String(body.code || "").trim(), String(body.purpose || "").trim())
  });
}));

app.post("/auth/reset-password", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  const email = String(body.email || "").trim();
  const code = String(body.code || "").trim();
  const newPassword = String(body.newPassword || "");
  const account = store.getAccount(email);

  if (!account) badRequest("Email is not registered.");
  if (!verifyCodeInternal(email, code, "RESET_PASSWORD")) badRequest("Verification code is invalid or expired.");

  store.putAccount(email, {
    ...account,
    passwordHash: await bcrypt.hash(newPassword, 12),
    updatedAt: nowIso()
  });
  res.json({ message: "Password has been reset." });
}));

app.post("/auth/refresh", asyncRoute(async (req, res) => {
  const body = requireBody(req.body);
  const refreshToken = String(body.refreshToken || "").trim();
  const accessToken = tokenService.refresh(refreshToken);
  if (!accessToken) unauthorized("Refresh token is invalid.");
  const userId = tokenService.resolveAccessToken(accessToken);
  const account = userId ? store.findAccountByUserId(userId) : null;
  const profile = userId ? store.getProfile(userId) : null;
  if (!userId || !account || !profile) unauthorized("Unable to resolve user.");
  res.json({
    accessToken,
    refreshToken,
    userId: account.userId,
    email: account.email,
    profile
  });
}));

app.post("/auth/logout", asyncRoute(async (req, res) => {
  const refreshToken = req.header("X-Refresh-Token");
  const authHeader = req.header("Authorization") ?? "";
  const accessToken = authHeader.startsWith("Bearer ") ? authHeader.slice(7).trim() : null;
  tokenService.revoke(accessToken, refreshToken);
  res.json({ message: "Logged out." });
}));

app.get("/me/profile", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const profile = store.getProfile(userId);
  if (!profile) notFound("Profile not found.");
  res.json(profile);
}));

app.put("/me/profile", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const profile = store.getProfile(userId);
  if (!profile) notFound("Profile not found.");
  const body = requireBody(req.body);
  const updated = {
    ...profile,
    displayName: String(body.displayName || ""),
    updatedAt: nowIso()
  };
  store.putProfile(userId, updated);
  res.json(updated);
}));

app.post("/me/avatar", upload.single("avatar"), asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const profile = store.getProfile(userId);
  if (!profile) notFound("Profile not found.");
  if (!req.file) badRequest("Avatar file is required.");
  const avatarUrl = `/uploads/${req.file.filename}`;
  store.putProfile(userId, {
    ...profile,
    avatarUrl,
    updatedAt: nowIso()
  });
  res.json({ avatarUrl });
}));

app.get("/friends", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  res.json(store.visibleFriendProfiles(userId));
}));

app.post("/friends", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const body = requireBody(req.body);
  const email = String(body.email || "").trim();
  if (!email) badRequest("Friend email is required.");
  const friendUserId = store.resolveUserIdByEmail(email);
  if (!friendUserId) notFound("Friend email is not registered.");
  store.addFriendship(userId, friendUserId);
  res.json({ message: "Friend connected." });
}));

app.get("/logs", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const logs = store.visibleLogs(userId).sort((a, b) => b.occurredAt.localeCompare(a.occurredAt));
  res.json(logs);
}));

app.post("/logs", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const body = requireBody(req.body);
  if (body.userId !== userId) badRequest("User mismatch.");
  store.upsertLog(userId, body);
  res.json(body);
}));

app.put("/logs/:id", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const body = requireBody(req.body);
  if (body.id !== req.params.id) badRequest("Log id mismatch.");
  if (body.userId !== userId) badRequest("User mismatch.");
  const updated = { ...body, updatedAt: nowIso() };
  store.upsertLog(userId, updated);
  res.json(store.getLog(userId, req.params.id));
}));

app.delete("/logs/:id", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const current = store.getLog(userId, req.params.id);
  if (!current) notFound("Log not found.");
  store.upsertLog(userId, {
    ...current,
    isDeleted: true,
    updatedAt: nowIso()
  });
  res.json({ message: "Deleted" });
}));

app.get("/stats/monthly", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const month = String(req.query.month || new Date().toISOString().slice(0, 7));
  const logs = store.visibleLogs(userId).filter((log) => !log.isDeleted && String(log.dateKey).startsWith(month));
  res.json({
    month,
    totalCount: logs.length,
    activeDays: new Set(logs.map((log) => log.dateKey)).size
  });
}));

app.get("/stats/streak", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  res.json(calculateStreak(store.visibleLogs(userId)));
}));

app.get("/stats/yearly", asyncRoute(async (req, res) => {
  const userId = getAuthUserId(req);
  const year = Number(req.query.year || new Date().getUTCFullYear());
  const counts = Object.fromEntries(Array.from({ length: 12 }, (_, index) => [index + 1, 0]));
  for (const log of store.visibleLogs(userId)) {
    if (log.isDeleted || !String(log.dateKey).startsWith(`${year}-`)) continue;
    const month = Number(String(log.dateKey).slice(5, 7));
    counts[month] = (counts[month] ?? 0) + 1;
  }
  res.json(Array.from({ length: 12 }, (_, index) => ({
    month: index + 1,
    count: counts[index + 1] ?? 0
  })));
}));

app.use((err, _req, res, _next) => {
  const status = err instanceof HttpError ? err.status : 500;
  const message = err instanceof HttpError ? err.message : "Internal server error";
  if (status >= 500) {
    console.error(err);
  }
  res.status(status).json({ message });
});

app.listen(port, "0.0.0.0", () => {
  console.log(`DumpDiary Node backend listening on http://0.0.0.0:${port}`);
});
