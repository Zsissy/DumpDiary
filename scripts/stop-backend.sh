#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PID_FILE="$ROOT_DIR/backend/backend.pid"

if [[ ! -f "$PID_FILE" ]]; then
  echo "No backend.pid found."
  exit 0
fi

BACKEND_PID="$(cat "$PID_FILE")"
if kill -0 "$BACKEND_PID" 2>/dev/null; then
  kill "$BACKEND_PID"
  echo "Stopped backend PID $BACKEND_PID"
else
  echo "Backend PID $BACKEND_PID is not running."
fi

rm -f "$PID_FILE"
