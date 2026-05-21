# Running Tests

## Java Test Suite

The project ships 52 tests with no test framework — pure Java with hand-rolled assertions.

```bash
bash scripts/test.sh
```

Expected output: all 52 tests pass, no `FAIL` lines.

Tests live in `src/test/`. They use `infrastructure.persistence.inmemory.*` repositories
to avoid disk I/O — each test constructs the in-memory adapters directly and passes them
to use cases.

### What is covered

- All 46 use cases — success paths and key failure paths
- Enrollment rule chain — all 6 rules individually
- Domain entity invariants
- Value object validation
- Citation formatter output (both formats)
- GPA and attestation computation

### Running a single test class

```bash
javac -cp src src/test/SomeTest.java && java -cp src test.SomeTest
```

## Frontend Test Suite

119 tests using **Vitest** and **React Testing Library**.

```bash
cd frontend
npm test
```

Or in watch mode:

```bash
npm run test:watch
```

### What is covered

- Every page component renders without crashing
- API call functions (`api/index.js`) are mocked — tests verify UI behaviour on success and error states
- Form validation
- Role-gated navigation (Sidebar shows/hides links correctly per role)
- Toast and Modal component interactions
