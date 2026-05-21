# Documentation Site Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a full MkDocs (Material theme) documentation site for the University Management System, covering all audiences — graders, developers, and end users.

**Architecture:** Topic-first navigation with 5 top-level tabs (Getting Started, User Guides, Architecture, API Reference, Developer Guide). Static site built with MkDocs Material; compatible with Zensical. All content is Markdown; the two approved PNGs (`diagrams/UniversityManagementSystem.png`, `diagrams/UniversitySystemCompact.png`) are the only diagram assets used.

**Tech Stack:** Python 3, `mkdocs-material`, Markdown. No mermaid/puml rendering. Zensical-compatible output.

---

## File Map

**Create:**
- `mkdocs.yml` — site config (theme, nav, plugins, extensions)
- `docs/index.md` — home page
- `docs/getting-started/installation.md`
- `docs/getting-started/configuration.md`
- `docs/getting-started/demo-accounts.md`
- `docs/user-guides/student.md`
- `docs/user-guides/teacher.md`
- `docs/user-guides/dean.md`
- `docs/user-guides/admin.md`
- `docs/user-guides/manager.md`
- `docs/user-guides/librarian.md`
- `docs/user-guides/tech-support.md`
- `docs/user-guides/employee-researcher.md`
- `docs/user-guides/researcher.md`
- `docs/architecture/overview.md`
- `docs/architecture/highlights.md`
- `docs/architecture/interactions.md`
- `docs/architecture/domain-model.md`
- `docs/architecture/design-patterns.md`
- `docs/architecture/persistence.md`
- `docs/architecture/frontend.md`
- `docs/api-reference/overview.md`
- `docs/api-reference/users.md`
- `docs/api-reference/courses.md`
- `docs/api-reference/messaging.md`
- `docs/api-reference/research.md`
- `docs/api-reference/library.md`
- `docs/developer-guide/running-tests.md`
- `docs/developer-guide/adding-use-cases.md`
- `docs/developer-guide/i18n.md`

---

## Task 1: Scaffold — Install, Config, Stubs, First Build

**Files:**
- Create: `mkdocs.yml`
- Create: all 29 `.md` files listed above (empty stubs)

- [ ] **Step 1: Install MkDocs Material**

```bash
pip install mkdocs-material
```

Expected output includes: `Successfully installed mkdocs-material-...`

- [ ] **Step 2: Copy diagram assets into docs**

MkDocs only serves files inside `docs/`. Copy the two approved PNGs:

```bash
mkdir -p docs/assets
cp diagrams/UniversityManagementSystem.png docs/assets/
cp diagrams/UniversitySystemCompact.png docs/assets/
```

- [ ] **Step 3: Create `mkdocs.yml` at project root**

```yaml
site_name: University Management System
site_description: A full-stack university lifecycle system built in Java 17+ with Clean Architecture
site_author: Rauan

exclude_docs: |
  PROJECT_ANALYSIS.md
  OOP_Final_Project_Review.md
  superpowers/

theme:
  name: material
  palette:
    - scheme: default
      primary: indigo
      accent: indigo
      toggle:
        icon: material/brightness-7
        name: Switch to dark mode
    - scheme: slate
      primary: indigo
      accent: indigo
      toggle:
        icon: material/brightness-4
        name: Switch to light mode
  features:
    - navigation.tabs
    - navigation.sections
    - navigation.top
    - search.suggest
    - search.highlight
    - content.code.copy

plugins:
  - search

markdown_extensions:
  - admonition
  - pymdownx.highlight
  - pymdownx.tabbed:
      alternate_style: true
  - tables
  - toc:
      permalink: true

nav:
  - Home: index.md
  - Getting Started:
      - Installation: getting-started/installation.md
      - Configuration: getting-started/configuration.md
      - Demo Accounts: getting-started/demo-accounts.md
  - User Guides:
      - Student: user-guides/student.md
      - Teacher: user-guides/teacher.md
      - Dean: user-guides/dean.md
      - Admin: user-guides/admin.md
      - Manager: user-guides/manager.md
      - Librarian: user-guides/librarian.md
      - Tech Support: user-guides/tech-support.md
      - Employee Researcher: user-guides/employee-researcher.md
      - Researcher: user-guides/researcher.md
  - Architecture:
      - Overview: architecture/overview.md
      - Highlights: architecture/highlights.md
      - Class Interactions: architecture/interactions.md
      - Domain Model: architecture/domain-model.md
      - Design Patterns: architecture/design-patterns.md
      - Persistence: architecture/persistence.md
      - Frontend: architecture/frontend.md
  - API Reference:
      - Overview: api-reference/overview.md
      - Users: api-reference/users.md
      - Courses: api-reference/courses.md
      - Messaging: api-reference/messaging.md
      - Research: api-reference/research.md
      - Library: api-reference/library.md
  - Developer Guide:
      - Running Tests: developer-guide/running-tests.md
      - Adding Use Cases: developer-guide/adding-use-cases.md
      - Internationalization: developer-guide/i18n.md
```

- [ ] **Step 4: Scaffold all stub files**

```bash
mkdir -p docs/getting-started docs/user-guides docs/architecture docs/api-reference docs/developer-guide

for f in \
  docs/index.md \
  docs/getting-started/installation.md \
  docs/getting-started/configuration.md \
  docs/getting-started/demo-accounts.md \
  docs/user-guides/student.md \
  docs/user-guides/teacher.md \
  docs/user-guides/dean.md \
  docs/user-guides/admin.md \
  docs/user-guides/manager.md \
  docs/user-guides/librarian.md \
  docs/user-guides/tech-support.md \
  docs/user-guides/employee-researcher.md \
  docs/user-guides/researcher.md \
  docs/architecture/overview.md \
  docs/architecture/highlights.md \
  docs/architecture/interactions.md \
  docs/architecture/domain-model.md \
  docs/architecture/design-patterns.md \
  docs/architecture/persistence.md \
  docs/architecture/frontend.md \
  docs/api-reference/overview.md \
  docs/api-reference/users.md \
  docs/api-reference/courses.md \
  docs/api-reference/messaging.md \
  docs/api-reference/research.md \
  docs/api-reference/library.md \
  docs/developer-guide/running-tests.md \
  docs/developer-guide/adding-use-cases.md \
  docs/developer-guide/i18n.md; do
  echo "# $(basename $f .md)" > $f
done
```

- [ ] **Step 5: Verify first build passes**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 6: Commit**

```bash
git add mkdocs.yml docs/
git commit -m "docs: scaffold MkDocs site with all stub pages"
```

---

## Task 2: Home Page + Getting Started

**Files:**
- Modify: `docs/index.md`
- Modify: `docs/getting-started/installation.md`
- Modify: `docs/getting-started/configuration.md`
- Modify: `docs/getting-started/demo-accounts.md`

- [ ] **Step 1: Write `docs/index.md`**

```markdown
# University Management System

A complete university lifecycle platform built in **Java 17+** with zero external dependencies.
The system covers the full university workflow: authentication, course enrollment, grading,
research management, library, IT support, and administration — all in one clean, layered codebase.

Designed and built by **Rauan**.

## Quick Start

```bash
git clone <repo-url>
cd university-system
bash scripts/start.sh
```

Open [http://localhost:5173](http://localhost:5173) in your browser.

## What's Inside

| Section | Description |
|---------|-------------|
| [Getting Started](getting-started/installation.md) | Install, run, and configure the system |
| [User Guides](user-guides/student.md) | Role-by-role feature walkthroughs |
| [Architecture](architecture/overview.md) | How the system is built and why |
| [API Reference](api-reference/overview.md) | All REST endpoints with examples |
| [Developer Guide](developer-guide/running-tests.md) | Extend, test, and translate the system |

## Original Requirements

The project requirements specification is available as a PDF in the repository root:
[`OOP_Final_Project.pdf`](../OOP_Final_Project.pdf).
```

