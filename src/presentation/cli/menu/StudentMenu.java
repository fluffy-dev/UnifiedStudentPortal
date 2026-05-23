package presentation.cli.menu;

import application.Result;
import application.usecase.course.DropCourse;
import application.usecase.course.EnrollInCourse;
import application.usecase.course.ViewTranscript;
import application.usecase.library.BorrowBook;
import application.usecase.library.ReturnBook;
import application.usecase.organization.CreateOrganization;
import application.usecase.organization.JoinOrganization;
import application.usecase.user.RateTeacher;
import domain.course.CourseId;
import domain.repository.CourseRepository;
import domain.repository.NotificationRepository;
import domain.repository.OrganizationRepository;
import domain.shared.Username;
import domain.user.Student;
import presentation.cli.Console;

import java.util.ArrayList;
import java.util.List;

public final class StudentMenu extends Menu {
    private final Student student;
    private final CourseRepository courses;
    private final OrganizationRepository orgs;
    private final NotificationRepository notifications;
    private final EnrollInCourse enroll;
    private final DropCourse drop;
    private final BorrowBook borrow;
    private final ReturnBook returnBook;
    private final ViewTranscript viewTranscript;
    private final RateTeacher rateTeacher;
    private final CreateOrganization createOrg;
    private final JoinOrganization joinOrg;
    private final BecomeResearcherAction becomeResearcher;
    private final ResearcherMenuExtension researcherMenu;
    private final CommonMenuActions common;

    public StudentMenu(Console console, Student student,
                       CourseRepository courses, OrganizationRepository orgs,
                       NotificationRepository notifications,
                       EnrollInCourse enroll, DropCourse drop,
                       BorrowBook borrow, ReturnBook returnBook,
                       ViewTranscript viewTranscript,
                       RateTeacher rateTeacher,
                       CreateOrganization createOrg, JoinOrganization joinOrg,
                       BecomeResearcherAction becomeResearcher,
                       ResearcherMenuExtension researcherMenu,
                       CommonMenuActions common) {
        super(console);
        this.student = student;
        this.courses = courses;
        this.orgs = orgs;
        this.notifications = notifications;
        this.enroll = enroll;
        this.drop = drop;
        this.borrow = borrow;
        this.returnBook = returnBook;
        this.viewTranscript = viewTranscript;
        this.rateTeacher = rateTeacher;
        this.createOrg = createOrg;
        this.joinOrg = joinOrg;
        this.becomeResearcher = becomeResearcher;
        this.researcherMenu = researcherMenu;
        this.common = common;
    }

    @Override protected String title() { return "=== STUDENT MENU (" + student.username() + ") ==="; }

    @Override protected List<MenuItem> items() {
        List<MenuItem> items = new ArrayList<>();
        if (!student.isResearcher())
            items.add(new MenuItem("Become a researcher", () -> becomeResearcher.run(student)));
        items.add(new MenuItem("View enrolled courses",   this::viewEnrolled));
        items.add(new MenuItem("View available courses",  this::viewAvailable));
        items.add(new MenuItem("Enroll in a course",      this::enrollInteractive));
        items.add(new MenuItem("Drop a course",           this::dropInteractive));
        items.add(new MenuItem("View transcript",         this::renderTranscript));
        items.add(new MenuItem("View schedule",           this::viewSchedule));
        items.add(new MenuItem("Borrow a book",           this::borrowInteractive));
        items.add(new MenuItem("Return a book",           this::returnInteractive));
        items.add(new MenuItem("View inbox",              common::viewInbox));
        items.add(new MenuItem("Send message",            common::sendMessageInteractive));
        items.add(new MenuItem("View news",               common::viewNews));
        items.add(new MenuItem("Submit help request",     common::submitRequestInteractive));
        items.add(new MenuItem("Submit IT order",         common::createOrderInteractive));
        items.add(new MenuItem("View notifications",      this::viewNotifications));
        items.add(new MenuItem("Rate a teacher",          this::rateInteractive));
        items.add(new MenuItem("View organizations",      this::listOrgs));
        items.add(new MenuItem("Join organization",       this::joinOrgInteractive));
        items.add(new MenuItem("Create organization",     this::createOrgInteractive));
        items.addAll(researcherMenu.itemsFor(student));
        return items;
    }

