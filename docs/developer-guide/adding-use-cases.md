# Adding a Use Case

Follow these steps to add a new use case to the system. The example adds a
`RateStudent` use case that lets a Dean rate a student.

## Step 1: Create the use case class

Create `src/application/usecase/user/RateStudent.java`:

```java
package application.usecase.user;

import application.Result;
import domain.repository.UserRepository;
import domain.shared.Username;
import domain.user.Dean;

public final class RateStudent {
    private final UserRepository userRepository;

    public RateStudent(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Result execute(Dean dean, Username studentUsername, int rating) {
        if (rating < 1 || rating > 5) return Result.failure("Rating must be 1–5.");
        return userRepository.findByUsername(studentUsername)
            .map(student -> {
                // domain logic here
                return Result.success("Student rated.");
            })
            .orElse(Result.failure("Student not found."));
    }
}
```

## Step 2: Wire it in AppContext

Open `src/bootstrap/AppContext.java`. Find the section where use cases are constructed
and add:

```java
public final RateStudent rateStudent;

// in the constructor, after userRepository is available:
this.rateStudent = new RateStudent(userRepository);
```

## Step 3: Expose via CLI (optional)

Open the relevant menu class (e.g. `src/presentation/cli/menu/DeanMenu.java`).
Add a menu item and delegate to `ctx.rateStudent.execute(...)`.

## Step 4: Expose via REST (optional)

Open the appropriate controller or create a new one. Add a method:

```java
public HttpResponse rateStudent(HttpRequest request) {
    Dean dean = (Dean) RequestContext.current();
    String username = str(request.body(), "username");
    int rating = intVal(request.body(), "rating");
    Result result = ctx.rateStudent.execute(dean, new Username(username), rating);
    return resultToResponse(result);
}
```

Then register the route in `src/presentation/rest/server/RestServer.java`:

```java
router.register(Route.of(HttpMethod.POST, "/api/students/rate", ctrl::rateStudent, Dean.class));
```

## Step 5: Write a test

Create `src/test/RateStudentTest.java` using the in-memory repositories:

```java
package test;

import application.Result;
import application.usecase.user.RateStudent;
import infrastructure.persistence.inmemory.InMemoryUserRepository;
import domain.shared.Username;

public class RateStudentTest {
    public static void main(String[] args) {
        InMemoryUserRepository users = new InMemoryUserRepository();
        // seed a Dean and a Student, then:
        RateStudent useCase = new RateStudent(users);
        // assert result.success() == true, etc.
        System.out.println("RateStudentTest PASSED");
    }
}
```

Run: `bash scripts/test.sh`
