# API Reference Overview

## Base URL

```
http://localhost:8080
```

## Authentication

The API uses **Bearer token** authentication.

### Login

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username": "<username>", "password": "<password>"}'
```

Response:
```json
{ "token": "abc123..." }
```

Pass the token on every subsequent request:

```
Authorization: Bearer abc123...
```

### Logout

```bash
curl -X POST http://localhost:8080/api/logout \
  -H "Authorization: Bearer abc123..."
```

## Standard Error Responses

| Status | Meaning |
|--------|---------|
| `400 Bad Request` | Invalid input — message field describes the problem |
| `401 Unauthorized` | Missing or invalid token |
| `403 Forbidden` | Token valid but role insufficient for this endpoint |
| `404 Not Found` | Resource does not exist |

Error body:
```json
{ "error": "Description of the problem" }
```

## Role-Gated Endpoints

Each endpoint lists the required role. A token from a lower-privileged role returns `403`.

## Public Endpoints (no auth)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/login` | Obtain a Bearer token |

## Directory (any authenticated user)

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/users/directory` | List all users (username + role) |
| `GET` | `/api/system/messages` | Load i18n string map for the current locale |
