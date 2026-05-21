# Users API

All endpoints in this section require **Admin** role unless noted.

---

### GET /api/users

List all user accounts.

**Role required:** Admin

**Response:**
```json
[
  {
    "username": "alice",
    "firstName": "Alice",
    "lastName": "Smith",
    "role": "Teacher",
    "faculty": "COMPUTER_SCIENCE"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/users \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/users/{username}

Get a single user by username.

**Role required:** Admin

**Response:**
```json
{
  "username": "eve",
  "firstName": "Eve",
  "lastName": "Johnson",
  "role": "Student",
  "faculty": "COMPUTER_SCIENCE",
  "degreeType": "BACHELOR",
  "studyYear": 2
}
```

**Example:**
```bash
curl http://localhost:8080/api/users/eve \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/users/students

Create a new student account.

**Role required:** Admin

**Request body:**
```json
{
  "username": "newstudent",
  "password": "secret123",
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane@university.edu",
  "faculty": "COMPUTER_SCIENCE",
  "degreeType": "BACHELOR",
  "studyYear": 1
}
```

Valid `faculty` values: `COMPUTER_SCIENCE`, `MATH`, `PHYSICS`, `CHEMISTRY`, `BIOLOGY`, `HISTORY`, `ECONOMICS`.

Valid `degreeType` values: `BACHELOR`, `MASTER`.

**Response:** `201 Created` with the created user object.

**Example:**
```bash
curl -X POST http://localhost:8080/api/users/students \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"username":"newstudent","password":"secret123","firstName":"Jane","lastName":"Doe","email":"jane@uni.edu","faculty":"COMPUTER_SCIENCE","degreeType":"BACHELOR","studyYear":1}'
```

---

### DELETE /api/users/{username}

Delete a user account permanently.

**Role required:** Admin

**Response:**
```json
{ "message": "User deleted." }
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/users/newstudent \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/logs

Retrieve the full audit log.

**Role required:** Admin

**Response:**
```json
[
  {
    "at": "2026-05-01T10:23:44",
    "actor": "admin",
    "action": "Created student newstudent"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/logs \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/reports/academic

Generate an academic performance report.

**Role required:** Admin

**Response:**
```json
{
  "totalCourses": 12,
  "totalStudents": 45,
  "totalTeachers": 8,
  "averageGpa": 2.87,
  "failingStudents": 3,
  "courseRows": [
    { "course": "Algorithms", "enrolled": 30, "capacity": 35, "avgScore": 72.4, "passing": 27 }
  ],
  "topStudents": [
    { "username": "eve", "fullName": "Eve Johnson", "gpa": 3.9 }
  ]
}
```

**Example:**
```bash
curl http://localhost:8080/api/reports/academic \
  -H "Authorization: Bearer <token>"
```
