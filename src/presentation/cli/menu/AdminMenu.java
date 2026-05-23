package presentation.cli.menu;

import application.Result;
import application.usecase.admin.CreateStudent;
import application.usecase.admin.DeleteUser;
import application.usecase.admin.GenerateTopResearcherNews;
import domain.enums.DegreeType;
import domain.enums.Faculty;
import domain.enums.Gender;
import domain.repository.LogRepository;
import domain.repository.UserRepository;
import domain.shared.Username;
import domain.user.Admin;
import presentation.cli.Console;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class AdminMenu extends Menu {
    private final Admin admin;
    private final UserRepository users;
    private final LogRepository logs;
    private final DeleteUser deleteUser;
    private final CreateStudent createStudent;
    private final GenerateTopResearcherNews topResearcher;
    private final CommonMenuActions common;

    public AdminMenu(Console console, Admin admin, UserRepository users, LogRepository logs,
                     DeleteUser deleteUser, CreateStudent createStudent,
                     GenerateTopResearcherNews topResearcher, CommonMenuActions common) {
        super(console);
        this.admin = admin;
        this.users = users;
        this.logs = logs;
        this.deleteUser = deleteUser;
        this.createStudent = createStudent;
        this.topResearcher = topResearcher;
        this.common = common;
    }

    @Override protected String title() { return "=== ADMIN MENU (" + admin.username() + ") ==="; }

    @Override protected List<MenuItem> items() {
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("View all users",               this::listUsers));
        items.add(new MenuItem("Create student",               this::createStudentInteractive));
        items.add(new MenuItem("Delete a user",                this::deleteUserInteractive));
        items.add(new MenuItem("View all logs",                this::listLogs));
        items.add(new MenuItem("Generate top researcher news", this::topResearcherNews));
        items.add(new MenuItem("View inbox",                   common::viewInbox));
        items.add(new MenuItem("Send message",                 common::sendMessageInteractive));
        return items;
    }

    private void listUsers() {
        users.findAll().forEach(u -> console.println("  " + u));
    }

    private void createStudentInteractive() {
        String username  = console.readLine("Username:");
        String password  = console.readLine("Password:");
        String firstName = console.readLine("First name:");
        String lastName  = console.readLine("Last name:");
        String email     = console.readLine("Email:");
        console.println("Faculties: SITE, SEOGI, SG, KMA, ISE, BS");
        String facultyStr = console.readLine("Faculty:");
        console.println("Degrees: BACHELOR, MASTER, DOCTORATE");
        String degreeStr  = console.readLine("Degree:");
        int year          = console.readInt("Study year:");
        try {
            Faculty    faculty = Faculty.valueOf(facultyStr.toUpperCase());
            DegreeType degree  = DegreeType.valueOf(degreeStr.toUpperCase());
            var s = createStudent.execute(admin.username(), firstName, lastName, username, password,
                    Gender.MALE, LocalDate.of(2000, 1, 1), email, faculty, degree, year);
            console.println("Student created: " + s.username());
        } catch (IllegalArgumentException e) {
            console.println("Invalid faculty or degree: " + e.getMessage());
        }
    }

    private void deleteUserInteractive() {
        listUsers();
        String name = console.readLine("Username to delete:");
        Result r = deleteUser.execute(admin.username(), new Username(name));
        console.println(r.message());
    }

    private void listLogs() {
        var all = logs.findAll();
        if (all.isEmpty()) { console.println("No logs."); return; }
        all.forEach(e -> console.println(e.toString()));
    }

    private void topResearcherNews() {
        Result r = topResearcher.execute(admin.username());
        console.println(r.message());
    }
}
