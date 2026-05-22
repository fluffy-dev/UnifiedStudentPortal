# Manager Guide

## Overview

**Managers** are academic administrators responsible for the course lifecycle. They create courses,
assign teachers, schedule lessons, set prerequisites and capacity, manage student help requests,
and publish news. They are the operational backbone of the academic system.

---

## Demo Credentials

| Username | Password | Name |
|---|---|---|
| `serdar` | serdar | Serdar Dundar, Manager |

---

## Sidebar Navigation

- **Academics**: Courses
- **Library**: Library
- **Research**: Research (if researcher)
- **Communication**: Messages, News, **Requests**, IT Orders
- 🔔 Notifications bell

---

## What a Manager Can Do

### Course Management

This is the Manager's core responsibility. All course lifecycle operations are Manager-only.

**Create a course**
Click **＋ New Course** on the Courses page. Required fields:

| Field | Constraints |
|---|---|
| Course Name | Any non-blank string |
| Credits | 1–10 |
| Capacity | Minimum 1 student |
| Type | MAJOR / MINOR / FREE |

The course is created with an auto-generated ID and zero enrolled students.

**Assign a teacher to a course**
On any course card, click **👤 Assign Teacher**. A searchable user picker shows
all Teacher-role users. Select one and click **Save**.

- A course can have multiple teachers
- Re-assigning the same teacher returns an error: "Teacher already assigned"
- Teachers only appear in their own Gradebook after being assigned

**Add a lesson to a course**
On any course card, click **＋ Add Lesson**. Fill in:

| Field | Options / Format |
|---|---|
| Lesson Type | LECTURE · PRACTICE · OFFICE_HOURS · EXAM |
| Day | MONDAY – SUNDAY |
| Time | Free text, e.g. `09:00` or `09:00–10:30` |
| Room | Any room name, e.g. `Aud 401`, `Lab 2` |

The `RoomScheduler` checks for conflicts — same room + same day + same time is rejected.
Lessons appear immediately in students' and teachers' Schedule timetable.

**Why this matters**
Without assigned teachers, the Gradebook is empty for that teacher.
Without lessons, the Schedule page shows nothing for enrolled students.

---

### Managing Requests

Managers see **all** help requests from all students (along with Deans). Students see
only their own.

**Browse and filter**
Use the status filter (PENDING / APPROVED / REJECTED) and the search box.

**Approve or reject a request**
PENDING requests show **✓ Approve** and **✗ Reject** buttons on the card and in the
detail modal. Click to process — the status updates immediately.

!!! warning "Self-approval"
    A Manager who submits their own request can also approve it. This is a known
    limitation — use discretion.

---

### News

- **Publish** news posts with title and body
- **Pin posts** — the Pin checkbox appears in the compose form for Managers and Deans
- **Pin/unpin from detail modal** — for existing posts
- Comment on any post

---

### Messages

Full messaging: compose to any user, view inbox + sent, read full body on click.

---

### Library

Browse and borrow books (up to 3). Return own borrowed books.

---

### IT Orders

Submit IT orders, track own orders. The New Order button is visible to Managers.

---

### Requests (as a submitter)

Can also *submit* their own requests of any type.

---

## What a Manager CANNOT Do

- ❌ Record student grades (Teacher only)
- ❌ View grades for courses (Teacher only)
- ❌ Create or delete user accounts (Admin only)
- ❌ View audit logs (Admin only)
- ❌ Add/remove books (Librarian only)
- ❌ Accept/complete IT orders (TechSupport only)

---

## API Endpoints (Manager-specific)

| Method | Endpoint | Body | What it does |
|---|---|---|---|
| POST | `/api/courses` | `{name, credits, type, capacity}` | Create a course |
| POST | `/api/courses/{id}/teachers` | `{teacherUsername}` | Assign teacher |
| POST | `/api/courses/{id}/lessons` | `{type, day, time, room}` | Add a lesson |
| POST | `/api/news` | `{title, body, pinned}` | Publish (pinnable) |
| PUT | `/api/news/{id}/pin` | `{pinned: true/false}` | Pin/unpin any post |
| GET | `/api/requests` | — | List ALL requests |
| PUT | `/api/requests/{id}` | `{status}` | Approve/reject |

---

## Typical Workflows

### Setting up a new course for a semester

1. **Courses** → **＋ New Course** → fill name, credits, type, capacity → Create
2. Click **👤 Assign Teacher** → select teacher(s) → Save
3. Click **＋ Add Lesson** for each weekly session:
   - Monday 09:00 Lecture in Aud 401
   - Wednesday 11:00 Practice in Lab 2
   - Friday 14:00 Office Hours in Rm 305
4. Students can now enroll; teachers see the course in their Gradebook; everyone sees it on the Schedule

### Processing a wave of scholarship transcript requests

1. **Requests** → set filter to **PENDING**
2. Review each card; click **Read more** for the full description
3. **✓ Approve** for valid requests, **✗ Reject** for incomplete ones
