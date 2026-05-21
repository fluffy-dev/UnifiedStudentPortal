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
