#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

echo "Building JAR locally..."
./mvnw clean package -DskipTests

echo "Starting Docker Compose..."
docker compose up --build "$@"
