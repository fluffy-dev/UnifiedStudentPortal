# Documentation Site Design Spec
**Date:** 2026-05-21
**Project:** University Management System
**Author:** Rauan

---

## 1. Goal

Build a comprehensive, thorough MkDocs documentation site for the University Management System — covering all audiences: graders/professors evaluating the project, future developers extending it, and end users running it. The site must be compilable with both MkDocs (Material theme) and Zensical.

---

## 2. Approach

**Topic-First navigation (Option B):** top-level sections organized by subject matter, not by audience. Each reader navigates to the section relevant to them. A search bar eliminates friction. This is the standard Material for MkDocs pattern and presents professionally to graders.

---

## 3. Site Structure

```
docs/
├── index.md
├── getting-started/
│   ├── installation.md
│   ├── configuration.md
│   └── demo-accounts.md
├── user-guides/
│   ├── student.md
│   ├── teacher.md
│   ├── dean.md
│   ├── admin.md
│   ├── manager.md
│   ├── librarian.md
│   ├── tech-support.md
│   ├── employee-researcher.md
│   └── researcher.md
├── architecture/
│   ├── overview.md
│   ├── highlights.md
│   ├── interactions.md
│   ├── domain-model.md
│   ├── design-patterns.md
│   ├── persistence.md
│   └── frontend.md
├── api-reference/
│   ├── overview.md
│   ├── users.md
│   ├── courses.md
│   ├── messaging.md
│   ├── research.md
│   └── library.md
└── developer-guide/
    ├── running-tests.md
    ├── adding-use-cases.md
    └── i18n.md
```

---

## 4. MkDocs Configuration

File: `mkdocs.yml` at project root.

