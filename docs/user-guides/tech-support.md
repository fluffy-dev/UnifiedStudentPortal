# Tech Support Guide

## Overview

**TechSupport** manages the IT order queue. They accept incoming orders, work on them, and
mark them complete. TechSupport does **not** create orders — they process what users submit.

---

## Demo Credentials

| Username | Password | Name |
|---|---|---|
| `bekasyl` | bekasyl | Bekasyl Amanshayev, TechSupport |

---

## Sidebar Navigation

- **Library**: Library
- **Communication**: Messages, News, **IT Orders**, Requests
- 🔔 Notifications bell

---

## What TechSupport Can Do

### IT Orders — Primary Responsibility

The IT Orders page is the TechSupport's main workspace. They see **all orders from all users**.

**Order queue**
All orders are listed with:

| Column | Description |
|---|---|
| Description | What the user needs |
| User | Full name + @username of the requester |
| Status | NEW · ACCEPTED · DONE |
| Created | Date submitted |
| Actions | Accept or Complete buttons (TechSupport only) |

Use the status filter (NEW / ACCEPTED / DONE) to focus on what needs attention.
Use the search box to find orders by description or requester name.

**Accept an order**
When a new order comes in, click **Accept** to take ownership of it.
The order moves to ACCEPTED status and your username is recorded as the executor.
This signals to the requester that their issue is being worked on.

**Complete an order**
Once resolved, click **Complete** on an accepted order.
The order moves to DONE status. The requester sees their order as resolved.

**Order lifecycle**

```
NEW (submitted by user)
 → ACCEPTED (TechSupport takes it)
   → DONE (TechSupport completes it)
```

Orders cannot be re-opened once completed.

!!! info "No new orders"
    The **New Order** button is hidden for TechSupport. They process orders, not create them.

---

### Messages

Full messaging: inbox + sent with body view, compose to anyone.
TechSupport often uses messages to ask clarifying questions before accepting an order,
or to inform users of resolution steps.

---

### News

- Read and comment on news
- **Publish** news posts (e.g. scheduled maintenance notices, WiFi upgrade announcements)
- Cannot pin posts (Manager/Dean only)

---

### Library

Browse, borrow, and return own books.

---

### Requests

Submit and view own help requests. Cannot process others' requests.

---

## What TechSupport CANNOT Do

- ❌ Create IT orders (only process them)
- ❌ Record grades or manage courses
- ❌ Approve/reject student requests (Manager/Dean only)
- ❌ Create or delete user accounts (Admin only)
- ❌ View audit logs or generate reports (Admin only)
- ❌ Add/remove books (Librarian only)
- ❌ Pin news posts (Manager/Dean only)

---

## API Endpoints (TechSupport-specific)

| Method | Endpoint | What it does |
|---|---|---|
| GET | `/api/orders` | View **all** orders (others see only own) |
| PUT | `/api/orders/{id}/accept` | Accept an order |
| PUT | `/api/orders/{id}/complete` | Complete an accepted order |

---

## Typical Workflows

### Morning triage

1. **IT Orders** → filter by **NEW**
2. Review descriptions — group by type (hardware, software, access, other)
3. Accept all that can be addressed today
4. For unclear requests: compose a message to the requester asking for details

### Closing out a resolved issue

1. **IT Orders** → filter by **ACCEPTED**
2. Find the resolved order
3. Click **Complete**
4. Optionally send the requester a message confirming resolution

### Publishing maintenance notice before downtime

1. **News** → **Publish**
2. Write casual but informative title (e.g. "⚠️ Scheduled Maintenance Thursday Night")
3. Describe: what's affected, when, duration, workarounds
4. **Important**: mention submission deadlines if the portal will be down
