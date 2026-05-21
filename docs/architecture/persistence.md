# Persistence

## Overview

The system persists all state as JSON files with zero external libraries. Every component
in the persistence stack was built from scratch.

## JSON Parser / Writer

**Package:** `infrastructure.persistence.json`

A hand-rolled JSON parser and writer that handles all serialization needs. Key classes:

- `JsonValue` — sealed type hierarchy: `JsonObject`, `JsonArray`, `JsonString`, `JsonNumber`, `JsonBoolean`, `JsonNull`
- `JsonParser` — recursive descent parser, converts raw JSON strings to `JsonValue` trees
- `JsonObjectBuilder` — fluent builder for constructing JSON objects

```java
JsonValue obj = JsonObjectBuilder.create()
    .put("username", user.username().value())
    .put("role",     user.getClass().getSimpleName())
    .put("gpa",      student.gpa())
    .build();
```

## JsonFileDatabase

**Package:** `infrastructure.persistence.database`

`JsonFileDatabase` is the low-level persistence adapter. It reads from and writes to a
JSON array file on disk. Each call to `save()` rewrites the entire file atomically.

On startup, `AppContext` constructs one `JsonFileDatabase` instance per entity type,
backed by a file in the configured data directory.

## Custom ORM

**Package:** `infrastructure.persistence.orm`

The generic `Repository<T, ID>` provides a CRUD interface backed by `JsonFileDatabase`.
It manages an in-memory cache of all entities, applies `EntityMapper<T>` for
serialization/deserialization, and flushes changes to disk on every write.

Entity mappers are in `infrastructure.persistence.mapper.*` — one per domain entity.

## QueryBuilder

`QueryBuilder<T, ID>` wraps the in-memory collection with a fluent filtering and
sorting API. Concrete repositories use it to avoid raw stream boilerplate.

```java
// Finding all courses a student is enrolled in
return query()
    .where(c -> c.enrolledStudents().contains(username))
    .findAll();
```

## In-Memory Repositories

**Package:** `infrastructure.persistence.inmemory`

Test-only implementations of all repository interfaces that store data in `HashMap`s.
Used exclusively by the 52-test Java suite — no disk I/O, no file setup, fast execution.
