# Demo Accounts

The system is pre-seeded with demo accounts for every role. Credentials are defined in
`src/bootstrap/DataSeeder.java` and may change between versions — check that file for
current usernames and passwords.

## Roles

| Role | Description |
|------|-------------|
| **Admin** | Full system access: user management, logs, academic reports |
| **Teacher (Professor)** | Teaches courses, records marks, manages attestations |
| **Teacher (Senior Lector)** | Same capabilities as Professor |
| **Dean** | Department oversight, handles complaints, approves requests |
| **Manager** | Creates courses, processes requests and IT orders |
| **Student (Bachelor)** | Enrolls in courses, views grades and transcript, library, messaging |
| **Librarian** | Manages book catalogue, processes borrowing and returns |
| **Tech Support** | Handles IT orders queue |

!!! note
    Credentials are seeded data subject to change. See `src/bootstrap/DataSeeder.java`
    for the current username/password pairs.
