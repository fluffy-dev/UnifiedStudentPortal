# Library API

---

### GET /api/books

List all books in the library catalogue.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "title": "Clean Code",
    "author": "Robert Martin",
    "availableCopies": 2,
    "totalCopies": 3
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/books \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/books

Add a new book to the catalogue.

**Role required:** Librarian

**Request body:**
```json
{
  "title": "Clean Code",
  "author": "Robert Martin",
  "copies": 3
}
```

**Response:** `201 Created` with the created book object.

**Example:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert Martin","copies":3}'
```

---

### DELETE /api/books/{title}

Remove a book from the catalogue by title.

**Role required:** Librarian

**Response:**
```json
{ "message": "Book removed." }
```

**Example:**
```bash
curl -X DELETE "http://localhost:8080/api/books/Clean%20Code" \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/books/{title}/borrow

Borrow a book.

**Role required:** Any authenticated user

**Request body:** none

**Response:**
```json
{ "message": "Book borrowed." }
```

Returns `400` if no copies are available.

**Example:**
```bash
curl -X POST "http://localhost:8080/api/books/Clean%20Code/borrow" \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/books/{title}/return

Return a borrowed book.

**Role required:** Any authenticated user

**Request body:** none

**Response:**
```json
{ "message": "Book returned." }
```

**Example:**
```bash
curl -X POST "http://localhost:8080/api/books/Clean%20Code/return" \
  -H "Authorization: Bearer <token>"
```