```yaml
site_name: University Management System
site_description: A full-stack university lifecycle system built in Java 17+ with Clean Architecture
site_author: Rauan

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
  - pymdownx.tabbed
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

---

## 5. Diagram Assets

Only two final diagram assets are used. All other diagram files (`.mermaid`, `.puml`, `Use_Case_Diagram.png`, `UseCaseDiagram1.svg`) are excluded — they are intermediate artifacts.

| File | Used in |
|------|---------|
| `diagrams/UniversityManagementSystem.png` | `architecture/interactions.md` — full class diagram |
| `diagrams/UniversitySystemCompact.png` | `architecture/overview.md` — compact overview |

---

## 6. Content Spec Per Section

### 6.1 `index.md` — Home
- One-paragraph project summary
- What the system does and who it is for
- Quick-start command block (clone → build → run)
- Link to the original requirements PDF
- Navigation card grid pointing to each top-level section

### 6.2 Getting Started

**`installation.md`**
- Prerequisites (Java 17+, Node.js)
- `bash scripts/start.sh` — single command that does everything
- Manual steps: `scripts/build.sh`, frontend install, running the JAR
- Run modes: CLI (default), REST server (`--server`), custom port, custom data dir

**`configuration.md`**
- All JVM flags (`-Duni.data`)
- Port configuration
- Data directory layout (what JSON files live where)
- Environment expectations

**`demo-accounts.md`**
- Table of all 11 roles with role name and description only — no usernames or passwords (credentials are seeded and subject to change)
- What each role can do at a high level

### 6.3 User Guides

One page per role. Each page covers every action available to that role in both the CLI and the React frontend. Structure per page:

```
# <Role> Guide
## Overview — what this role does in the university
## Getting Started — first login, what you see
## Features
### <Feature 1>
### <Feature 2>
...
```

Roles and their key features:

| Role | Key features to document |
|------|--------------------------|
| Student | Enroll/drop courses, view grades, attestation, library borrowing, messaging, research (if activated), organizations. Graduate Student (Master) is a subclass — covered as a subsection here |
| Teacher | Record marks, manage courses, rate students, research, messaging |
| Dean | Manage department, handle complaints, oversee teachers |
| Admin | Create/delete users, generate reports, system management |
| Manager | Handle requests, create IT orders |
| Librarian | Process book borrowing and returns |
| Tech Support | Handle IT orders, tech support tickets |
| Employee Researcher | Research as an employee — papers, projects, journals |
| Researcher | Publish papers, manage projects, subscribe to journals, set supervisor |

### 6.4 Architecture

**`overview.md`**
- Clean Architecture diagram (layer boxes with dependency arrows — written as ASCII or described)
- `UniversitySystemCompact.png` embedded
- One paragraph per layer: domain, application, infrastructure, presentation
- Dual-mode entry point explanation (`Main.java` switch)

**`highlights.md`** — the showcase page
- Opens with attribution: this system was designed and built by **Rauan**, an exceptionally talented developer and architect whose engineering decisions are evident in every layer of the codebase
- Celebrates the zero-dependency philosophy: 239 Java files, pure Java SE, no Spring, no Hibernate, no Jackson — and it works
- The REST-on-top story: the entire REST API was added as a second presentation adapter without modifying a single domain class, use case, or repository interface — this is Clean Architecture working exactly as its authors intended, and it is genuinely rare to see executed this cleanly
- The manual DI container (`AppContext`): instead of reaching for Spring DI, Rauan wired all 46 use cases, 7 domain services, and 6 enrollment rules by hand — explicit, readable, zero magic
- Tone: enthusiastic technical admiration, not generic praise — every claim is backed by a concrete example from the codebase

**`interactions.md`**
- `UniversityManagementSystem.png` embedded (full class diagram)
- Written sequence walkthrough of three key flows:
  1. **Course enrollment** — Student → EnrollInCourse use case → EnrollmentService → 6-rule chain → CourseRepository → result
  2. **Paper publish** — PublishPaper use case → PaperPublisher domain service → Observer fires → NotificationRepository
  3. **REST request lifecycle** — HTTP request → RestServer → controller → use case → domain → repository → JSON response
- Dependency direction table: what each layer may and may not import

**`domain-model.md`**
- Full class hierarchy (User → Student/Employee branches, interfaces)
- Key entities and their responsibilities
- Value objects (Username, Credits, Money, etc.)
- Repository interfaces as ports

**`design-patterns.md`**
- All 8 patterns: Singleton, Factory Method, Chain of Responsibility, Observer, Decorator, Strategy, Repository, Query Builder
- Per pattern: intent, where it appears in the codebase, a short code snippet from the actual implementation

**`persistence.md`**
- Hand-rolled JSON parser/writer (no external deps)
- `JsonFileDatabase` — how data is stored and loaded
- Custom ORM: `Repository<T,ID>` + `QueryBuilder` — fluent query API
- In-memory repositories used in tests

**`frontend.md`**
- React SPA architecture
- How it communicates with the REST API
- i18n (EN/KZ/RU) in the frontend
- Component structure overview

### 6.5 API Reference

**`overview.md`**
- Base URL: `http://localhost:8080`
- Authentication: Bearer token — `POST /api/login` returns a token, passed as `Authorization: Bearer <token>` on subsequent requests
- Standard error response format
- HTTP status codes used

**Per-domain pages** (`users.md`, `courses.md`, `messaging.md`, `research.md`, `library.md`)

Each endpoint entry:
```
### METHOD /path

**Role required:** <role>
**Request body:** <JSON schema or "none">
**Response:** <JSON shape>
**Example:**
curl -X METHOD http://localhost:8080/path \
  -H "Authorization: Bearer <token>" \
  -d '{"key": "value"}'
```

All 36 endpoints documented.

### 6.6 Developer Guide

**`running-tests.md`**
- Java test suite: `bash scripts/test.sh` — 52 tests, no framework
- Frontend test suite: `npm test` — 119 Vitest + RTL tests
- What each suite covers

**`adding-use-cases.md`**
- Step-by-step: create use case class in `application/usecase/<domain>/`
- Wire it in `AppContext`
- Expose via CLI menu or REST controller
- Example walkthrough

**`i18n.md`**
- Three locale files: `messages_en.properties`, `messages_kz.properties`, `messages_ru.properties`
- How to add a new key
- How to switch locale

---

## 7. Constraints

- No seeded usernames or passwords anywhere in the docs — role names only
- No mermaid/puml files embedded or rendered — only the two approved PNGs
- No `pymdownx.superfences` needed (no inline diagrams)
- All content written fresh except Getting Started which draws from `README.md`
- Compatible with both MkDocs (Material) and Zensical

---

## 8. Out of Scope

- Hosting/deployment of the docs site
- Automated API doc generation (e.g. Swagger UI)
- PDF export
- Versioning of the docs
