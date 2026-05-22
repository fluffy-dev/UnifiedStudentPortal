# Student & Graduate Student Guide

## Overview

**Student** is the primary learner role. Students enroll in courses, track academic progress,
use the library, submit requests to the administration, and can optionally activate researcher capabilities.

**Graduate Student** extends Student with MASTER or DOCTORATE degree type. All capabilities below
apply to Graduate Students. The only difference is eligibility for graduate-specific request types
(thesis coordination, academic mobility at graduate level) and the ability to gain a supervisor
once the `SetSupervisor` feature is exposed via UI.

---

## Demo Credentials

| Username | Password | Year | Notes |
|---|---|---|---|
| `dariya` | dariya | 2 | Enrolled in Discrete Math, OOP, Linear Algebra, Technical English, Stats, Web Dev |
| `bexultan` | bexultan | 3 | Enrolled in OOP, Algorithms, DB Systems, SE, Architecture, PM; has FX and F grades |
| `assel` | assel | 1 | Enrolled in OOP, Linear Algebra, Technical English, AI/ML, Web Dev, Ethics |
| `arman` | arman | 2 | Enrolled in Discrete Math, OOP, Stats, Mob Dev, PM, Ethics, Calculus |
| `zarina` | zarina | 3 | Enrolled in Algorithms, DB Systems, SE, Technical English, PM |
| `dias` | dias | 1 | Enrolled in OOP, Linear Algebra, Technical English, Web Dev, Mob Dev, Calculus, Ethics |
| `aizat` | aizat | 2 | Enrolled in Discrete Math, Linear Algebra, Technical English, Mob Dev, Calculus |
| `alibek` | alibek | 4 | Enrolled in Algorithms, DB Systems, SE, OS, Cybersecurity, PM, Architecture |
| `madina` | madina | 1 | Enrolled in Discrete Math, Linear Algebra, Technical English, Web Dev, Calculus, Ethics |
| `yerlan` | yerlan | 2 | Enrolled in Discrete Math, Technical English, AI/ML, Stats, Computer Networks |
| `sanzhar` | sanzhar | 4 | Enrolled in Algorithms, DB Systems, SE, OS, PM, Architecture |
| `kamila` | kamila | 1 | Enrolled in OOP, Linear Algebra, Web Dev, Mob Dev, Stats, Ethics |
| `temirlan` | temirlan | 3 | Enrolled in OOP, Algorithms, SE, AI/ML, Architecture, PM |
| `nurasyl` | nurasyl | Master yr1 | Researcher; enrolled in Algorithms, DB Systems, Nets, Technical English, AI/ML, Cloud, Data Mining |
| `aigerim` | aigerim | Doctorate yr2 | Researcher; enrolled in Computer Networks, OS, Cloud, Data Mining |

---

## Sidebar Navigation

Students see the following sections and pages:

- **Academics**: Courses, Transcript, Schedule
- **Library**: Library
- **Research**: Research
- **Communication**: Messages, News, Requests, IT Orders
- 🔔 Bell icon appears when there are notifications

---

## What a Student Can Do

### Courses

**Browse all courses**
The Courses page lists every course in the system with name, ID, credits, type (Major/Minor/Free),
and remaining seats. Use the search box to filter by name or ID.

**Enroll in a course**
Click **Enroll** on any available course. Enrollment is validated server-side:

1. Have not exceeded the 3-fail limit
2. Not already enrolled in this course
3. Available semester credits not exceeded
4. All prerequisite courses completed
5. Course has seats remaining
6. No time-slot conflict with existing courses

If validation fails, a toast shows the specific reason.

**Drop a course**
Click **Drop** on an enrolled course. Credits are refunded immediately. Dropping after
grades have been recorded removes the grade entry — avoid dropping a course you've passed
unless intentional.

**View enrollment status**
Enrolled courses show a green **Enrolled** badge. Full courses show a **FULL** badge
with no Enroll button. When a course has lessons, a "View schedule" link appears.

**View course lessons**
Click "View schedule" on a course card to see a modal table with lesson type, day, time, and room.

---

### Transcript

**View academic transcript**  
Shows all currently enrolled courses with their recorded grades:

| Column | Description |
|---|---|
| Course | Course name |
| Total | Final score (0–100) |
| Grade | Letter grade: A (90–100), B (80–89), C (70–79), D (50–69), FX (exam 10–19, retake allowed), F (failed) |
| Status | Passing / Failing badge |

Header shows: student name, degree type, study year, GPA, fail count, total course count.

!!! info "FX vs F"
    **FX** means you were admitted to the exam (attestation ≥ 30) but scored 10–19 on the exam.
    You may retake the exam in the retake window without it counting as a fail.
    **F** means either you were not admitted (attestation < 30) or your exam score was < 10 —
    this counts against your 3-fail limit.

---

### Schedule

**Weekly timetable**  
Shows a color-coded grid of all enrolled courses' lessons:

- **Indigo** — Lecture
- **Green** — Practice / Lab
- **Amber** — Office Hours
- **Red** — Exam

