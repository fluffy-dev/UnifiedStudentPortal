# Messaging API

Covers direct messages, news, help requests, and IT orders.

---

### GET /api/messages/inbox

Get messages received by the authenticated user.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "sender": "alice",
    "senderFullName": "Alice Smith",
    "recipient": "eve",
    "subject": "Grade update",
    "body": "Your exam has been graded.",
    "urgency": "MEDIUM",
    "status": "UNREAD",
    "sentAt": "2026-05-01T09:15:00"
  }
]
```

**Example:**
```bash
curl http://localhost:8080/api/messages/inbox \
  -H "Authorization: Bearer <token>"
```

---

### GET /api/messages/sent

Get messages sent by the authenticated user.

**Role required:** Any authenticated user

**Response:** Same shape as inbox.

**Example:**
```bash
curl http://localhost:8080/api/messages/sent \
  -H "Authorization: Bearer <token>"
```

---

### POST /api/messages

Send a direct message.

**Role required:** Any authenticated user

**Request body:**
```json
{
  "recipient": "alice",
  "subject": "Question about homework",
  "body": "Could you clarify task 3?",
  "urgency": "LOW"
}
```

Valid `urgency` values: `LOW`, `MEDIUM`, `HIGH`.

**Response:**
```json
{ "message": "Message sent." }
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"recipient":"alice","subject":"Question","body":"Could you clarify task 3?","urgency":"LOW"}'
```

---

### GET /api/news

List all news posts, sorted by pin status then date (newest first).

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "title": "End of semester reminder",
    "body": "Exams start June 1st.",
    "author": "carol",
    "authorFullName": "Carol Dean",
    "publishedAt": "2026-05-10T08:00:00",
    "pinned": true,
    "comments": []
  }
]
```

---

### POST /api/news

Publish a news post.

**Role required:** Employee (Teacher, Dean, Manager, Librarian, TechSupport, EmployeeResearcher)

**Request body:**
```json
{
  "title": "Library hours update",
  "body": "The library will close early on Friday.",
  "pinned": false
}
```

**Response:**
```json
{ "message": "News published." }
```

---

### PUT /api/news/{id}/pin

Pin or unpin a news post.

**Role required:** Employee (author, or Manager/Dean for any post)

**Request body:**
```json
{ "pinned": true }
```

**Response:**
```json
{ "message": "News pinned." }
```

---

### POST /api/news/{id}/comment

Add a comment to a news post.

**Role required:** Any authenticated user

**Request body:**
```json
{ "comment": "Thanks for the update!" }
```

**Response:**
```json
{ "message": "Comment added." }
```

---

### GET /api/requests

List help requests. Regular users see their own; Managers and Deans see all.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "requester": "eve",
    "requesterFullName": "Eve Johnson",
    "title": "Transcript request",
    "type": "TRANSCRIPT",
    "faculty": "SITE",
    "urgency": "MEDIUM",
    "body": "Need a transcript for scholarship application.",
    "createdAt": "2026-05-05T11:00:00",
    "status": "PENDING"
  }
]
```

---

### POST /api/requests

Submit a new help request.

**Role required:** Any authenticated user

**Request body:**
```json
{
  "title": "Transcript request",
  "type": "TRANSCRIPT_FOR_YEAR",
  "urgency": "MEDIUM",
  "body": "Need a transcript for scholarship application."
}
```

Valid `type` values: `TRANSCRIPT_FOR_SEMESTER`, `TRANSCRIPT_FOR_YEAR`, `CERTIFICATE_OF_EDUCATION`, `ACADEMIC_MOBILITY`, `COORDINATION_OF_DIPLOMA_TOPIC`, `REQUEST_FOR_CREATING_ORGANIZATION`.

Valid `urgency` values: `LOW`, `MEDIUM`, `HIGH`.

**Response:**
```json
{ "message": "Request submitted." }
```

---

### PUT /api/requests/{id}

Process a request (approve or reject).

**Role required:** Manager or Dean

**Request body:**
```json
{ "status": "APPROVED" }
```

Valid `status` values: `PENDING`, `APPROVED`, `REJECTED`, `NOT_APPROVED`.

**Response:**
```json
{ "message": "Request updated." }
```

---

### GET /api/orders

List IT orders. Regular users see their own; TechSupport sees all.

**Role required:** Any authenticated user

**Response:**
```json
[
  {
    "id": 1,
    "requester": "bob",
    "requesterFullName": "Bob Ross",
    "description": "Need a new laptop for the lab.",
    "status": "NEW",
    "createdAt": "2026-05-12T14:00:00"
  }
]
```

---

### POST /api/orders

Submit an IT order.

**Role required:** Any authenticated user

**Request body:**
```json
{ "description": "Need a new projector for room 204." }
```

**Response:**
```json
{ "message": "Order created." }
```

---

### PUT /api/orders/{id}/accept

Accept an IT order (move to ACCEPTED state).

**Role required:** TechSupport

**Request body:** none

**Response:**
```json
{ "message": "Order accepted." }
```

---

### PUT /api/orders/{id}/complete

Mark an IT order as completed (move to DONE state).

**Role required:** TechSupport

**Request body:** none

**Response:**
```json
{ "message": "Order completed." }
```
