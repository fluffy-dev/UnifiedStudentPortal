# Employee Researcher Guide

## Overview

**EmployeeResearcher** is a dedicated hybrid role — a university employee who is
simultaneously a full researcher. Unlike Teachers who can *optionally* become researchers,
an EmployeeResearcher is created with researcher capabilities already enabled and stored
in their profile.

This role is useful for research associates, adjunct faculty, or lab coordinators who
are primarily research-focused rather than teaching-focused.

---

## Capabilities

An EmployeeResearcher has exactly the union of:

1. **Employee** capabilities (the base for all non-student staff):
    - Send and receive messages
    - Read and publish news (cannot pin — Managers/Deans only)
    - Submit and view own help requests
    - Submit and track IT orders
    - Use the library (borrow/return books)

2. **Researcher** capabilities (always active, no activation step required):
    - Publish research papers
    - Create and join research projects
    - Subscribe and unsubscribe to journals
    - Generate citations (BibTeX and plain text)
    - Receive notifications for subscribed journals

---

## Difference from Teacher + Researcher

| Capability | Teacher + Researcher | EmployeeResearcher |
|---|---|---|
| Teach courses | ✅ Assigned via Manager | ❌ No teaching role |
| Record grades | ✅ For assigned courses | ❌ |
| Gradebook access | ✅ | ❌ |
| View schedule | ✅ (as teacher) | Only student-enrolled view |
| Research (publish, projects, subs) | ✅ (after activation) | ✅ (always on) |
| Research field | Set manually on activation | Set at account creation |

---

## Demo Accounts

There are no pre-seeded EmployeeResearcher accounts in the demo. Accounts of this type
can only be created by editing the data files directly or extending the Admin creation UI.

If you need to test EmployeeResearcher behavior, use `zhomart` (Teacher + Researcher)
as the closest analogue — all researcher capabilities are identical.

---

## API Access

EmployeeResearcher passes `User.class`, `Employee.class`, and `ResearcherCapable` checks.
It does **not** pass `Teacher.class`, `Manager.class`, `Admin.class`, or `Student.class` checks.

Available endpoints:

| Category | Access |
|---|---|
| All User-level endpoints | ✅ |
| News publishing (`POST /api/news`) | ✅ (Employee) |
| News pinning (`PUT /api/news/{id}/pin`) | ✅ (Employee, if author) |
| Research endpoints | ✅ (all) |
| Grade recording (`POST /api/courses/{id}/marks`) | ❌ |
| Course creation (`POST /api/courses`) | ❌ |
| Admin endpoints | ❌ |
| Request processing (`PUT /api/requests/{id}`) | ❌ |
| IT order management (`PUT /api/orders/{id}/accept`) | ❌ |