    private void viewEnrolled() {
        var enrolled = courses.findAll().stream()
                .filter(c -> c.hasStudent(student.username())).toList();
        if (enrolled.isEmpty()) { console.println("No enrollments."); return; }
        enrolled.forEach(c -> {
            String grade = c.gradeOf(student.username())
                    .map(g -> g.letter() + " (" + g.total() + "/100)").orElse("in progress");
            console.println("  " + c.name() + " [" + c.id() + "] — " + grade);
        });
    }

    private void viewAvailable() {
        var all = courses.findAll();
        if (all.isEmpty()) { console.println("No courses available."); return; }
        all.forEach(c -> console.println("  [" + c.id() + "] " + c.name()
                + " | " + c.credits() + " cr | " + c.remainingSeats() + "/" + c.capacity().max() + " seats"
                + (c.isFull() ? " FULL" : "")));
    }

    private void enrollInteractive() {
        viewAvailable();
        Result r = enroll.execute(student, new CourseId(console.readLine("Course ID:")));
        console.println(r.message());
    }

    private void dropInteractive() {
        viewEnrolled();
        Result r = drop.execute(student, new CourseId(console.readLine("Course ID:")));
        console.println(r.message());
    }

    private void renderTranscript() {
        ViewTranscript.Transcript t = viewTranscript.execute(student);
        console.println("\n=== TRANSCRIPT — " + t.fullName() + " ===");
        console.println("Degree: " + t.degreeType() + " | Year: " + t.year() + " | Fails: " + t.failCount());
        if (t.lines().isEmpty()) { console.println("  No courses yet."); }
        else t.lines().forEach(l -> {
            String score = l.firstHalf() >= 0
                    ? l.firstHalf() + "+" + l.secondHalf() + "+" + l.exam() + "=" + l.total()
                    : "ungraded";
            console.println("  " + l.courseName() + ": " + l.letter() + " (" + score + ")");
        });
        String gpaStr = t.gpa() != null ? String.format("%.2f / 4.0", t.gpa()) : "N/A (no grades yet)";
        console.println("GPA: " + gpaStr);
    }

    private void viewSchedule() {
        var myCourses = courses.findAll().stream()
                .filter(c -> c.hasStudent(student.username()) && !c.lessons().isEmpty()).toList();
        if (myCourses.isEmpty()) { console.println("No lessons scheduled."); return; }
        console.println("\n--- LESSON SCHEDULE ---");
        myCourses.forEach(c -> {
            console.println("  " + c.name() + ":");
            c.lessons().forEach(l -> console.println("    " + l.slot().day() + " " + l.slot().time()
                    + " | " + l.type() + " | Room: " + l.room()));
        });
    }

    private void borrowInteractive() {
        Result r = borrow.execute(student, console.readLine("Book title:"));
        console.println(r.message());
    }

    private void returnInteractive() {
        Result r = returnBook.execute(student, console.readLine("Book title:"));
        console.println(r.message());
    }

    private void viewNotifications() {
        var list = notifications.findFor(student.username());
        if (list.isEmpty()) { console.println("No notifications."); return; }
        list.forEach(n -> console.println("  " + n.at().toLocalDate() + " — " + n.text()));
    }

    private void rateInteractive() {
        String teacher = console.readLine("Teacher username:");
        int rating = console.readInt("Rating (1-10):");
        Result r = rateTeacher.execute(student, new Username(teacher), rating);
        console.println(r.message());
    }

    private void listOrgs() {
        var all = orgs.findAll();
        if (all.isEmpty()) { console.println("No organizations."); return; }
        all.forEach(o -> console.println("  " + o));
    }

    private void joinOrgInteractive() {
        Result r = joinOrg.execute(student, console.readLine("Organization name:"));
        console.println(r.message());
    }

    private void createOrgInteractive() {
        Result r = createOrg.execute(student, console.readLine("Organization name:"));
        console.println(r.message());
    }
}
