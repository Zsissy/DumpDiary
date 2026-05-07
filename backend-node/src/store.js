import fs from "node:fs";
import path from "node:path";

export class Store {
  constructor(storeFilePath) {
    this.storeFilePath = storeFilePath;
    this.state = {
      accountsByEmail: {},
      profilesByUserId: {},
      verificationCodes: {},
      refreshTokens: {},
      accessTokens: {},
      logsByUserId: {},
      friendsByUserId: {}
    };
    this.load();
  }

  load() {
    if (!fs.existsSync(this.storeFilePath)) return;
    const raw = fs.readFileSync(this.storeFilePath, "utf-8");
    if (!raw.trim()) return;
    const parsed = JSON.parse(raw);
    this.state = {
      accountsByEmail: parsed.accountsByEmail ?? {},
      profilesByUserId: parsed.profilesByUserId ?? {},
      verificationCodes: parsed.verificationCodes ?? {},
      refreshTokens: parsed.refreshTokens ?? {},
      accessTokens: parsed.accessTokens ?? {},
      logsByUserId: parsed.logsByUserId ?? {},
      friendsByUserId: parsed.friendsByUserId ?? {}
    };
  }

  persist() {
    fs.mkdirSync(path.dirname(this.storeFilePath), { recursive: true });
    const tempFile = `${this.storeFilePath}.tmp`;
    fs.writeFileSync(tempFile, JSON.stringify(this.state, null, 2));
    fs.renameSync(tempFile, this.storeFilePath);
  }

  containsAccount(email) {
    return Boolean(this.state.accountsByEmail[email]);
  }

  getAccount(email) {
    return this.state.accountsByEmail[email] ?? null;
  }

  putAccount(email, account) {
    this.state.accountsByEmail[email] = account;
    this.persist();
  }

  findAccountByUserId(userId) {
    return Object.values(this.state.accountsByEmail).find((account) => account.userId === userId) ?? null;
  }

  getProfile(userId) {
    return this.state.profilesByUserId[userId] ?? null;
  }

  putProfile(userId, profile) {
    this.state.profilesByUserId[userId] = profile;
    this.persist();
  }

  putVerificationCode(key, record) {
    this.state.verificationCodes[key] = record;
    this.persist();
  }

  getVerificationCode(key) {
    return this.state.verificationCodes[key] ?? null;
  }

  removeVerificationCode(key) {
    delete this.state.verificationCodes[key];
    this.persist();
  }

  putAccessToken(token, userId) {
    this.state.accessTokens[token] = userId;
    this.persist();
  }

  resolveAccessToken(token) {
    return this.state.accessTokens[token] ?? null;
  }

  removeAccessToken(token) {
    if (!token) return;
    delete this.state.accessTokens[token];
    this.persist();
  }

  putRefreshToken(token, record) {
    this.state.refreshTokens[token] = record;
    this.persist();
  }

  getRefreshToken(token) {
    return this.state.refreshTokens[token] ?? null;
  }

  removeRefreshToken(token) {
    if (!token) return;
    delete this.state.refreshTokens[token];
    this.persist();
  }

  userLogs(userId) {
    if (!this.state.logsByUserId[userId]) {
      this.state.logsByUserId[userId] = {};
    }
    return this.state.logsByUserId[userId];
  }

  upsertLog(userId, log) {
    this.userLogs(userId)[log.id] = log;
    this.persist();
  }

  getLog(userId, logId) {
    return this.userLogs(userId)[logId] ?? null;
  }

  userFriends(userId) {
    if (!this.state.friendsByUserId[userId]) {
      this.state.friendsByUserId[userId] = [];
    }
    return this.state.friendsByUserId[userId];
  }

  addFriendship(userId, friendUserId) {
    if (userId === friendUserId) {
      throw new Error("You cannot add yourself.");
    }
    const currentFriends = new Set(this.userFriends(userId));
    currentFriends.add(friendUserId);
    this.state.friendsByUserId[userId] = Array.from(currentFriends);

    const reciprocalFriends = new Set(this.userFriends(friendUserId));
    reciprocalFriends.add(userId);
    this.state.friendsByUserId[friendUserId] = Array.from(reciprocalFriends);
    this.persist();
  }

  resolveUserIdByEmail(email) {
    return this.state.accountsByEmail[email]?.userId ?? null;
  }

  visibleLogs(userId) {
    const visibleUserIds = Array.from(new Set([userId, ...this.userFriends(userId)]));
    return visibleUserIds.flatMap((visibleUserId) => Object.values(this.userLogs(visibleUserId)));
  }

  visibleFriendProfiles(userId) {
    return this.userFriends(userId)
      .map((friendUserId) => {
        const account = this.findAccountByUserId(friendUserId);
        const profile = this.getProfile(friendUserId);
        if (!account || !profile) return null;
        return {
          userId: friendUserId,
          email: account.email,
          displayName: profile.displayName,
          avatarUrl: profile.avatarUrl ?? null
        };
      })
      .filter(Boolean)
      .sort((a, b) => a.displayName.toLowerCase().localeCompare(b.displayName.toLowerCase()));
  }
}
