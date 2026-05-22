package application.usecase.course;

import domain.course.Course;
import domain.course.Grade;
import domain.repository.CourseRepository;
import domain.service.GpaCalculator;
import domain.user.Student;

import java.util.ArrayList;
import java.util.List;

public final class ViewTranscript {
    /**
     * One row in the transcript. Includes full score breakdown so the frontend can display
     * att1/att2/exam columns alongside the total and letter grade.
     * firstHalf/secondHalf/exam are -1 when no grade has been recorded yet.
     */
    public record TranscriptLine(
            String courseName, String letter, int total,
            int firstHalf, int secondHalf, int exam,
            boolean passing) {}

    public record Transcript(String fullName, String degreeType, int year,
                             int failCount, double gpa, List<TranscriptLine> lines) {}

    private final CourseRepository courses;
    private final GpaCalculator gpa;

    public ViewTranscript(CourseRepository courses, GpaCalculator gpa) {
        this.courses = courses;
        this.gpa = gpa;
    }

    public Transcript execute(Student student) {
        List<TranscriptLine> lines = new ArrayList<>();

        // Scan ALL courses to find ones where this student is enrolled.
        // This is intentionally authoritative: we look at course membership, not the
        // student's own enrolled-set, because seeded data may populate courses directly
        // without going through the enrollment use case.
        for (Course c : courses.findAll()) {
            if (!c.hasStudent(student.username())) continue;
            Grade g = c.gradeOf(student.username()).orElse(null);
            if (g == null) {
                lines.add(new TranscriptLine(c.name(), "—", 0, -1, -1, -1, false));
            } else {
                lines.add(new TranscriptLine(
                        c.name(), g.letter(), g.total(),
                        g.firstHalf(), g.secondHalf(), g.exam(),
                        g.isPassing()));
            }
        }

        // Sort: graded courses first by total descending; ungraded rows last, alphabetically.
        // The ungraded sentinel is firstHalf == -1 (never a valid Grade component value).
        lines.sort((a, b) -> {
            boolean aUngraded = a.firstHalf() == -1;
            boolean bUngraded = b.firstHalf() == -1;
            if (aUngraded && !bUngraded) return 1;          // a goes after b
            if (!aUngraded && bUngraded) return -1;          // a goes before b
            if (aUngraded)               return a.courseName().compareToIgnoreCase(b.courseName());
            return Integer.compare(b.total(), a.total());    // both graded: desc total
        });

        return new Transcript(student.name().full(), student.degreeType().name(),
                student.studyYear(), student.failCount(), gpa.of(student), lines);
    }
}
