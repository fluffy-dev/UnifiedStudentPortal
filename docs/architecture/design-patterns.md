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
enrollment or passes to the next rule. Adding a new enrollment constraint requires adding one class
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
