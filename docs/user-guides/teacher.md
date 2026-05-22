# Teacher Guide

## Overview

**Teachers** are the core academic staff. They are assigned to courses by Managers, record
student marks, publish news and announcements, and participate in the research system.
A Teacher can also be a Researcher — both sets of capabilities are available simultaneously.

---

## Demo Credentials

| Username | Password | Name | Notes |
|---|---|---|---|
| `zhomart` | zhomart | Zhomart Aldamuratov, PhD, Professor | Teaches Discrete Math, OOP, Linear Algebra, DB Systems, AI/ML, Calculus, Data Mining. Active Researcher. |
| `anel` | anel | Anel Yeraliyeva, MSc, Senior Lector | Teaches OOP, Algorithms, Technical English, Web Dev, Mobile Dev, PM, Ethics |
| `marat` | marat | Marat Ospanov, PhD, Professor | Teaches Computer Networks, SE, OS, Stats, Cybersecurity, Cloud, Computer Architecture |

---

## Sidebar Navigation

- **Academics**: Courses, **Gradebook**, Schedule
- **Library**: Library
- **Research**: Research (if researcher)
- **Communication**: Messages, News, Requests, IT Orders
- 🔔 Notifications bell

---

## What a Teacher Can Do

### Gradebook

The Gradebook is exclusive to Teachers (and visible to Dean via direct URL, though
grades are Teacher-only at the API level).

**Browse assigned courses**
The left panel lists all courses you teach. Search by name or ID.
Each entry shows enrolled student count.

**View student grades**
Click any course to load the grade table:

| Column | Description |
|---|---|
| Student | Full name + @username |
| 1st | Attestation 1 (0–30) |
| 2nd | Attestation 2 (0–30) |
| Exam | Exam score (0–40); `—` badge if not admitted to exam |
| Total | Sum (0–100) |
| Grade | Letter badge: A · B · C · D · FX · F |
| Status | Passing / **Retake exam** (FX) / **Retake course** (F<10) / Failing |

**Record marks**
Click **＋ Record Marks**. The form opens with a fresh state each time (no stale values).

1. Select student from the searchable picker (shows only enrolled students for this course)
2. Enter attestation 1 (0–30)
3. Enter attestation 2 (0–30)
4. Enter exam score (0–40)
5. Click **Save**

Validation enforces valid ranges — out-of-range values return a 400 error with a clear message.
Student must be selected before saving.

**Grade calculation (automatic)**

```
attestationTotal = att1 + att2

admitted = attestationTotal >= 30

if !admitted:           → F  (fail — not admitted to exam)
if total >= 90:         → A  (passing)
if total >= 80:         → B  (passing)
if total >= 70:         → C  (passing)
if total >= 50:         → D  (passing)
if exam < 10:           → F  (fail — absolute fail, must retake course)
if exam in [10..19]:    → FX (conditional fail — may retake exam, does NOT count as fail)
otherwise:              → F  (fail — poor exam performance)
```

**Re-recording is idempotent**
If you submit marks for a student who already has a grade (to correct an error),
the system reverses the previous side effect before applying the new one.
Fail counts are never double-counted.

---

### Schedule

Your weekly timetable — all courses you teach, lessons as a color-coded grid.
Sticky time column. Room shown per slot.

---

### Courses

Browse all courses. No enrollment buttons shown (teachers don't enroll).
Course schedule links work if the course has lessons.

---

### Library

- Browse all books
- Borrow available books (up to 3)
- Return own borrowed books

---

### Messages

- View inbox + sent tabs with full message body on click
- Compose to any user with subject, body, urgency
- Search by subject, sender, or body

---

### News

- **Read and comment** on all posts
- **Publish** — Teachers can create news posts (no pin checkbox — Managers/Deans only)
- Teachers who authored a post can pin it from the detail modal

---

### Requests

- Submit own requests (any type)
- View own submitted requests
- Cannot approve/reject

---

### IT Orders

- Submit IT orders
- Track own order status (TechSupport processes them)

---

### Research (optional)

Teachers implement `ResearcherCapable`. Activate once via the Research page.
See [Researcher Guide](researcher.md) for publishing papers, creating projects,
subscribing to journals, and receiving notifications.

---

## What a Teacher CANNOT Do

- ❌ Create/modify courses or assign teachers (Manager only)
- ❌ Approve/reject student requests (Manager/Dean only)
- ❌ View grades for courses they're not assigned to
- ❌ Add/remove library books (Librarian only)
- ❌ Create/delete user accounts (Admin only)
- ❌ Accept/complete IT orders (TechSupport only)
- ❌ View audit logs or generate reports (Admin only)

---

## API Endpoints (Teacher-specific, beyond User)

| Method | Endpoint | What it does |
|---|---|---|
| GET | `/api/courses/{id}/grades` | View all grades for an assigned course |
| POST | `/api/courses/{id}/marks` | Record marks `{studentUsername, firstHalf, secondHalf, exam}` |
| POST | `/api/news` | Publish a news post |
| PUT | `/api/news/{id}/pin` | Pin/unpin own post |
