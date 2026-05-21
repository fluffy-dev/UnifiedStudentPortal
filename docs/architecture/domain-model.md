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