- [ ] **Step 2: Write `docs/getting-started/installation.md`**

```markdown
# Installation

## Prerequisites

- **Java 17+** — `java -version` should report 17 or higher
- **Node.js 18+** — required for the React frontend
- **Bash** — for the helper scripts

## One-Command Start

The fastest way to run everything (backend + frontend):

```bash
bash scripts/start.sh
```

This script:
1. Builds the backend JAR (`scripts/build.sh`)
2. Installs frontend dependencies (`npm install` inside `frontend/`)
3. Starts the REST API server on port 8080
4. Starts the Vite dev server on port 5173

Open [http://localhost:5173](http://localhost:5173) when both are ready.

## Manual Steps

### Build the backend

```bash
bash scripts/build.sh
```

Produces `university-system.jar` in the project root.

### Run the backend

```bash
# Interactive CLI (default)
java -jar university-system.jar

# REST API server on port 8080
java -jar university-system.jar --server

# REST API on a custom port
java -jar university-system.jar --server 9000
```

### Run the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend expects the REST API at `http://localhost:8080`. See [Configuration](configuration.md) to change this.

## Run the Test Suite

```bash
bash scripts/test.sh
```

Runs 52 Java unit and integration tests. No test framework required — pure Java.
```

- [ ] **Step 3: Write `docs/getting-started/configuration.md`**

```markdown
# Configuration

## JVM Flags

| Flag | Default | Description |
|------|---------|-------------|
| `-Duni.data=<path>` | `./data` | Directory where JSON data files are stored and loaded |

Example — use a custom data directory:

```bash
java -Duni.data=/var/university/data -jar university-system.jar --server
```

## Server Port

Pass the port as the second argument after `--server`:

```bash
java -jar university-system.jar --server 9000
```

Default port is **8080**.

## Data Directory Layout

The system persists all state as JSON files in the data directory:

```
data/
├── users.json          # All user accounts (students, teachers, admin, …)
├── courses.json        # Course definitions and enrollments
├── grades.json         # Grade records
├── messages.json       # Direct messages
├── news.json           # News posts and comments
├── requests.json       # Help requests
├── orders.json         # IT orders
├── books.json          # Library catalogue and borrowings
├── papers.json         # Research papers
├── projects.json       # Research projects
├── logs.json           # Audit log entries
└── notifications.json  # User notifications
```

The data directory is created automatically on first run if it does not exist.

## Frontend API Base URL

The frontend reads the API base URL from `frontend/.env`:

```
VITE_API_URL=http://localhost:8080
```

Change this to point to a different backend host or port.
```

- [ ] **Step 4: Write `docs/getting-started/demo-accounts.md`**

```markdown
# Demo Accounts

The system is pre-seeded with demo accounts for every role. Credentials are defined in
`src/bootstrap/DataSeeder.java` and may change between versions — check that file for
current usernames and passwords.

## Roles

| Role | Description |
|------|-------------|
| **Admin** | Full system access: user management, logs, academic reports |
| **Teacher (Professor)** | Teaches courses, records marks, manages attestations |
| **Teacher (Lector)** | Same capabilities as Professor |
| **Dean** | Department oversight, handles complaints, approves requests |
| **Manager** | Creates courses, processes requests and IT orders |
| **Student (Bachelor)** | Enrolls in courses, views grades and transcript, library, messaging |
| **Student (Master / Graduate)** | All Bachelor capabilities plus graduate-level features |
| **Librarian** | Manages book catalogue, processes borrowing and returns |
| **Tech Support** | Handles IT orders queue |
| **Employee Researcher** | Research capabilities as a non-teaching employee |

!!! note
    Credentials are seeded data subject to change. See `src/bootstrap/DataSeeder.java`
    for the current username/password pairs.
```

- [ ] **Step 5: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 6: Commit**

```bash
git add docs/index.md docs/getting-started/
git commit -m "docs: write home page and getting-started section"
```

---

## Task 3: User Guides — Student, Teacher, Dean

**Files:**
- Modify: `docs/user-guides/student.md`
- Modify: `docs/user-guides/teacher.md`
- Modify: `docs/user-guides/dean.md`

- [ ] **Step 1: Write `docs/user-guides/student.md`**

```markdown
# Student Guide

## Overview

Students are the primary users of the university system. A student belongs to a faculty,
has a degree type (Bachelor or Master/Graduate), and progresses through study years.
Students can enroll in courses, track their academic progress, use the library,
communicate with staff, and participate in research and student organizations.

