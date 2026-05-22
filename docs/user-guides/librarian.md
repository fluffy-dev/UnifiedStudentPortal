# Librarian Guide

## Overview

**The Librarian** manages the physical book collection. They can add new books, remove books
from the catalogue, and return any book regardless of who borrowed it. All other users
can borrow (up to 3 books) and return their own books, but only the Librarian has
full catalogue management and the ability to check in books borrowed by others.

---

## Demo Credentials

| Username | Password | Name |
|---|---|---|
| `assylzhan` | assylzhan | Assylzhan Izbassar, Librarian |

---

## Sidebar Navigation

- **Library**: Library
- **Communication**: Messages, News, Requests, IT Orders
- 🔔 Notifications bell

---

## What a Librarian Can Do

### Library — Full Catalogue Management

**Browse all books**
All books listed with title, author, availability status (Available / Borrowed), and
current borrower's username if borrowed.

**Add a book**
Click **＋ Add Book**. Required: Title. Author is optional but recommended.
The book is immediately available for borrowing.

**Remove a book**
Click the red **Remove** button on any book row. A confirmation dialog appears before deletion.
Books can be removed whether available or currently borrowed.

**Return any book**
The Librarian can return any book borrowed by any user — the Return button is always
visible for borrowed books when logged in as Librarian. Regular users can only return
books they borrowed themselves.

This allows the Librarian to check in books dropped off at the desk by students who
may not have internet access at that moment.

**Borrow a book**
Librarians can also borrow books for personal use (same 3-book limit applies).

---

### Messages

Full messaging: compose, view inbox/sent, read full body on click.

---

### News

- **Publish** news posts (Librarians are employees)
- Typical posts: new book arrivals, library hours updates, renovation notices, return reminders
- Cannot pin posts (Manager/Dean only)

---

### Requests

- Submit own requests (any type)
- View own submitted requests
- Cannot process other users' requests

---

### IT Orders

Submit IT orders, track own order status. Examples: printer repair, printing system maintenance.

---

## What a Librarian CANNOT Do

- ❌ Create or modify courses (Manager only)
- ❌ Record grades (Teacher only)
- ❌ Approve/reject student requests (Manager/Dean only)
- ❌ Create or delete user accounts (Admin only)
- ❌ View audit logs or generate reports (Admin only)
- ❌ Accept/complete IT orders (TechSupport only)
- ❌ Pin news posts (Manager/Dean only)

---

## API Endpoints (Librarian-specific)

| Method | Endpoint | What it does |
|---|---|---|
| POST | `/api/books` | Add a book `{title, author}` |
| DELETE | `/api/books/{title}` | Remove a book |
| POST | `/api/books/{title}/return` | Return any borrowed book |
| POST | `/api/books/{title}/borrow` | Borrow a book (user-level, shared) |

---

## Typical Workflows

### Adding a batch of new arrivals

1. **Library** → **＋ Add Book**
2. Enter title and author for each new title
3. Publish a **News** post announcing the new arrivals with a list of titles
4. Books are immediately available for borrowing

### Checking in returned books at the desk

1. Student brings a book to the library desk
2. Librarian opens **Library**, finds the book by title using the search box
3. Clicks **Return** — the book is marked available immediately
4. The student's borrower record is cleared

### Removing a damaged or lost book

1. **Library** → find the book
2. Click **Remove** → confirm the dialog
3. Book disappears from the catalogue entirely
