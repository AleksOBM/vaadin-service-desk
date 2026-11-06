#!/bin/sh
set -e

# Определяем папку проекта (откуда запущен скрипт)
PROJECT_DIR="$(pwd)"
SECRETS_DIR="${PROJECT_DIR}/.secrets"

CERT_FILE="${SECRETS_DIR}/server.crt"
KEY_FILE="${SECRETS_DIR}/server.key"

# Если файлы уже есть — ничего не делаем (если нужно перезаписать, удалите папку .secrets)
if [ -f "$CERT_FILE" ] && [ -f "$KEY_FILE" ]; then
    echo "Сертификаты уже существуют в ${SECRETS_DIR}, пропускаем генерацию."
    exit 0
fi

echo "Генерация сертификатов в ${SECRETS_DIR}..."
mkdir -p "$SECRETS_DIR"

openssl req -x509 -newkey rsa:4096 -nodes \
    -keyout "$KEY_FILE" \
    -out "$CERT_FILE" \
    -days 3650 \
    -subj "/CN=localhost" \
    -addext "subjectAltName=DNS:localhost,DNS:backend,IP:127.0.0.1,IP:::1"

# Права доступа: ключ должен читать только владелец
chmod 600 "$KEY_FILE"

echo "Готово!"
echo "Сертификат: ${CERT_FILE}"
echo "Ключ:       ${KEY_FILE}"