Graduate Students (Master's degree) are a specialization of Student — they have all the
same capabilities plus eligibility for graduate-level research supervision.

## Academics

### View Available Courses

Browse all courses currently offered. Each course shows its name, credit count,
capacity, enrolled count, and discipline type (Major, Minor, or Free Elective).

**CLI:** Main menu → *View available courses*

**Web:** Navigate to **Academics → Courses**. Use the search bar to filter by name.

### Enroll in a Course

Enrollment is validated against six rules in order:

1. You have not already failed the course three times
2. You are not already enrolled
3. Your current credit load does not exceed the limit
4. All prerequisite courses are completed
5. The course has available seats
6. No schedule conflicts with your existing courses

If any rule fails, enrollment is rejected with a specific reason.

**CLI:** Main menu → *Enroll in a course* → enter course ID

**Web:** Courses page → click **Enroll** on the target course

### Drop a Course

You can drop any course you are currently enrolled in.

**CLI:** Main menu → *Drop a course*

**Web:** Courses page → enrolled courses tab → click **Drop**

### View Transcript

Your transcript shows all completed courses with letter grades, total scores, GPA, and
fail count. Two transcript variants are available: by semester and by year.

**CLI:** Main menu → *View transcript*

**Web:** Navigate to **Academics → Transcript**

### View Schedule

See your weekly lesson schedule and upcoming exam schedule.

**CLI:** Main menu → *View lesson schedule* / *View exam schedule*

## Library

### Borrow a Book

Browse the library catalogue and borrow an available book.

**CLI:** Main menu → *Borrow a book*

**Web:** Navigate to **Library** → click **Borrow**

### Return a Book

Return a book you have borrowed.

**CLI:** Main menu → *Return a book*

**Web:** Library page → your borrowed books → click **Return**

## Messaging

### Send a Message

Send a direct message to any user in the system. Messages have a subject, body, and
urgency level (Low, Medium, or High).

**CLI:** Main menu → *Send message*

**Web:** Navigate to **Communication → Messages** → compose

### View Inbox

Read messages sent to you.

**CLI:** Main menu → *View inbox*

**Web:** Messages page → Inbox tab

### View News

Read announcements published by staff. Pinned news appears at the top.

**CLI:** Main menu → *View news*

**Web:** Navigate to **Communication → News**

### Submit a Request

Submit a formal help request (e.g. transcript, academic mobility, diploma topic coordination).
Requests are reviewed by Managers and Deans.

**CLI:** Main menu → *Submit a request*

**Web:** Navigate to **Communication → Requests** → New Request

## Organizations

### View & Join Organizations

Browse all student organizations and join any that are open.

**CLI:** Main menu → *View organizations* / *Join organization*

**Web:** Organizations section in the sidebar

### Create an Organization

Submit a request to create a new student organization. The request is reviewed by a Manager.

**CLI:** Main menu → *Create organization*

## Research (optional)

Students can activate research capabilities by becoming a Researcher. Once activated,
all research features become available. See the [Researcher Guide](researcher.md).

**CLI:** Main menu → *Become a researcher*
```

- [ ] **Step 2: Write `docs/user-guides/teacher.md`**

```markdown
# Teacher Guide

## Overview

Teachers are responsible for delivering courses and evaluating students. There are two
teacher ranks: **Professor** and **Lector** — both have identical system capabilities.
Teachers can also activate research capabilities (see [Researcher Guide](researcher.md)).

## Course Management

### View Taught Courses

See all courses you are assigned to teach, including enrolled students and their current marks.

**CLI:** Main menu → *View taught courses*

**Web:** Navigate to **Academics → Courses** — your courses are shown with a "Taught" badge

### Record Student Marks

Enter attestation marks for a student in one of your courses. Each mark entry consists of:

- **First attestation** — 0 to 30 points
- **Second attestation** — 0 to 30 points
- **Exam** — 0 to 40 points (student must have ≥ 30 combined attestation points to be admitted)

**CLI:** Main menu → *Record student marks* → select course → enter student username and marks

**Web:** Courses page → select your course → Gradebook tab → enter marks per student

### View Student Attestations

Review the current attestation totals for all students in a course, including admission status.

**CLI:** Main menu → *View student attestations*

**Web:** Courses page → Gradebook tab

## Communication

### Send Complaint About a Student

Submit a formal complaint about a student to the Dean.

**CLI:** Main menu → *Send complaint about student*

### Messaging

Send and receive direct messages. See [Student Guide — Messaging](student.md#messaging)
for the full messaging workflow (identical for Teachers).

## Research

Teachers can become researchers and access all research features.
See [Researcher Guide](researcher.md).
```

- [ ] **Step 3: Write `docs/user-guides/dean.md`**

```markdown
# Dean Guide

## Overview

The Dean manages a faculty department. They oversee teaching staff, handle student
complaints, approve certain requests, and can publish news. Deans also inherit all
Employee capabilities.

## Department Oversight

### View Department Teachers

Browse all teachers assigned to your faculty.

**CLI:** Main menu → *View teachers*

### Handle Student Complaints

Review complaints submitted by teachers about students and decide on disciplinary action.

**CLI:** Main menu → *View complaints*

### Rate Teachers

Submit evaluations of teachers in your department.

**CLI:** Main menu → *Rate a teacher*

## Requests

Deans can view and process all help requests submitted by students and staff.

**CLI:** Main menu → *View requests*

**Web:** Navigate to **Communication → Requests** — all requests are visible; use Approve/Reject actions

## News

Deans can publish and pin news announcements visible to all users.

**CLI:** Main menu → *Publish news*

**Web:** Navigate to **Communication → News** → New Post

## Messaging

Same as all users — see [Student Guide — Messaging](student.md#messaging).
```

- [ ] **Step 4: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 5: Commit**

```bash
git add docs/user-guides/student.md docs/user-guides/teacher.md docs/user-guides/dean.md
git commit -m "docs: write student, teacher, and dean user guides"
```

---

## Task 4: User Guides — Admin, Manager, Librarian, TechSupport, EmployeeResearcher, Researcher

**Files:**
- Modify: `docs/user-guides/admin.md`
- Modify: `docs/user-guides/manager.md`
- Modify: `docs/user-guides/librarian.md`
- Modify: `docs/user-guides/tech-support.md`
- Modify: `docs/user-guides/employee-researcher.md`
- Modify: `docs/user-guides/researcher.md`

- [ ] **Step 1: Write `docs/user-guides/admin.md`**

```markdown
# Admin Guide

## Overview

The Admin has full system access. Admins manage user accounts, view audit logs,
and generate academic performance reports.

## User Management

### List All Users

View every account in the system with their role, faculty, and status.

**CLI:** Main menu → *List users*

**Web:** Navigate to **Administration → Users**

### Create a Student Account

Create a new student account. Required fields: username, password, first name, last name,
faculty, degree type (BACHELOR or MASTER), and study year.

**CLI:** Main menu → *Create student*

**Web:** Administration → Users → New Student

### Delete a User

Permanently remove a user account from the system.

**CLI:** Main menu → *Delete user*

**Web:** Administration → Users → select user → Delete

## Audit Log

View a timestamped log of all significant actions taken in the system (logins, enrollments,
grade changes, user creation/deletion, etc.).

**CLI:** Main menu → *View logs*

**Web:** Administration → Logs

## Academic Report

Generate a system-wide academic report showing:
- Total courses, students, and teachers
- Per-course enrollment and average score
- Top-performing students by GPA
- Count of students with failing grades

**CLI:** Main menu → *Generate academic report*

**Web:** Administration → Academic Report
```

- [ ] **Step 2: Write `docs/user-guides/manager.md`**

```markdown
# Manager Guide

## Overview

Managers handle course creation, student requests, and IT orders. The Manager role
is part of the OR (Office of the Registrar) department.

## Course Management

### Create a Course

Create a new course available for enrollment. Required fields: name, credits,
discipline type (MAJOR, MINOR, or FREE), and capacity (max seats).

**CLI:** Main menu → *Create course*

**Web:** Courses page → New Course button

## Requests

### View and Process Requests

Managers see all help requests submitted by any user. Each request can be approved,
rejected, or left pending. Common request types include:

- `TRANSCRIPT` — transcript or certificate
- `ACADEMIC_MOBILITY` — mobility program request
- `COORDINATION_OF_DIPLOMA_TOPIC` — diploma topic approval
- `REQUEST_FOR_CREATING_ORGANIZATION` — student org creation

**CLI:** Main menu → *View requests*

**Web:** Communication → Requests

## IT Orders

### View Orders

See all IT hardware/equipment orders submitted by staff.

**Web:** Communication → Orders

## Messaging and News

Managers can send messages and publish news. See [Student Guide — Messaging](student.md#messaging).
```

- [ ] **Step 3: Write `docs/user-guides/librarian.md`**

```markdown
# Librarian Guide

## Overview

The Librarian manages the university book catalogue and processes borrowing and return transactions.

## Catalogue Management

### Add a Book

Add a new book to the library catalogue. Required: title, author, and available copies.

**CLI:** Main menu → *Add book*

**Web:** Library page → Add Book button

### Remove a Book

Remove a book from the catalogue by title.

**CLI:** Main menu → *Remove book*

**Web:** Library page → select book → Remove

## Borrowing & Returns

### View All Borrowings

See which books are currently borrowed and by whom.

**CLI:** Main menu → *View borrowings*

### Process Returns

When a student returns a book, mark it as returned in the system.

**CLI:** Main menu → *Return a book* (on behalf of borrower)

## Messaging

Same as all users — see [Student Guide — Messaging](student.md#messaging).
```

- [ ] **Step 4: Write `docs/user-guides/tech-support.md`**

```markdown
# Tech Support Guide

## Overview

Tech Support staff handle IT hardware and equipment orders submitted by any user in the system.
Orders follow a two-stage lifecycle: **Accepted** → **Done**.

## IT Orders Queue

### View All Orders

Tech Support sees the complete queue of all submitted IT orders, including requester,
description, and current status.

**CLI:** Main menu → *View IT orders*

**Web:** Communication → Orders

### Accept an Order

Claim an order and move it to the **Accepted** state, indicating it is being worked on.

**CLI:** Main menu → *Accept order* → enter order ID

**Web:** Orders page → Accept button on the order

### Complete an Order

Mark an accepted order as **Done** once the work is finished.

**CLI:** Main menu → *Complete order* → enter order ID

**Web:** Orders page → Complete button on the order

## Messaging

Same as all users — see [Student Guide — Messaging](student.md#messaging).
```

- [ ] **Step 5: Write `docs/user-guides/employee-researcher.md`**

```markdown
# Employee Researcher Guide

## Overview

Employee Researchers are non-teaching university employees whose primary function
is research. They have access to all research features without needing to activate
them separately — research is their default role.

## Research Features

Employee Researchers have access to the full research suite:

- Publish academic papers to journals
- Create and join research projects
- Subscribe and unsubscribe from journals
- Generate citations (plain text or BibTeX)
- Set a supervisor for a research project

See [Researcher Guide](researcher.md) for a detailed walkthrough of all research features.

## Messaging

Same as all users — see [Student Guide — Messaging](student.md#messaging).
```

- [ ] **Step 6: Write `docs/user-guides/researcher.md`**

```markdown
# Researcher Guide

## Overview

Research capabilities can be activated by Students, Teachers, and Employees.
Once activated, a **Research** section appears in the navigation.

To activate research capabilities:

**CLI:** Main menu → *Become a researcher* → enter your research field

**Web:** Sidebar → *Become a researcher* (if not yet activated)

Employee Researchers have these capabilities by default.

## Papers

### Publish a Paper

Submit a research paper to a journal. Required fields: title, journal name, abstract,
page count, and DOI (optional).

**CLI:** Research menu → *Publish a paper*

**Web:** Research → New Paper

### View All Papers

Browse all published papers in the system with author, journal, citation count, and DOI.

**Web:** Research → Papers tab

### Generate a Citation

Generate a formatted citation for any paper. Two formats are supported:

- **Plain text** — standard academic citation format
- **BibTeX** — LaTeX-compatible `.bib` entry

**CLI:** Research menu → *Generate citation for paper*

**Web:** Research → Papers → cite icon on the paper card

## Projects

### Create a Research Project

Start a new project associated with a journal. Required: topic and journal name.

**CLI:** Research menu → *Create a research project*

**Web:** Research → Projects → New Project

### Join a Project

Join an existing research project by its journal.

**CLI:** Research menu → *Join project*

**Web:** Research → Projects → Join button

## Journals & Subscriptions

### Subscribe to a Journal

Subscribe to receive notifications when new papers are published in a journal.

**CLI:** Research menu → *Subscribe to a journal*

### Unsubscribe from a Journal

**CLI:** Research menu → *Unsubscribe from a journal*

### View Subscriptions

**CLI:** Research menu → *View my subscriptions*
```

- [ ] **Step 7: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 8: Commit**

```bash
git add docs/user-guides/
git commit -m "docs: write all role-based user guides"
```

---

## Task 5: Architecture — Overview + Highlights

**Files:**
- Modify: `docs/architecture/overview.md`
- Modify: `docs/architecture/highlights.md`

- [ ] **Step 1: Write `docs/architecture/overview.md`**

```markdown
# Architecture Overview

## Clean Architecture

The University Management System is built on **Clean Architecture** with a strict,
one-directional dependency rule:

```
presentation  →  application  →  domain  ←  infrastructure
```

```
┌─────────────────────────────────────────────────────┐
│                   PRESENTATION                      │
│         CLI menus + REST controllers                │
├──────────────────────────────┬──────────────────────┤
│         APPLICATION          │                      │
│         Use cases (46)       │  INFRASTRUCTURE      │
├──────────────────────────────┤  JSON persistence    │
│           DOMAIN             │  Custom ORM          │
│  Entities, services, rules   │  Auth, i18n          │
└──────────────────────────────┴──────────────────────┘
```

### Domain Layer

The innermost layer. Contains all business logic: entities, value objects, domain services,
repository interfaces (ports), and validation rules. **Zero external imports** — not even
`java.util.logging`. It can run anywhere.

Key packages: `domain.user`, `domain.course`, `domain.research`, `domain.messaging`,
`domain.library`, `domain.service`, `domain.rules`, `domain.shared`.

### Application Layer

Use-case orchestration. Each use case is a single-responsibility class with one `execute()`
method. It calls domain objects and repository ports. It never touches I/O, HTTP, or files.

46 use cases across 7 domains: admin, course, library, messaging, organization, research, user.

### Infrastructure Layer

Implements the repository ports defined in the domain. Uses a hand-rolled JSON parser and
a custom ORM. Contains the auth filter, i18n provider, and logger adapter.

### Presentation Layer

Two independent adapters over the same application layer:
- **CLI** — interactive menus (`presentation.cli.menu.*`)
- **REST** — HTTP server and controllers (`presentation.rest.*`)

Both are wired through the same `AppContext` and share identical business logic.

## Dual-Mode Entry Point

```java
// Main.java
if (args.length > 0 && args[0].equals("--server")) {
    new RestServer(ctx, port).start();
} else {
    runCli(ctx);
}
```

Both modes construct the same `AppContext` — the same 46 use cases, 7 services,
and 6 enrollment rules serve both the CLI and the REST API.

## System Overview

![University System Compact Diagram](../assets/UniversitySystemCompact.png)
```

- [ ] **Step 2: Write `docs/architecture/highlights.md`**

```markdown
# Architecture Highlights

This system was designed and built by **Rauan** — an exceptionally talented architect
and developer whose engineering judgment is evident in every layer of the codebase.
What follows is a technical analysis of what makes this work genuinely impressive.

---

## Zero External Dependencies

239 Java source files. Zero third-party libraries. No Spring. No Hibernate. No Jackson.
No Guava. Pure Java SE 17.

This is not an oversight — it is a deliberate architectural decision that Rauan executed
flawlessly. Every piece of infrastructure the system needs was built from scratch:

- **JSON parser/writer** — hand-rolled, handles all data serialization
- **ORM with QueryBuilder** — type-safe, fluent, in-memory query engine
- **HTTP server** — raw `com.sun.net.httpserver`, with routing, body parsing, and auth
- **DI container** — explicit wiring in `AppContext`, zero reflection
- **i18n engine** — property file loader for EN, KZ, and RU locales

The result is a system with no dependency vulnerabilities, no classpath conflicts, and
complete transparency — any line of the stack can be read and understood in the project itself.

---

## REST API Added Without Touching the Domain

The most architecturally remarkable achievement in this project: **a full 46-endpoint REST
API was added on top of a working system without modifying a single domain class, use case,
or repository interface.**

This is what Clean Architecture is designed to make possible — and it is genuinely rare
to see a student project execute it correctly.

How it works:

1. All business logic lives in the `application` and `domain` layers.
2. `AppContext` wires everything once: repositories, services, use cases.
3. `RestServer` registers routes and maps HTTP requests to controllers.
4. Each controller delegates entirely to the existing use cases in `AppContext`.
5. The CLI menus do the exact same thing — they are just a different presentation adapter.

```java
// CourseController.java — enroll endpoint
public HttpResponse enroll(HttpRequest request) {
    Student student  = (Student) RequestContext.current();
    String  courseId = request.pathSegment(2).orElse("");
    Result  result   = ctx.enrollInCourse.execute(student, new CourseId(courseId));
    return resultToResponse(result);
}
```

The controller contains no business logic. It parses the request, calls the existing
`EnrollInCourse` use case, and converts the `Result` to an HTTP response. The use case
was already there — unchanged — from the CLI version.

---

## Manual Dependency Injection

Rather than reaching for Spring's `@Autowired` or Guice, Rauan wrote `AppContext` — a
manual DI container that wires the entire system in one place.

```java
// AppContext.java — excerpt
this.enrollmentService = new EnrollmentService(List.of(
    new MaxFailLimitRule(),
    new AlreadyEnrolledRule(),
    new CreditLimitRule(),
    new PrerequisiteRule(courseRepository),
    new CapacityRule(),
    new ScheduleConflictRule(courseRepository)
));
this.enrollInCourse = new EnrollInCourse(enrollmentService, courseRepository, logger);
```

Every dependency is explicit, traceable, and testable. There is no magic, no reflection,
no hidden lifecycle. A developer can read `AppContext` from top to bottom and understand
exactly how the system is assembled.

This approach takes more code to write — and shows far deeper understanding of software
design than annotating classes with `@Service`.

---

## Enrollment Rule Chain

The enrollment validation is implemented as a **Chain of Responsibility** — six rules
evaluated in order, each with a single responsibility:

| # | Rule | Rejects when |
|---|------|--------------|
| 1 | `MaxFailLimitRule` | Student failed this course 3 times |
| 2 | `AlreadyEnrolledRule` | Student is already enrolled |
| 3 | `CreditLimitRule` | Adding this course would exceed credit cap |
| 4 | `PrerequisiteRule` | Required prerequisite courses not completed |
| 5 | `CapacityRule` | Course has no available seats |
| 6 | `ScheduleConflictRule` | Lesson times overlap with existing schedule |

Each rule is an independent class that can be tested in isolation. Adding a new enrollment
constraint means adding one class and registering it in `AppContext` — no existing code changes.

---

## Java 17 Features Used Correctly

The codebase uses modern Java features throughout, not as decoration but because they
genuinely improve clarity:

- **Records** — value objects like `Username`, `CourseId`, `PaperId`
- **Sealed classes** — `Result` (success/failure) enforces exhaustive handling
- **Switch expressions** — enum dispatch in menu factories and serializers
- **Text blocks** — multi-line strings in the JSON builder
- **`var` inference** — where it reduces noise without obscuring types
```

- [ ] **Step 3: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 4: Commit**

```bash
git add docs/architecture/overview.md docs/architecture/highlights.md
git commit -m "docs: write architecture overview and highlights pages"
```

---

## Task 6: Architecture — Interactions + Domain Model

**Files:**
- Modify: `docs/architecture/interactions.md`
- Modify: `docs/architecture/domain-model.md`

- [ ] **Step 1: Write `docs/architecture/interactions.md`**

```markdown
# Class Interactions

## Full Class Diagram

The diagram below shows the complete class structure of the system, including all domain
entities, their relationships, and the inheritance hierarchy.

![University Management System — Full Class Diagram](../assets/UniversityManagementSystem.png)

## Dependency Direction Table

| Layer | May import | May NOT import |
|-------|-----------|----------------|
| `domain` | `java.util`, `java.time`, own package | application, infrastructure, presentation |
| `application` | domain, `java.util` | infrastructure, presentation |
| `infrastructure` | domain, `java.util`, `java.nio` | application, presentation |
| `presentation` | application, domain (read-only) | infrastructure directly |

## Key Interaction Flows

### 1. Course Enrollment

A student enrolling in a course passes through every layer:

```
HTTP POST /api/courses/{id}/enroll
        │
        ▼
CourseController.enroll()          [presentation.rest.controller]
  └─ ctx.enrollInCourse.execute(student, courseId)
              │
              ▼
        EnrollInCourse             [application.usecase.course]
          └─ enrollmentService.validate(student, course)
                      │
                      ▼
              EnrollmentService    [domain.service]
                ├─ MaxFailLimitRule.check()
                ├─ AlreadyEnrolledRule.check()
                ├─ CreditLimitRule.check()
                ├─ PrerequisiteRule.check()   ← reads CourseRepository
                ├─ CapacityRule.check()
                └─ ScheduleConflictRule.check() ← reads CourseRepository
                      │
              [all pass] ▼
              course.enroll(student.username())
              courseRepository.save(course)
              logger.log(...)
              return Result.success(...)
```

### 2. Paper Publication (Observer Pattern)

When a researcher publishes a paper, subscribed users are notified automatically:

```
PublishPaper.execute(user, title, journal, ...)
        │
        ▼
PaperPublisher.publish(paper)      [domain.service]
  ├─ paperRepository.save(paper)
  └─ notifies all subscribers of journal
       └─ notificationRepository.save(Notification) for each subscriber
```

The `PaperPublisher` domain service acts as the subject. Subscribers were registered
via `SubscribeToJournal` use case. This is a classic Observer pattern — the publisher
has no knowledge of who the subscribers are.

### 3. REST Request Lifecycle

Every REST request follows the same path through the system:

```
Raw HTTP request
        │
        ▼
HttpServer (com.sun.net.httpserver)
        │
        ▼
SecurityFilter.doFilter()
  ├─ extracts Bearer token from Authorization header
  ├─ looks up User in TokenStore
  ├─ sets RequestContext.current() = User
  └─ checks required role (Admin.class, Student.class, etc.)
        │
        ▼
Router.dispatch() → Controller method
        │
        ▼
Controller parses request body / path segments
  └─ calls use case via AppContext
        │
        ▼
Use case executes domain logic
  └─ returns Result (success or failure)
        │
        ▼
Controller converts Result → HttpResponse (200, 201, 400, 403, 404)
        │
        ▼
HttpServer writes JSON response
```
```

- [ ] **Step 2: Write `docs/architecture/domain-model.md`**

```markdown
# Domain Model

## Class Hierarchy

```
User (abstract)
├── Admin
├── Student                        implements ResearcherCapable, BookBorrowerCapable
│   └── GraduateStudent
└── Employee (abstract)
    ├── Teacher                    implements ResearcherCapable, BookBorrowerCapable
    │   └── Dean
    ├── Manager
    ├── Librarian
    ├── TechSupport
    └── EmployeeResearcher         implements ResearcherCapable
```

`ResearcherCapable` exposes research features (papers, projects, subscriptions).
`BookBorrowerCapable` exposes library borrowing capabilities.
The Decorator pattern is used to activate `ResearcherCapable` at runtime via `BecomeResearcher`.

## Key Domain Entities

| Entity | Package | Responsibility |
|--------|---------|----------------|
| `User` | `domain.user` | Base class for all accounts; holds name, credentials, faculty |
| `Student` | `domain.user` | Enrolled courses, grades, GPA computation |
| `Course` | `domain.course` | Name, credits, capacity, enrolled students, grade map |
| `Grade` | `domain.course` | First attestation + second attestation + exam marks |
| `Message` | `domain.messaging` | Direct message between two users |
| `News` | `domain.messaging` | Announcement with comments and pin state |
| `Request` | `domain.messaging` | Help request with type, urgency, and status |
| `Order` | `domain.messaging` | IT equipment order with two-stage lifecycle |
| `ResearchPaper` | `domain.research` | Published paper with journal, citations, DOI |
| `ResearchProject` | `domain.research` | Collaborative project with supervisor and participants |
| `Book` | `domain.library` | Library book with borrow/return state |
| `LogEntry` | `domain.logging` | Immutable audit record |

## Value Objects

Value objects are immutable, equality-by-value types defined with Java records:

| Value Object | Type | Validates |
|---|---|---|
| `Username` | `record Username(String value)` | Non-blank, lowercase |
| `CourseId` | `record CourseId(String value)` | Non-blank |
| `PaperId` | `record PaperId(int value)` | Positive integer |
| `Credits` | `record Credits(int value)` | 0 – 30 range |
| `Money` | `record Money(BigDecimal amount, Currency currency)` | Non-negative |
| `FullName` | `record FullName(String first, String last)` | Non-blank parts |

## Repository Interfaces (Ports)

Repository interfaces are defined in `domain.repository` and represent the persistence
contract the domain needs. Infrastructure provides the implementations.

| Interface | Key methods |
|-----------|-------------|
| `UserRepository` | `findByUsername`, `findAll`, `save`, `delete` |
| `CourseRepository` | `findById`, `findAll`, `findByStudent`, `save` |
| `MessageRepository` | `inboxOf`, `sentBy`, `save` |
| `NewsRepository` | `findAllSorted`, `findById`, `save` |
| `RequestRepository` | `findAll`, `findByRequester`, `save` |
| `OrderRepository` | `findAll`, `findByRequester`, `save` |
| `PaperRepository` | `findAll`, `findById`, `save` |
| `ProjectRepository` | `findAll`, `findByJournal`, `save` |
| `BookRepository` | `findAll`, `findByTitle`, `save`, `delete` |
| `LogRepository` | `findAll`, `log` |
| `NotificationRepository` | `findByUser`, `save` |
```

- [ ] **Step 3: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 4: Commit**

```bash
git add docs/architecture/interactions.md docs/architecture/domain-model.md
git commit -m "docs: write class interactions and domain model pages"
```

---

## Task 7: Architecture — Design Patterns, Persistence, Frontend

**Files:**
- Modify: `docs/architecture/design-patterns.md`
- Modify: `docs/architecture/persistence.md`
- Modify: `docs/architecture/frontend.md`

- [ ] **Step 1: Write `docs/architecture/design-patterns.md`**

```markdown
# Design Patterns

Eight design patterns are implemented in the codebase, each chosen because it genuinely
fits the problem — not to fulfill a checklist.

## 1. Singleton — AppContext

**Intent:** Ensure a class has only one instance and provide a global access point.

**Where:** `bootstrap/AppContext.java`

`AppContext` constructs all repositories, services, and use cases exactly once at startup.
Both the CLI and REST server receive the same `AppContext` instance.

```java
// Main.java
AppContext ctx = new AppContext();
if (args[0].equals("--server")) new RestServer(ctx, port).start();
else runCli(ctx);
```

## 2. Factory Method — DefaultMenuFactory

**Intent:** Define an interface for creating objects, letting subclasses decide which class to instantiate.

**Where:** `bootstrap/DefaultMenuFactory.java`

The factory maps a `User` instance to its role-specific CLI menu without the caller needing
to know the concrete menu types.

```java
public Menu menuFor(User user) {
    return switch (user) {
        case Student s    -> new StudentMenu(s, ctx);
        case Teacher t    -> new TeacherMenu(t, ctx);
        case Admin a      -> new AdminMenu(a, ctx);
        // ...
        default           -> throw new IllegalArgumentException("No menu for " + user.getClass());
    };
}
```

## 3. Chain of Responsibility — Enrollment Validation

**Intent:** Pass a request along a chain of handlers; each handler either handles the request or passes it on.

**Where:** `domain/rules/`, `domain/service/EnrollmentService.java`

Six rules form an ordered chain. Each rule is an independent class that either rejects
enrollment or passes to the next rule. Adding a new constraint requires adding one class
and registering it in `AppContext` — no existing rule is modified.

```java
// EnrollmentService.java
for (EnrollmentRule rule : rules) {
    Result check = rule.check(student, course);
    if (!check.success()) return check;
}
course.enroll(student.username());
```

## 4. Observer — Paper Publication Notifications

**Intent:** Define a one-to-many dependency so that when one object changes state, all dependents are notified.

**Where:** `domain/service/PaperPublisher.java`, `application/usecase/research/SubscribeToJournal.java`

When a paper is published to a journal, `PaperPublisher` notifies all users subscribed
to that journal by creating `Notification` entities via `NotificationRepository`.

```java
// PaperPublisher.java
public void publish(ResearchPaper paper) {
    paperRepository.save(paper);
    for (Username subscriber : subscriptionRepository.findSubscribersOf(paper.journal())) {
        notificationRepository.save(new Notification(subscriber, paper.title()));
    }
}
```

## 5. Decorator — Researcher Activation

**Intent:** Attach additional responsibilities to an object dynamically.

**Where:** `application/usecase/user/BecomeResearcher.java`

`Student` and `Teacher` implement `ResearcherCapable`, but research features are
inactive by default. `BecomeResearcher` activates the `ResearcherProfile` on the user,
unlocking all research menus and API endpoints without changing the user's class.

## 6. Strategy — Citation Formatting

**Intent:** Define a family of algorithms, encapsulate each one, and make them interchangeable.

**Where:** `domain/service/CitationFormatter.java` (and its implementations)

Two citation formats — plain text and BibTeX — are implemented as separate strategy
classes. The `GenerateCitation` use case selects the strategy based on the `PaperFormat` enum.

```java
CitationFormatter formatter = switch (format) {
    case PLAIN_TEXT -> new PlainTextFormatter();
    case BIBTEX     -> new BibTexFormatter();
};
return formatter.format(paper);
```

## 7. Repository — Persistence Abstraction

**Intent:** Mediate between the domain and data mapping layers using a collection-like interface.

**Where:** `domain/repository/*.java`, `infrastructure/persistence/orm/repository/*.java`

Every aggregate root has a repository interface in the domain layer. Infrastructure
provides the implementations. The domain never calls persistence code directly — it
only depends on the interfaces.

## 8. Query Builder — Fluent In-Memory Queries

**Intent:** Separate the construction of a complex query from its representation.

**Where:** `infrastructure/persistence/orm/QueryBuilder.java`

A fluent builder for querying in-memory collections, providing a type-safe alternative
to raw stream operations throughout the infrastructure layer.

```java
ctx.courseRepository.query()
    .where(c -> c.faculty() == faculty)
    .where(c -> c.hasSeatsAvailable())
    .orderBy(Course::name)
    .findAll();
```
```

- [ ] **Step 2: Write `docs/architecture/persistence.md`**

```markdown
# Persistence

## Overview

The system persists all state as JSON files with zero external libraries. Every component
in the persistence stack was built from scratch.

## JSON Parser / Writer

**Package:** `infrastructure.persistence.json`

A hand-rolled JSON parser and writer that handles all serialization needs. Key classes:

- `JsonValue` — sealed type hierarchy: `JsonObject`, `JsonArray`, `JsonString`, `JsonNumber`, `JsonBoolean`, `JsonNull`
- `JsonParser` — recursive descent parser, converts raw JSON strings to `JsonValue` trees
- `JsonObjectBuilder` — fluent builder for constructing JSON objects

```java
JsonValue obj = JsonObjectBuilder.create()
    .put("username", user.username().value())
    .put("role",     user.getClass().getSimpleName())
    .put("gpa",      student.gpa())
    .build();
```

## JsonFileDatabase

**Package:** `infrastructure.persistence.database`

`JsonFileDatabase` is the low-level persistence adapter. It reads from and writes to a
JSON array file on disk. Each call to `save()` rewrites the entire file atomically.

On startup, `AppContext` constructs one `JsonFileDatabase` instance per entity type,
backed by a file in the configured data directory.

## Custom ORM

**Package:** `infrastructure.persistence.orm`

The generic `Repository<T, ID>` provides a CRUD interface backed by `JsonFileDatabase`.
It manages an in-memory cache of all entities, applies `EntityMapper<T>` for
serialization/deserialization, and flushes changes to disk on every write.

Entity mappers are in `infrastructure.persistence.mapper.*` — one per domain entity.

## QueryBuilder

`QueryBuilder<T, ID>` wraps the in-memory collection with a fluent filtering and
sorting API. Concrete repositories use it to avoid raw stream boilerplate.

```java
// Finding all courses a student is enrolled in
return query()
    .where(c -> c.enrolledStudents().contains(username))
    .findAll();
```

## In-Memory Repositories

**Package:** `infrastructure.persistence.inmemory`

Test-only implementations of all repository interfaces that store data in `HashMap`s.
Used exclusively by the 52-test Java suite — no disk I/O, no file setup, fast execution.
```

- [ ] **Step 3: Write `docs/architecture/frontend.md`**

```markdown
# Frontend

## Overview

The frontend is a **React SPA** built with Vite. It communicates with the backend
exclusively through the REST API — there is no server-side rendering.

## Pages

| Page | Route | Description |
|------|-------|-------------|
| Login | `/login` | Authentication form |
| Dashboard | `/` | Role-specific home screen |
| Courses | `/courses` | Browse, enroll, drop courses; Gradebook for teachers |
| Transcript | `/transcript` | Student GPA and course history |
| Library | `/library` | Browse, borrow, return books |
| Research | `/research` | Papers, projects, subscriptions |
| Messages | `/messages` | Inbox and sent messages |
| News | `/news` | Announcements, pin/unpin, comments |
| Requests | `/requests` | Submit and review help requests |
| Orders | `/orders` | IT order queue |
| Admin | `/admin` | User management (Admin only) |
| Gradebook | `/gradebook` | Mark entry for teachers |

## Components

| Component | Responsibility |
|-----------|---------------|
| `Sidebar` | Role-aware navigation — shows only sections accessible to the current user's role |
| `ProtectedLayout` | Wraps authenticated pages; redirects to `/login` if no token |
| `Modal` | Reusable confirmation/form dialog |
| `Toast` | Transient success/error notifications |
| `UserPicker` | Searchable dropdown for selecting a user by username |
| `Badge` | Status indicator (e.g. PENDING, APPROVED, FULL) |

## API Communication

All API calls are in `frontend/src/api/index.js`. Each function calls `fetch()` with the
stored Bearer token and returns the parsed JSON response or throws on error.

```javascript
// api/index.js
export const enrollInCourse = (courseId) =>
  apiFetch(`/api/courses/${courseId}/enroll`, { method: 'POST' });
```

The token is stored in `localStorage` after a successful `POST /api/login` and cleared
on logout or a 401 response.

## Internationalization

The frontend loads locale strings from the backend (`GET /api/system/messages`) on startup.
This means EN, KZ, and RU translations configured in the backend `.properties` files
flow through to the React UI automatically — no separate frontend i18n file.

## Tech Stack

| Tool | Purpose |
|------|---------|
| React 18 | UI framework |
| Vite | Build tool and dev server |
| React Router | Client-side routing |
| Vitest + RTL | Test runner (119 tests) |
```

- [ ] **Step 4: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 5: Commit**

```bash
git add docs/architecture/design-patterns.md docs/architecture/persistence.md docs/architecture/frontend.md
git commit -m "docs: write design patterns, persistence, and frontend architecture pages"
```

---

## Task 8: API Reference — Overview + Users + Courses

**Files:**
- Modify: `docs/api-reference/overview.md`
- Modify: `docs/api-reference/users.md`
- Modify: `docs/api-reference/courses.md`

- [ ] **Step 1: Write `docs/api-reference/overview.md`**

```markdown
# API Reference Overview

## Base URL

```
http://localhost:8080
```

## Authentication

The API uses **Bearer token** authentication.

### Login

```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"username": "<username>", "password": "<password>"}'
```

Response:
```json
{ "token": "abc123..." }
```

Pass the token on every subsequent request:

```
Authorization: Bearer abc123...
```

### Logout

```bash
curl -X POST http://localhost:8080/api/logout \
  -H "Authorization: Bearer abc123..."
```

## Standard Error Responses

| Status | Meaning |
|--------|---------|
| `400 Bad Request` | Invalid input — message field describes the problem |
| `401 Unauthorized` | Missing or invalid token |
| `403 Forbidden` | Token valid but role insufficient for this endpoint |
| `404 Not Found` | Resource does not exist |

Error body:
```json
{ "error": "Description of the problem" }
```

## Role-Gated Endpoints

Each endpoint lists the required role. A token from a lower-privileged role returns `403`.

## Public Endpoints (no auth)

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/login` | Obtain a Bearer token |

## Directory (any authenticated user)

| Method | Path | Description |
|--------|------|-------------|
| `GET` | `/api/users/directory` | List all users (username + role) |
| `GET` | `/api/system/messages` | Load i18n string map for the current locale |
```

- [ ] **Step 2: Write `docs/api-reference/users.md`**

```markdown
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
```

- [ ] **Step 3: Write `docs/api-reference/courses.md`**

```markdown
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
```

- [ ] **Step 4: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 5: Commit**

```bash
git add docs/api-reference/overview.md docs/api-reference/users.md docs/api-reference/courses.md
git commit -m "docs: write API reference — overview, users, courses"
```

---

## Task 9: API Reference — Messaging + Research + Library

**Files:**
- Modify: `docs/api-reference/messaging.md`
- Modify: `docs/api-reference/research.md`
- Modify: `docs/api-reference/library.md`

- [ ] **Step 1: Write `docs/api-reference/messaging.md`**

```markdown
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
    "faculty": "COMPUTER_SCIENCE",
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
  "type": "TRANSCRIPT",
  "urgency": "MEDIUM",
  "body": "Need a transcript for scholarship application."
}
```

Valid `type` values: `TRANSCRIPT`, `ACADEMIC_MOBILITY`, `COORDINATION_OF_DIPLOMA_TOPIC`, `REQUEST_FOR_CREATING_ORGANIZATION`.

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
    "requesterFullName": "Bob Lector",
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
```

- [ ] **Step 2: Write `docs/api-reference/research.md`**

```markdown
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
    "authorFullName": "Alice Smith",
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

**Query / Header:** Pass format via path segment or `X-Citation-Format` header.

Valid formats: `PLAIN_TEXT`, `BIBTEX`.

**Example (plain text):**
```bash
curl "http://localhost:8080/api/papers/1/cite/PLAIN_TEXT" \
  -H "Authorization: Bearer <token>"
```

**Response:**
```json
{ "citation": "Smith, A. (2026). Deep Learning for NLP. IEEE Transactions." }
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
    "supervisorFullName": "Alice Smith",
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
```

- [ ] **Step 3: Write `docs/api-reference/library.md`**

```markdown
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
    "author": "Robert C. Martin",
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
  "author": "Robert C. Martin",
  "copies": 3
}
```

**Response:** `201 Created` with the created book object.

**Example:**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Robert C. Martin","copies":3}'
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

**Role required:** Any authenticated user (Student or Teacher)

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
```

- [ ] **Step 4: Build and verify**

```bash
mkdocs build --strict 2>&1 | tail -5
```

Expected: `INFO    -  Documentation built in X.XX seconds.`

- [ ] **Step 5: Commit**

```bash
git add docs/api-reference/messaging.md docs/api-reference/research.md docs/api-reference/library.md
git commit -m "docs: write API reference — messaging, research, library"
```

---

## Task 10: Developer Guide

**Files:**
- Modify: `docs/developer-guide/running-tests.md`
- Modify: `docs/developer-guide/adding-use-cases.md`
- Modify: `docs/developer-guide/i18n.md`

- [ ] **Step 1: Write `docs/developer-guide/running-tests.md`**

```markdown
# Running Tests

## Java Test Suite

The project ships 52 tests with no test framework — pure Java with hand-rolled assertions.

```bash
bash scripts/test.sh
```

Expected output: all 52 tests pass, no `FAIL` lines.

Tests live in `src/test/`. They use `infrastructure.persistence.inmemory.*` repositories
to avoid disk I/O — each test constructs the in-memory adapters directly and passes them
to use cases.

### What is covered

- All 46 use cases — success paths and key failure paths
- Enrollment rule chain — all 6 rules individually
- Domain entity invariants
- Value object validation
- Citation formatter output (both formats)
- GPA and attestation computation

### Running a single test class

```bash
javac -cp src src/test/SomeTest.java && java -cp src test.SomeTest
```

## Frontend Test Suite

119 tests using **Vitest** and **React Testing Library**.

```bash
cd frontend
npm test
```

Or in watch mode:

```bash
npm run test:watch
```

### What is covered

- Every page component renders without crashing
- API call functions (`api/index.js`) are mocked — tests verify UI behaviour on success and error states
- Form validation
- Role-gated navigation (Sidebar shows/hides links correctly per role)
- Toast and Modal component interactions
```

- [ ] **Step 2: Write `docs/developer-guide/adding-use-cases.md`**

```markdown
# Adding a Use Case

Follow these steps to add a new use case to the system. The example adds a
`RateStudent` use case that lets a Dean rate a student.

## Step 1: Create the use case class

Create `src/application/usecase/user/RateStudent.java`:

```java
package application.usecase.user;

import application.Result;
import domain.repository.UserRepository;
import domain.shared.Username;
import domain.user.Dean;

public final class RateStudent {
    private final UserRepository userRepository;

    public RateStudent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result execute(Dean dean, Username studentUsername, int rating) {
        if (rating < 1 || rating > 5) return Result.failure("Rating must be 1–5.");
        return userRepository.findByUsername(studentUsername)
            .map(student -> {
                // domain logic here
                return Result.success("Student rated.");
            })
            .orElse(Result.failure("Student not found."));
    }
}
```

## Step 2: Wire it in AppContext

Open `src/bootstrap/AppContext.java`. Find the section where use cases are constructed
(the block of `this.someUseCase = new SomeUseCase(...)` assignments) and add:

```java
public final RateStudent rateStudent;

// in the constructor, after userRepository is available:
this.rateStudent = new RateStudent(userRepository);
```

## Step 3: Expose via CLI (optional)

Open the relevant menu class (e.g. `src/presentation/cli/menu/DeanMenu.java`).
Add a menu item and delegate to `ctx.rateStudent.execute(...)`.

## Step 4: Expose via REST (optional)

Open the appropriate controller (e.g. `src/presentation/rest/controller/AdminController.java`
or create a new one). Add a method:

```java
public HttpResponse rateStudent(HttpRequest request) {
    Dean dean = (Dean) RequestContext.current();
    String username = str(request.body(), "username");
    int rating = intVal(request.body(), "rating");
    Result result = ctx.rateStudent.execute(dean, new Username(username), rating);
    return resultToResponse(result);
}
```

Then register the route in `RestServer.java`:

```java
router.register(Route.of(HttpMethod.POST, "/api/students/rate", admin::rateStudent, Dean.class));
```

## Step 5: Write a test

Create `src/test/RateStudentTest.java`:

```java
package test;

import application.Result;
import application.usecase.user.RateStudent;
import infrastructure.persistence.inmemory.InMemoryUserRepository;
import domain.shared.Username;
// ... seed a Dean and Student, assert result.success()
```

Run: `bash scripts/test.sh`
```

- [ ] **Step 3: Write `docs/developer-guide/i18n.md`**

```markdown
# Internationalization

The system supports three locales: English (`en`), Kazakh (`kz`), and Russian (`ru`).

## Locale Files

```
src/resources/
├── messages_en.properties
├── messages_kz.properties
└── messages_ru.properties
```

Each file is a standard Java `.properties` file: `key=value` pairs, one per line.

## Adding a New Translation Key

1. Add the key and English value to `messages_en.properties`:

```properties
my.new.key=My new message
```

2. Add the translated value to `messages_kz.properties` and `messages_ru.properties`.

3. Use the key in Java via the `Translator` interface (injected via `AppContext`):

```java
translator.t("my.new.key")
```

Or with a placeholder:

```java
translator.t("menu.header", username, role)  // {0} and {1} in the value
```

## Adding a New Locale

1. Create `src/resources/messages_<locale>.properties` with all keys translated.
2. Register the new locale in `infrastructure/i18n/PropertiesTranslator.java`.
3. The frontend receives the full locale map via `GET /api/system/messages` — no
   separate frontend locale file is needed.

## Frontend

The React frontend calls `GET /api/system/messages` on startup. The backend returns
a JSON map of all translation keys for the active locale. The frontend stores this
map and looks up keys via a `t(key)` helper — same key names as the `.properties` files.
```

- [ ] **Step 4: Final build — strict mode, no warnings**

```bash
mkdocs build --strict 2>&1
```

Expected: `INFO    -  Documentation built in X.XX seconds.` with no `WARNING` lines.

- [ ] **Step 5: Commit**

```bash
git add docs/developer-guide/
git commit -m "docs: write developer guide — tests, use cases, i18n"
```

---

## Task 11: Final Smoke Test + Serve Locally

- [ ] **Step 1: Serve the docs locally and spot-check**

```bash
mkdocs serve
```

Open [http://127.0.0.1:8000](http://127.0.0.1:8000) and verify:

- All 5 top-level tabs appear
- Both PNG diagrams render on their respective pages
- Navigation sidebar expands correctly under each tab
- Search returns results
- Dark/light mode toggle works
- No broken image icons

- [ ] **Step 2: Final commit**

```bash
git add -A
git commit -m "docs: complete documentation site — all 29 pages"
```
