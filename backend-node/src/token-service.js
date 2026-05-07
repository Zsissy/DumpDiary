import { randomUUID } from "node:crypto";

export class TokenService {
  constructor(store) {
    this.store = store;
  }

  issueAccessToken(userId) {
    const token = `access-${randomUUID()}`;
    this.store.putAccessToken(token, userId);
    return token;
  }

  issueRefreshToken(userId) {
    const token = `refresh-${randomUUID()}`;
    this.store.putRefreshToken(token, { userId, token });
    return token;
  }

  resolveAccessToken(token) {
    return this.store.resolveAccessToken(token);
  }

  refresh(refreshToken) {
    const record = this.store.getRefreshToken(refreshToken);
    return record ? this.issueAccessToken(record.userId) : null;
  }

  revoke(accessToken, refreshToken) {
    this.store.removeAccessToken(accessToken);
    this.store.removeRefreshToken(refreshToken);
  }
}
