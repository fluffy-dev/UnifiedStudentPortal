package test;

import application.Result;
import application.usecase.course.RecordMarks;
import application.usecase.course.ViewTranscript;
import application.usecase.research.PublishPaper;
import application.usecase.user.BecomeResearcher;
import domain.course.Course;
import domain.course.Grade;
import domain.repository.*;
import domain.research.JournalName;
import domain.research.ResearchProject;
import domain.service.GpaCalculator;
import domain.service.PaperPublisher;
import domain.shared.IdSequence;
import domain.user.Student;
import domain.user.Teacher;
import infrastructure.logging.RepositoryLogger;
import infrastructure.persistence.inmemory.*;

/** Tests for gaps identified in coverage audit — Grade.isFx, Student guards,
 *  GpaCalculator edge cases, ViewTranscript, RecordMarks idempotency, PaperPublisher. */
public final class CoverageGapTest {

    public static void runAll() {
        // Grade.isFx()
        TestRunner.run("Grade: isFx true for admitted, not-passing, exam 10-19",     CoverageGapTest::testGradeIsFxTrue);
        TestRunner.run("Grade: isFx false when grade is passing (exam in [10,19])",   CoverageGapTest::testGradeIsFxFalseWhenPassing);
        TestRunner.run("Grade: isFx false when not admitted (att < 30)",              CoverageGapTest::testGradeIsFxFalseNotAdmitted);
        TestRunner.run("Grade: letter returns FX for admitted, not-passing, exam 10-19", CoverageGapTest::testGradeLetterFx);

        // Student guard behaviour
        TestRunner.run("Student.undoFail: no-op when failCount is already 0",        CoverageGapTest::testUndoFailAtZero);
        TestRunner.run("Student.undoFail: decrements by exactly 1",                  CoverageGapTest::testUndoFailDecrements);
        TestRunner.run("Student.removeCompletion: safe no-op for unknown course",     CoverageGapTest::testRemoveCompletionUnknown);

        // GpaCalculator edge cases
        TestRunner.run("GPA: null for student with no graded courses",               CoverageGapTest::testGpaNoGradedCourses);
        TestRunner.run("GPA: null when enrolled but nothing graded yet",             CoverageGapTest::testGpaEnrolledButUngraded);
        TestRunner.run("GPA: 0.0 when only failing grades (F)",                      CoverageGapTest::testGpaAllFail);
        TestRunner.run("GPA: FX excluded from GPA (provisional grade)",              CoverageGapTest::testGpaFxExcluded);
        TestRunner.run("GPA: mix of A and F averages to 2.0",                        CoverageGapTest::testGpaMixPassFail);

        // ViewTranscript
        TestRunner.run("ViewTranscript: new student has empty lines and null GPA",   CoverageGapTest::testTranscriptNoEnrollments);
        TestRunner.run("ViewTranscript: ungraded course shows sentinel values",      CoverageGapTest::testTranscriptUngradedLine);
        TestRunner.run("ViewTranscript: graded courses sort before ungraded",        CoverageGapTest::testTranscriptGradedBeforeUngraded);

        // RecordMarks idempotency
        TestRunner.run("RecordMarks: pass → fail removes completion, increments fail",  CoverageGapTest::testReGradePassToFail);
        TestRunner.run("RecordMarks: fail → pass undoes fail, marks completed",         CoverageGapTest::testReGradeFailToPass);
        TestRunner.run("RecordMarks: FX → pass does NOT call undoFail",                 CoverageGapTest::testReGradeFxToPass);
        TestRunner.run("RecordMarks: pass → FX removes completion, no failCount bump",  CoverageGapTest::testReGradePassToFx);
        TestRunner.run("RecordMarks: F → FX undoes previous fail, no new fail",         CoverageGapTest::testReGradeFailToFx);

        // PaperPublisher
        TestRunner.run("PaperPublisher: second paper in same journal notifies again",   CoverageGapTest::testSecondPaperNotifies);
        TestRunner.run("PaperPublisher: paper with no matching project still creates news", CoverageGapTest::testPaperNoProject);
        TestRunner.run("PaperPublisher: two papers produce two distinct news IDs",      CoverageGapTest::testTwoPapersDistinctNewsIds);
    }

    // ── Grade.isFx() ──────────────────────────────────────────────────────────

    private static void testGradeIsFxTrue() {
        Grade g = new Grade(20, 10, 15); // att=30 (admitted), total=45 (<50), exam=15 ∈[10,19]
        Assert.isTrue(g.isAdmittedToExam(), "att=30 must be admitted");
        Assert.isFalse(g.isPassing(), "total=45 is not passing");
        Assert.isTrue(g.isFx(), "exam=15 in [10,19], admitted, not-passing → isFx");
        Assert.equals("FX", g.letter(), "letter must be FX");
    }

