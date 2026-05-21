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