Days without lessons are hidden. Sticky time column stays visible when scrolling horizontally.
Each cell shows: lesson type, course name, room number.

Below the grid: compact course cards with lesson-pill summaries (type · day · time).

Teachers see their *taught* courses; students see their *enrolled* courses.

---

### Library

**Browse books**  
All books visible with title, author, availability status, and current borrower.

**Borrow a book**  
Click **Borrow** on any available (not borrowed) book. Maximum 3 books at a time.

**Return a book**  
Click **Return** on a book borrowed by you. Librarians can return any book.

---

### Messages

**View inbox**  
All messages sent to you, with subject, sender (full name + @username), urgency badge,
status (UNREAD / READ), and timestamp. Click any row or "Read more" to open the full message body.

**View sent messages**  
Switch to the **Sent** tab to see messages you have sent with their recipient and status.

**Compose a message**  
Click **Compose**, pick a recipient from the searchable user picker, write subject and body,
choose urgency (Low / Medium / High). Recipient and subject are required.

**Search messages**  
The search box filters by subject, sender name, or message body.

---

### News

**Read news**  
All university news sorted with pinned posts first, then by date. Pinned posts show a 📌 badge.

**Read full post**  
Click **Read more** to open the full article and all comments in a modal.

**Add a comment**  
Click **Comment** on any post. The Post button is disabled until you type something.

**Search news**  
Filter by title, body text, or author name.

---

### Requests

**Submit a help request**  
Click **New Request** to submit a formal request to the administration. Available types:

| Type | Use case |
|---|---|
| Transcript for semester | Official semester transcript |
| Transcript for year | Official year transcript |
| Certificate of education | Enrollment confirmation |
| Academic mobility | Exchange program application |
| Coordination of diploma topic | Thesis/diploma topic approval |
| Request for creating organization | Student club registration |

Choose urgency (Low / Medium / High) and write a description in the body.

**View your requests**  
Students see only their own requests. Each card shows title, type, urgency, date, and status.
Click **Read more** to see the full description.

**Status meanings**

| Status | Meaning |
|---|---|
| PENDING | Waiting for a Manager or Dean to process |
| APPROVED | Approved — request fulfilled |
| REJECTED | Denied with or without reason |

---

### IT Orders

**Submit an IT order**  
Click **New Order**, select a device type (Laptop, Desktop, Printer, Projector, Other),
and describe the problem. Description is required.

**Track your orders**  
Students see only their own orders with status (New → Accepted → Done).

---

### Research (optional)

Research capabilities are off by default. Any user can activate them once.

**Become a Researcher**  
Navigate to **Research**, enter your research field (e.g. "Machine Learning"), and click
**Become a Researcher**. This is permanent and cannot be undone.

Once activated, see the [Researcher Guide](researcher.md) for all available actions.

---

## Notifications

When a paper is published in a journal you subscribe to, a notification is created.
The 🔔 bell in the sidebar shows the unread count. Click it to go to the Notifications page,
where you can see the text and timestamp of each notification.

Click **Mark all as read** to clear them — the bell disappears until new notifications arrive.

---

## What a Student CANNOT Do

- ❌ Create or modify courses
- ❌ Record grades
- ❌ Publish or pin news
- ❌ Process other users' requests (approve/reject)
- ❌ Accept or complete IT orders
- ❌ Add or remove books from the library
- ❌ View other students' messages
- ❌ Access audit logs or academic reports
- ❌ Create or delete user accounts
- ❌ View other students' requests (each student sees only their own)

---

## API Endpoints Available to Students

| Method | Endpoint | Action |
|---|---|---|
| GET | `/api/courses` | List all courses |
| POST | `/api/courses/{id}/enroll` | Enroll |
| POST | `/api/courses/{id}/drop` | Drop |
| GET | `/api/transcript` | View own transcript |
| GET | `/api/books` | Browse library |
| POST | `/api/books/{title}/borrow` | Borrow a book |
| POST | `/api/books/{title}/return` | Return a book |
| GET | `/api/messages/inbox` | Read inbox |
| GET | `/api/messages/sent` | Read sent |
| POST | `/api/messages` | Send a message |
| GET | `/api/news` | Read news |
| POST | `/api/news/{id}/comment` | Comment on news |
| GET | `/api/requests` | View own requests |
| POST | `/api/requests` | Submit a request |
| GET | `/api/orders` | View own orders |
| POST | `/api/orders` | Submit an IT order |
| GET | `/api/papers` | Browse research papers |
| GET | `/api/projects` | Browse research projects |
| GET | `/api/subscriptions` | View journal subscriptions |
| POST | `/api/subscriptions` | Subscribe to a journal |
| DELETE | `/api/subscriptions/{journal}` | Unsubscribe |
| POST | `/api/research/become` | Activate researcher mode |
| GET | `/api/notifications` | View notifications |
| DELETE | `/api/notifications` | Clear notifications |
| GET | `/api/users/directory` | Search users (for message recipient picker) |