    private static void testGradeIsFxFalseWhenPassing() {
        // att=30+30=60, exam=15 → total=75 ≥ 50 → passing; isFx must be false
        Grade g = new Grade(30, 30, 15);
        Assert.isTrue(g.isPassing(), "total=75 is passing");
        Assert.isFalse(g.isFx(), "isPassing() guards against FX when passing");
    }

    private static void testGradeIsFxFalseNotAdmitted() {
        // att=10+10=20 < 30 → not admitted; isFx must be false even though exam∈[10,19]
        Grade g = new Grade(10, 10, 15);
        Assert.isFalse(g.isAdmittedToExam(), "att=20 < 30: not admitted");
        Assert.isFalse(g.isFx(), "not admitted: isFx must be false");
    }

    private static void testGradeLetterFx() {
        Assert.equals("FX", new Grade(20, 10, 15).letter(), "FX letter: att=30, total=45, exam=15");
    }

    // ── Student guards ────────────────────────────────────────────────────────

    private static void testUndoFailAtZero() {
        Student s = Fixtures.student("s");
        Assert.equals(0, s.failCount(), "starts at 0");
        s.undoFail();
        Assert.equals(0, s.failCount(), "undoFail at 0 must not go negative");
    }

    private static void testUndoFailDecrements() {
        Student s = Fixtures.student("s");
        s.recordFail(); s.recordFail();
        s.undoFail();
        Assert.equals(1, s.failCount(), "undoFail decrements by exactly 1");
    }

    private static void testRemoveCompletionUnknown() {
        Student s = Fixtures.student("s");
        Course c = Fixtures.course("X", 3, 30, 99);
        s.removeCompletion(c.id()); // must not throw
        Assert.isFalse(s.completedCourses().contains(c.id()), "removeCompletion of unknown course is no-op");
    }

    // ── GpaCalculator ─────────────────────────────────────────────────────────

    private static void testGpaNoGradedCourses() {
        Double gpa = new GpaCalculator(new InMemoryCourseRepository()).of(Fixtures.student("s"));
        Assert.isTrue(gpa == null, "No courses → GPA must be null (undefined, not 0.0)");
    }

    private static void testGpaEnrolledButUngraded() {
        var repo = new InMemoryCourseRepository();
        Course c = Fixtures.course("Math", 5, 30, 1);
        Student s = Fixtures.student("s");
        c.enroll(s.username());
        repo.save(c);
        Double gpa = new GpaCalculator(repo).of(s);
        Assert.isTrue(gpa == null, "Enrolled but no grade → GPA must be null");
    }

    private static void testGpaAllFail() {
        var repo = new InMemoryCourseRepository();
        Course c = Fixtures.course("Math", 5, 30, 1);
        Student s = Fixtures.student("s");
        c.enroll(s.username());
        c.recordGrade(s.username(), new Grade(10, 10, 20)); // not admitted → F
        repo.save(c);
        Double gpa = new GpaCalculator(repo).of(s);
        Assert.isTrue(gpa != null && gpa == 0.0, "All-fail GPA must be 0.0");
    }

    private static void testGpaFxExcluded() {
        var repo = new InMemoryCourseRepository();
        Course a = Fixtures.course("A", 5, 30, 1);
        Course b = Fixtures.course("B", 5, 30, 2);
        Student s = Fixtures.student("s");
        a.enroll(s.username()); b.enroll(s.username());
        a.recordGrade(s.username(), new Grade(30, 30, 40)); // A → 4.0
        b.recordGrade(s.username(), new Grade(20, 10, 15)); // FX → excluded
        repo.save(a); repo.save(b);
        Double gpa = new GpaCalculator(repo).of(s);
        // Only A counts → GPA = 4.0, not (4.0+0.0)/2 = 2.0
        Assert.isTrue(gpa != null && gpa == 4.0, "FX must be excluded from GPA; only A counts → 4.0");
    }

    private static void testGpaMixPassFail() {
        var repo = new InMemoryCourseRepository();
        Course a = Fixtures.course("A", 5, 30, 1);
        Course b = Fixtures.course("B", 5, 30, 2);
        Student s = Fixtures.student("s");
        a.enroll(s.username()); b.enroll(s.username());
        a.recordGrade(s.username(), new Grade(30, 30, 40)); // A → 4.0
        b.recordGrade(s.username(), new Grade(10, 10, 20)); // F → 0.0
        repo.save(a); repo.save(b);
        Double gpa = new GpaCalculator(repo).of(s);
        Assert.isTrue(gpa != null && gpa == 2.0, "A(4.0) + F(0.0) / 2 = 2.0");
    }

