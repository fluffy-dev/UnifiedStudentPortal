# Courses API

---

### GET /api/courses

List all courses.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": "cs101",
    "name": "Introduction to Programming",
    "credits": 5,
    "type": "MAJOR",
    "capacity": 30,
    "enrolled": 22,
    "teachers": ["alice"]
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/courses \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/courses/{id}

Get a single course by ID.

**Role required:** Any authenticated user

**Response:** Same shape as a single item from `GET /api/courses`.

**Example:**
```bash
curl http://localhost:8080/api/courses/cs101 \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/courses

Create a new course.

**Role required:** Manager

**Request body:**
```json
{
  "name": "Machine Learning",
  "credits": 6,
  "type": "MAJOR",
  "capacity": 25
}
```

Valid `type` values: `MAJOR`, `MINOR`, `FREE`.

**Response:** `201 Created` with the created course object.

**Example:**
```bash
curl -X POST http://localhost:8080/api/courses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Machine Learning","credits":6,"type":"MAJOR","capacity":25}'
```

---

### POST /api/courses/{id}/enroll

Enroll the authenticated student in a course.

**Role required:** Student

**Request body:** none

**Response:**
```json
{ "message": "Enrolled successfully." }
```

Returns `400` with a rejection reason if any enrollment rule fails.

**Example:**
```bash
curl -X POST http://localhost:8080/api/courses/cs101/enroll \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/courses/{id}/drop

Drop the authenticated student from a course.

**Role required:** Student

**Request body:** none

**Response:**
```json
{ "message": "Dropped successfully." }
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/courses/cs101/drop \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/courses/{id}/marks

Record marks for a student in a course.

**Role required:** Teacher

**Request body:**
```json
{
  "studentUsername": "eve",
  "firstHalf": 25,
  "secondHalf": 28,
  "exam": 35
}
```

- `firstHalf`: 0–30
- `secondHalf`: 0–30
- `exam`: 0–40 (student must have ≥ 30 combined attestation to be admitted)

**Response:**
```json
{ "message": "Marks recorded." }
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/courses/cs101/marks \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"studentUsername":"eve","firstHalf":25,"secondHalf":28,"exam":35}'
```

---

### GET /api/courses/{id}/grades

View all grades for a course. Only accessible to teachers assigned to that course.

**Role required:** Teacher (of that course)

**Response:**
```json
[
  { "student": "eve", "firstHalf": 25, "secondHalf": 28, "exam": 35, "total": 88, "letter": "A" }
]
```

**Example:**
```bash
curl http://localhost:8080/api/courses/cs101/grades \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/transcript

Get the authenticated student's transcript.

**Role required:** Student

**Response:**
```json
{
  "student": "Eve Johnson",
  "degree": "BACHELOR",
  "year": 2,
  "failCount": 0,
  "gpa": 3.76,
  "courses": [
    { "course": "Algorithms", "letter": "A", "total": 91 }
  ]
}
```

**Example:**
```bash
curl http://localhost:8080/api/transcript \
  -H "Authorization: Bearer <token>"
```
