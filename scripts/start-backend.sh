#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="$ROOT_DIR/backend/.env"
PID_FILE="$ROOT_DIR/backend/backend.pid"
LOG_FILE="$ROOT_DIR/backend/backend.log"

if [[ -f "$ENV_FILE" ]]; then
  set -a
  source "$ENV_FILE"
  set +a
fi

if [[ -f "$PID_FILE" ]]; then
  EXISTING_PID="$(cat "$PID_FILE" 2>/dev/null || true)"
  if [[ -n "${EXISTING_PID:-}" ]] && kill -0 "$EXISTING_PID" 2>/dev/null; then
    echo "Backend is already running on PID $EXISTING_PID"
    echo "Log file: $LOG_FILE"
    exit 0
  fi
fi

cd "$ROOT_DIR"
nohup ./gradlew :backend:run >"$LOG_FILE" 2>&1 &
BACKEND_PID=$!
echo "$BACKEND_PID" > "$PID_FILE"

echo "Backend started."
echo "PID: $BACKEND_PID"
echo "Local: http://localhost:8080"
echo "LAN:   http://10.12.251.123:8080"
echo "Log:   $LOG_FILE"
