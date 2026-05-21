# Research API

---

### POST /api/research/become

Activate researcher capabilities for the authenticated user.

**Role required:** Any authenticated user

**Request body:**
```json
{ "field": "Machine Learning" }
```

**Response:**
```json
{ "message": "You are now a researcher in Machine Learning." }
```

---

### GET /api/papers

List all published research papers.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "title": "Deep Learning for NLP",
    "author": "alice",
    "authorFullName": "Alice Walker",
    "journal": "IEEE Transactions",
    "pages": 12,
    "citations": 3,
    "doi": "10.1234/example",
    "publishedDate": "2026-03-15"
  }
]
```

---

### POST /api/papers

Publish a research paper.

**Role required:** Any authenticated user (researcher capabilities must be active)

**Request body:**
```json
{
  "title": "Deep Learning for NLP",
  "journal": "IEEE Transactions",
  "abstract": "This paper explores...",
  "pages": 12,
  "doi": "10.1234/example"
}
```

`doi` is optional.

**Response:**
```json
{ "message": "Paper published." }
```

---

### GET /api/papers/{id}/cite

Generate a citation for a paper.

**Role required:** Any authenticated user

**Format header (optional):** `X-Citation-Format: PLAIN_TEXT` or `X-Citation-Format: BIBTEX`. Defaults to `PLAIN_TEXT`.

**Example:**
```bash
curl http://localhost:8080/api/papers/1/cite \
  -H "Authorization: Bearer <token>" \
  -H "X-Citation-Format: BIBTEX"
```

**Response:**
```json
{ "citation": "Walker, A. (2026). Deep Learning for NLP. IEEE Transactions." }
```

---

### GET /api/projects

List all research projects.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "topic": "Quantum Computing Applications",
    "journal": "Nature",
    "supervisor": "alice",
    "supervisorFullName": "Alice Walker",
    "participants": ["alice", "grace"]
  }
]
```

---

### POST /api/projects

Create a research project.

**Role required:** Any authenticated user

**Request body:**
```json
{
  "topic": "Quantum Computing Applications",
  "journal": "Nature"
}
```

**Response:**
```json
{ "message": "Project created." }
```

---

### POST /api/projects/{journal}/join

Join a research project by its journal.

**Role required:** Any authenticated user

**Request body:** none

**Response:**
```json
{ "message": "Joined project." }
```

---

### POST /api/subscriptions

Subscribe to a journal.

**Role required:** Any authenticated user

**Request body:**
```json
{ "journal": "Nature" }
```

**Response:**
```json
{ "message": "Subscribed to Nature." }
```

---

### DELETE /api/subscriptions/{journal}

Unsubscribe from a journal.

**Role required:** Any authenticated user

**Request body:** none

**Response:**
```json
{ "message": "Unsubscribed from Nature." }
```
