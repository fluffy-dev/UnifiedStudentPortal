# Researcher Guide

## Overview

**Researcher** is not a standalone role — it is an **add-on capability** that any user
with `ResearcherCapable` can activate once. The eligible base roles are:

- **Student** (including GraduateStudent)
- **Teacher**
- **EmployeeResearcher** (a dedicated combined role)

Once activated, researcher mode is permanent and cannot be deactivated.

---

## Demo Researchers

| Username | Role | Research Field |
|---|---|---|
| `zhomart` | Teacher + Researcher | Natural Language Processing & Machine Learning |
| `nurasyl` | GraduateStudent + Researcher | Low-Resource NLP |
| `aigerim` | Doctorate + Researcher | Multilingual AI & Instruction Tuning |

---

## Activating Researcher Mode

Navigate to **Research** in the sidebar. If not yet a researcher, you see the activation screen:

1. Enter your research field (e.g. "Machine Learning", "Distributed Systems")
2. Click **Become a Researcher**

Your field is stored in your profile. The Research page replaces the activation screen immediately.

---

## What a Researcher Can Do

### Publications Tab

**Browse all papers**
Every published paper in the system, showing:
- Title
- Author (full name + @username)
- Journal / Venue
- Published date
- Citation count
- Page count

**Publish a paper**
Click **＋ Publish Paper**:

| Field | Required | Notes |
|---|---|---|
| Title | ✅ | |
| Journal / Venue | ✅ | Must match a ResearchProject journal if auto-announcement desired |
| Pages | | Defaults to 0 |
| DOI | | Optional, e.g. `10.1109/TNNLS.2025.001234` |

On publish:
- Paper is saved to the repository
- If a ResearchProject exists for this journal, the paper is recorded on the project
- All users subscribed to that journal receive a notification
- A pinned news announcement is auto-published: *"Research: New paper published in `[journal]`"*

**Generate a citation**
Click **Cite** on any paper. Opens a modal with BibTeX format:

```bibtex
@article{...
  title   = {Efficient Attention Mechanisms for Low-Resource NLP},
  author  = {Aldamuratov, Zhomart},
  journal = {IEEE Neural Networks},
  year    = {2025},
  doi     = {10.1109/TNNLS.2025.001234}
}
```

**Subscribe / Unsubscribe to a journal**
The Subscribe button on each paper row toggles subscription to that paper's journal.
Subscribed journals show a filled blue button; unsubscribed show an outline button.
When any paper is published in a subscribed journal, you receive a notification.

---

### Projects Tab

**Browse all research projects**
Each project card shows:
- Project / Journal name
- Research topic
- Supervisor full name
- Member count
- Paper count (from recorded publications)
- Your membership status (Joined badge if you're a participant)

**Create a research project**
Click **＋ New Project**:

| Field | Required | Notes |
|---|---|---|
| Journal / Project Name | ✅ | Used as the journal name for publication linking |
| Research Field / Topic | ✅ | Description of the project's focus |

You become the supervisor of the project you create.

**Join a project**
Click **Join Project** on any project card. You are added to the participants list.
The Join button changes to "✓ Joined" (disabled) after joining.
There is no leave-project functionality — membership is permanent.

**Subscribe to a project's journal**
Click **★ Subscribe** on a project card. Works identically to subscribing from the Papers tab.

---

## Notifications

When a paper is published in any journal you subscribe to:

1. A `Notification` is created for you
2. The 🔔 bell in the sidebar shows the unread count
3. Navigate to `/notifications` to read all notifications
4. Click **✓ Mark all as read** to clear them

Notification text format:
```
[JOURNAL] New paper in 'IEEE Neural Networks': Efficient Attention Mechanisms for Low-Resource NLP
```

---

## What a Researcher CANNOT Do (beyond base role)

Research capabilities are entirely additive — they don't remove or restrict anything from
the base role. The only constraint is:

- Cannot deactivate researcher mode once set
- Cannot change research field after activation (field is stored permanently)

---

## API Endpoints (Researcher)

| Method | Endpoint | Body | What it does |
|---|---|---|---|
| GET | `/api/papers` | — | List all papers |
| POST | `/api/papers` | `{title, journal, pages, doi}` | Publish a paper |
| GET | `/api/papers/{id}/cite?format=BIBTEX` | — | Generate citation |
| GET | `/api/projects` | — | List all projects |
| POST | `/api/projects` | `{journal, topic}` | Create a project |
| POST | `/api/projects/{journal}/join` | — | Join a project |
| GET | `/api/subscriptions` | — | List my subscribed journals |
| POST | `/api/subscriptions` | `{journal}` | Subscribe |
| DELETE | `/api/subscriptions/{journal}` | — | Unsubscribe |
| POST | `/api/research/become` | `{field}` | Activate researcher mode |
| GET | `/api/notifications` | — | List notifications |
| DELETE | `/api/notifications` | — | Clear all notifications |