    // ── ViewTranscript ────────────────────────────────────────────────────────

    private static void testTranscriptNoEnrollments() {
        var t = new ViewTranscript(new InMemoryCourseRepository(),
                new GpaCalculator(new InMemoryCourseRepository()))
                .execute(Fixtures.student("s"));
        Assert.isTrue(t.lines().isEmpty(), "No enrollments → empty transcript");
        Assert.isTrue(t.gpa() == null, "No enrollments → GPA must be null");
        Assert.equals(0, t.failCount(), "No enrollments → failCount = 0");
    }

    private static void testTranscriptUngradedLine() {
        var repo = new InMemoryCourseRepository();
        Course c = Fixtures.course("Calc", 5, 30, 1);
        Student s = Fixtures.student("s");
        c.enroll(s.username()); // enrolled, no grade
        repo.save(c);
        var t = new ViewTranscript(repo, new GpaCalculator(repo)).execute(s);
        Assert.equals(1, t.lines().size(), "One enrolled course → one line");
        var line = t.lines().get(0);
        Assert.equals("—", line.letter(), "Ungraded letter is '—'");
        Assert.equals(-1, line.firstHalf(), "Ungraded firstHalf sentinel = -1");
        Assert.isFalse(line.passing(), "Ungraded line is not passing");
    }

    private static void testTranscriptGradedBeforeUngraded() {
        var repo = new InMemoryCourseRepository();
        Course graded   = Fixtures.course("Graded",   5, 30, 1);
        Course ungraded = Fixtures.course("Ungraded", 5, 30, 2);
        Student s = Fixtures.student("s");
        graded.enroll(s.username());
        graded.recordGrade(s.username(), new Grade(30, 30, 30)); // A
        ungraded.enroll(s.username()); // no grade
        repo.save(graded); repo.save(ungraded);
        var lines = new ViewTranscript(repo, new GpaCalculator(repo)).execute(s).lines();
        Assert.equals(2, lines.size(), "Two lines");
        Assert.equals("Graded", lines.get(0).courseName(), "Graded course must come first");
        Assert.equals("Ungraded", lines.get(1).courseName(), "Ungraded course must come last");
    }

    // ── RecordMarks idempotency ───────────────────────────────────────────────

    private record Scenario(UserRepository users, CourseRepository courses,
                             Student student, Teacher teacher, Course course) {
        RecordMarks useCase() {
            return new RecordMarks(courses, users,
                    new RepositoryLogger(new InMemoryLogRepository()));
        }
        Result record(Grade g) {
            return useCase().execute(teacher, course.id(), student.username(), g);
        }
    }

    private static Scenario setup() {
        var users   = new InMemoryUserRepository();
        var courses = new InMemoryCourseRepository();
        Student s = Fixtures.student("eve");
        Teacher t = Fixtures.teacher("bob");
        Course  c = Fixtures.course("Math", 5, 30, 1);
        c.assignTeacher(t.username());
        s.recordEnrollment(c.id(), c.credits());
        c.enroll(s.username());
        users.save(s); users.save(t); courses.save(c);
        return new Scenario(users, courses, s, t, c);
    }

    private static Grade passing()     { return new Grade(30, 30, 30); } // A
    private static Grade plainFail()   { return new Grade(10, 10, 20); } // not admitted → F
    private static Grade fxGrade()     { return new Grade(20, 10, 15); } // FX

    private static void testReGradePassToFail() {
        var sc = setup();
        sc.record(passing());
        Assert.isTrue(sc.student().completedCourses().contains(sc.course().id()), "Pass: in completed");
        sc.record(plainFail());
        Assert.isFalse(sc.student().completedCourses().contains(sc.course().id()), "After pass→fail: removeCompletion called");
        Assert.equals(1, sc.student().failCount(), "After pass→fail: failCount = 1");
    }

    private static void testReGradeFailToPass() {
        var sc = setup();
        sc.record(plainFail());
        Assert.equals(1, sc.student().failCount(), "After F: failCount = 1");
        sc.record(passing());
        Assert.equals(0, sc.student().failCount(), "After fail→pass: undoFail called → 0");
        Assert.isTrue(sc.student().completedCourses().contains(sc.course().id()), "After fail→pass: completed");
    }

