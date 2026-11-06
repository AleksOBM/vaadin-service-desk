#!/bin/sh
set -e

CERT_DIR="/certs"
CERT_FILE="${CERT_DIR}/server.crt"
KEY_FILE="${CERT_DIR}/server.key"

if [ -f "$CERT_FILE" ] && [ -f "$KEY_FILE" ]; then
    echo "Сертификаты уже существуют, пропускаем генерацию."
    exit 0
fi

echo "Генерация сертификатов..."
mkdir -p "$CERT_DIR"
openssl req -x509 -newkey rsa:4096 -nodes \
    -keyout "$KEY_FILE" \
    -out "$CERT_FILE" \
    -days 3650 \
    -subj "/CN=localhost" \
    -addext "subjectAltName=DNS:localhost,DNS:backend,IP:127.0.0.1"

echo "Сертификаты успешно созданы."