# Installation

## Prerequisites

- **Java 17+** — `java -version` should report 17 or higher
- **Node.js 18+** — required for the React frontend
- **Bash** — for the helper scripts

## One-Command Start

The fastest way to run everything (backend + frontend):

```bash
bash scripts/start.sh
```

This script:
1. Builds the backend JAR (`scripts/build.sh`)
2. Installs frontend dependencies (`npm install` inside `frontend/`)
3. Starts the REST API server on port 8080
4. Starts the Vite dev server on port 5173

Open [http://localhost:5173](http://localhost:5173) when both are ready.

## Manual Steps

### Build the backend

```bash
bash scripts/build.sh
```

Produces `university-system.jar` in the project root.

### Run the backend

```bash
# Interactive CLI (default)
java -jar university-system.jar

# REST API server on port 8080
java -jar university-system.jar --server

# REST API on a custom port
java -jar university-system.jar --server 9000
```

### Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend expects the REST API at `http://localhost:8080`. See [Configuration](configuration.md) to change this.

## Run the Test Suite

```bash
bash scripts/test.sh
```

Runs 52 Java unit and integration tests. No test framework required — pure Java.