    private static void testReGradeFxToPass() {
        var sc = setup();
        sc.record(fxGrade());
        Assert.equals(0, sc.student().failCount(), "FX: failCount stays 0");
        sc.record(passing());
        // FX was never counted → undoFail must NOT fire → failCount stays 0, not -1
        Assert.equals(0, sc.student().failCount(), "FX→pass: undoFail must NOT fire (failCount stays 0)");
        Assert.isTrue(sc.student().completedCourses().contains(sc.course().id()), "FX→pass: completed");
    }

    private static void testReGradePassToFx() {
        var sc = setup();
        sc.record(passing());
        sc.record(fxGrade());
        Assert.isFalse(sc.student().completedCourses().contains(sc.course().id()), "pass→FX: removeCompletion called");
        Assert.equals(0, sc.student().failCount(), "pass→FX: FX does not bump failCount");
    }

    private static void testReGradeFailToFx() {
        var sc = setup();
        sc.record(plainFail());
        Assert.equals(1, sc.student().failCount(), "After F: 1");
        sc.record(fxGrade());
        Assert.equals(0, sc.student().failCount(), "F→FX: undoFail called for previous F → 0");
        Assert.isFalse(sc.student().completedCourses().contains(sc.course().id()), "F→FX: not completed");
    }

    // ── PaperPublisher ────────────────────────────────────────────────────────

    private static void testSecondPaperNotifies() {
        var users = new InMemoryUserRepository();
        var papers = new InMemoryResearchPaperRepository();
        var projects = new InMemoryResearchProjectRepository();
        var notifications = new InMemoryNotificationRepository();
        var news = new InMemoryNewsRepository();
        var logs = new InMemoryLogRepository();
        Student sub = Fixtures.student("sub");
        new BecomeResearcher(new RepositoryLogger(logs)).execute(sub, "AI");
        sub.researcherProfile().subscribe("J1");
        users.save(sub);
        projects.save(new ResearchProject(1, new JournalName("J1"), "AI", sub.username()));
        Student author = Fixtures.student("auth");
        new BecomeResearcher(new RepositoryLogger(logs)).execute(author, "AI");
        users.save(author);
        var publisher = new PaperPublisher(papers, projects, notifications, news, users, new IdSequence());
        var pp = new PublishPaper(publisher, new IdSequence(), new RepositoryLogger(logs));
        pp.execute(author, "Paper 1", "J1", "", 5, null);
        Assert.equals(1, notifications.findFor(sub.username()).size(), "First paper: 1 notification");
        pp.execute(author, "Paper 2", "J1", "", 5, null);
        Assert.equals(2, notifications.findFor(sub.username()).size(), "Second paper: 2 notifications");
    }

    private static void testPaperNoProject() {
        var users = new InMemoryUserRepository();
        var papers = new InMemoryResearchPaperRepository();
        var projects = new InMemoryResearchProjectRepository();
        var notifications = new InMemoryNotificationRepository();
        var news = new InMemoryNewsRepository();
        var logs = new InMemoryLogRepository();
        Student author = Fixtures.student("auth");
        new BecomeResearcher(new RepositoryLogger(logs)).execute(author, "CS");
        users.save(author);
        // No ResearchProject saved for "UnknownJournal"
        var publisher = new PaperPublisher(papers, projects, notifications, news, users, new IdSequence());
        new PublishPaper(publisher, new IdSequence(), new RepositoryLogger(logs))
                .execute(author, "Paper", "UnknownJournal", "", 0, null);
        Assert.isFalse(news.findAllSorted().isEmpty(), "Paper with no project still creates news");
        Assert.isTrue(notifications.findFor(author.username()).isEmpty(), "No subscribers → no notifications");
    }

    private static void testTwoPapersDistinctNewsIds() {
        var users = new InMemoryUserRepository();
        var papers = new InMemoryResearchPaperRepository();
        var projects = new InMemoryResearchProjectRepository();
        var notifications = new InMemoryNotificationRepository();
        var news = new InMemoryNewsRepository();
        var logs = new InMemoryLogRepository();
        Student author = Fixtures.student("auth");
        new BecomeResearcher(new RepositoryLogger(logs)).execute(author, "CS");
        users.save(author);
        var publisher = new PaperPublisher(papers, projects, notifications, news, users, new IdSequence());
        var pp = new PublishPaper(publisher, new IdSequence(), new RepositoryLogger(logs));
        pp.execute(author, "Alpha", "J1", "", 0, null);
        pp.execute(author, "Beta",  "J2", "", 0, null);
        var allNews = news.findAllSorted();
        Assert.equals(2, allNews.size(), "Two papers → two news items");
        Assert.isFalse(allNews.get(0).id() == allNews.get(1).id(), "News items must have distinct IDs");
    }
}
