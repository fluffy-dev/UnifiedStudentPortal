# Configuration

## JVM Flags

| Flag | Default | Description |
|------|---------|-------------|
| `-Duni.data=<path>` | `./data` | Directory where JSON data files are stored and loaded |

Example — use a custom data directory:

```bash
java -Duni.data=/var/university/data -jar university-system.jar --server
```

## Server Port

Pass the port as the second argument after `--server`:

```bash
java -jar university-system.jar --server 9000
```

Default port is **8080**.

## Data Directory Layout

The system persists all state as JSON files in the data directory:

```
data/
├── users.json
├── courses.json
├── grades.json
├── messages.json
├── news.json
├── requests.json
├── orders.json
├── books.json
├── papers.json
├── projects.json
├── logs.json
└── notifications.json
```

The data directory is created automatically on first run if it does not exist.

## Frontend API Base URL

The frontend reads the API base URL from `frontend/.env`:

```
VITE_API_URL=http://localhost:8080
```

Change this to point to a different backend host or port.
