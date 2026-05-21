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
