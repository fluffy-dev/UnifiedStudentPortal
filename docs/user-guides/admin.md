# Admin Guide

## Overview

**Admin** has system-wide oversight. The Admin manages user accounts, monitors system activity
through audit logs, and generates the academic performance report. The Admin does **not**
have academic responsibilities (no course management, no grade recording) — that belongs
to Managers and Teachers respectively.

---

## Demo Credentials

| Username | Password | Name |
|---|---|---|
| `rauan` | rauan | Rauan Assetov, System Admin |

---

## Sidebar Navigation

- **Administration**: Users, Audit Logs, Report
- **Communication**: Messages, News, IT Orders, Requests
- 🔔 Notifications bell

Admin-specific pages (`/admin/users`, `/admin/logs`, `/admin/report`) are **route-guarded** —
non-Admin users who navigate to these URLs directly are redirected to the dashboard.

---

## What an Admin Can Do

### User Management (`/admin/users`)

**View all users**
Complete list of every user in the system with username, full name, email, faculty, and role badge.
Use the search box to filter by username or role.

**Create a student account**
Click **＋ Create Student**. Fill in:

| Field | Required | Notes |
|---|---|---|
| Username | ✅ | Must be unique |
| Password | ✅ | Stored as hash |
| First Name | ✅ | |
| Last Name | | |
| Email | ✅ | |
| Faculty | ✅ | SITE, SEOGI, SG, KMA, ISE, BS |
| Study Year | ✅ | 1–6 |
| Degree Type | ✅ | BACHELOR · MASTER · DOCTORATE |
| Gender | | Defaults to MALE if blank |
| Date of Birth | | ISO-8601 format (YYYY-MM-DD), defaults to 2000-01-01 |

!!! info "Only students can be created via the UI"
    Teacher, Manager, Dean, Librarian, TechSupport, and Admin accounts are currently
    only creatable by editing the `data/users.json` file directly or seeding.
    This is a known limitation — contact the developer to extend the admin creation UI.

**Delete a user**
Click the **Delete** button on any user row. A confirmation dialog appears.

!!! warning "No cascade"
    Deleting a user does not remove their messages, course enrollments, grades, or
    research papers. Phantom usernames may appear in those records. Avoid deleting
    active users mid-semester.

---

### Audit Logs (`/admin/logs`)

Every significant action in the system is logged with:

| Column | Description |
|---|---|
| Time | ISO timestamp of the action |
| Actor | Username who performed the action |
| Action | Free-text description (e.g. "Recorded marks 82/B for dariya in Discrete Mathematics") |

Logs are append-only and never deleted. Use this to investigate grade changes, enrollment
anomalies, or suspicious activity.

Examples of logged actions:

- Student enrolled in / dropped a course
- Teacher recorded marks for a student
- Manager created a course / assigned a teacher
- Paper published
- Request approved or rejected
- User created or deleted

---

### Academic Report (`/admin/report`)

Generates a real-time snapshot of the university's academic standing.

**Summary stats** (top row):

| Stat | Meaning |
|---|---|
| Students | Total student accounts |
| Courses | Total active courses |
| Avg GPA | System-wide average GPA |
| At-Risk Students | Students with fail count > 0 |

**Top Students by GPA**
Table of students with the highest cumulative GPA, showing full name and GPA value.

**Course Performance table**
Per-course breakdown:

| Column | Description |
|---|---|
| Course | Course name |
| Enrolled / Capacity | Current enrollment vs max seats |
| Avg Score | Mean total score across all graded students |
| Passing | Number of students with a passing grade |

---

### Messages, News, Requests, IT Orders

Admin has full read/write access to messaging:

- Compose messages to any user
- View inbox and sent messages
- Read and comment on news (cannot publish — Admin is not an Employee subclass)
- View own submitted requests; submit new requests
- Submit IT orders, track own order status

!!! note "Admin cannot publish news"
    `isEmployee` in the frontend checks `!["Student","GraduateStudent","Admin"].includes(role)`.
    Admin is explicitly excluded from news publishing — it's a purely administrative account.

---

## What an Admin CANNOT Do

- ❌ Publish news or pin posts
- ❌ Record grades or manage courses
- ❌ Process student requests (Manager/Dean only)
- ❌ Accept/complete IT orders (TechSupport only)
- ❌ Add/remove library books (Librarian only)
- ❌ Create Teacher/Manager/Dean/staff accounts via the UI (only Student creation is supported)

---

## API Endpoints (Admin-exclusive)

| Method | Endpoint | What it does |
|---|---|---|
| GET | `/api/users` | List all users |
| GET | `/api/users/{username}` | Get user detail |
| POST | `/api/users/students` | Create a student |
| DELETE | `/api/users/{username}` | Delete a user |
| GET | `/api/logs` | View audit log |
| GET | `/api/reports/academic` | Academic performance report |

---

## Typical Workflows

### Onboarding new students at semester start

1. **Users** → **＋ Create Student**
2. Fill in all required fields
3. Distribute credentials to the student
4. Student can immediately log in and enroll in courses

### Investigating a grade dispute

1. **Audit Logs**
2. Search or scroll for the student's username and "marks" keyword
3. Find the specific grade-recording events with timestamps and teacher username
4. Cross-reference with the Gradebook if needed

### Monitoring end-of-semester performance

1. **Report**
2. Review At-Risk Students count
3. Check Course Performance table for courses with low average scores or poor passing rates
4. Share findings with the Dean or relevant teachers
