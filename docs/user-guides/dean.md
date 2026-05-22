# Dean Guide

## Overview

**The Dean** is the highest academic authority in the faculty. The Dean approves and rejects
student requests alongside Managers, publishes official announcements, and can pin any news post.
The Dean extends `Teacher` internally, which means they technically have teacher capabilities
at the domain level, but the Gradebook is restricted to assigned teachers in the UI.

---

## Demo Credentials

| Username | Password | Name |
|---|---|---|
| `bayan` | bayan | Bayan Assetova, Dean, Doctor of Sciences |

---

## Sidebar Navigation

- **Academics**: Courses
- **Library**: Library
- **Research**: Research (if researcher)
- **Communication**: Messages, News, **Requests**, IT Orders
- 🔔 Notifications bell

!!! note
    The Gradebook is **not shown** in the Dean's sidebar. The backend grades endpoint
    requires Teacher role. The Dean reviews academic standing through the Requests workflow.

---

## What a Dean Can Do

### Requests — Primary Responsibility

The Dean processes **all** student requests (same as Manager). This is the Dean's most
important function in the system.

**View all requests**
All pending and historical requests from every student are visible. Use the status filter
and search to navigate.

**Approve or reject**
Any PENDING request shows **✓ Approve** and **✗ Reject** buttons. The Dean typically
handles escalated or graduate-level requests, academic integrity matters, and official
institutional letters.

---

### News

**Publish official announcements**
The Dean publishes formal faculty-wide communications — exam schedules, grade policies,
partnership announcements, scholarship information.

**Pin any post**
The Dean (like Managers) can pin or unpin any news post, including posts written by teachers.

**Comment on news**
The Dean can respond to student comments to provide official clarifications.

---

### Courses

The Dean sees all courses in the system. This is a read-only view — course management
is the Manager's responsibility. The course list is useful for verifying course availability
and teaching assignments.

---

### Messages

Full messaging capabilities:

- View inbox and sent messages with full body
- Compose to any user (students, teachers, managers, other staff)
- Formal communications to faculty and administrative staff

---

### Library

Browse and borrow books. Return own borrowed books.

---

### IT Orders

Submit IT orders (e.g. conference room AV equipment). Track own order status.

---

### Requests (as a submitter)

The Dean can also submit their own requests of any type, though this is uncommon.

---

## What a Dean CANNOT Do

- ❌ Create courses or assign teachers (Manager only)
- ❌ Record student grades via Gradebook UI (Teacher's assigned courses only)
- ❌ Create or delete user accounts (Admin only)
- ❌ View audit logs or generate system reports (Admin only)
- ❌ Add or remove library books (Librarian only)
- ❌ Accept or complete IT orders (TechSupport only)

---

## API Access

The Dean inherits from `Teacher` which inherits from `Employee` which inherits from `User`.
This means the Dean passes authentication checks for `User.class`, `Employee.class`,
and `Teacher.class` routes.

| Method | Endpoint | Dean access |
|---|---|---|
| GET | `/api/requests` | ✅ All requests |
| PUT | `/api/requests/{id}` | ✅ Approve/reject |
| POST | `/api/news` | ✅ Publish (with pin) |
| PUT | `/api/news/{id}/pin` | ✅ Pin any post |
| GET | `/api/courses/{id}/grades` | ✅ Technically accessible (Teacher inheritance), though not shown in UI |
| All User-level endpoints | — | ✅ |

---

## Typical Workflows

### Approving academic mobility request

1. **Requests** → filter by **PENDING**
2. Find the mobility request, click **Read more** to review the full description
3. Verify student eligibility (GPA, year, faculty)
4. Click **✓ Approve** or **✗ Reject**

### Publishing official semester announcement

1. **News** → **Publish**
2. Write formal title (e.g. "Final Examination Schedule — Fall 2025")
3. Add structured body: dates, rooms, appeal procedures
4. Check "Pin this post" to keep it at the top
5. Publish — immediately visible to all users
