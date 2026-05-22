package domain.service;

import domain.course.Course;
import domain.course.Grade;
import domain.repository.CourseRepository;
import domain.user.Student;

public final class GpaCalculator {
    private final CourseRepository courses;

    public GpaCalculator(CourseRepository courses) { this.courses = courses; }

    /**
     * Calculates GPA on a 4.0 scale by scanning all courses for ones where this student
     * has a recorded grade. Only graded courses are included (ungraded enrollments are skipped).
     * Scale: A=4.0, B=3.0, C=2.0, D=1.0, FX/F=0.0
     */
    public double of(Student student) {
        int count = 0;
        double sum = 0;
        for (Course c : courses.findAll()) {
            if (!c.hasStudent(student.username())) continue;
            Grade g = c.gradeOf(student.username()).orElse(null);
            if (g == null) continue;
            sum += letterToGpa(g.letter());
            count++;
        }
        return count == 0 ? 0.0 : Math.round((sum / count) * 100.0) / 100.0;
    }

    private static double letterToGpa(String letter) {
        return switch (letter) {
            case "A"  -> 4.0;
            case "B"  -> 3.0;
            case "C"  -> 2.0;
            case "D"  -> 1.0;
            default   -> 0.0; // F, FX — zero points
        };
    }
}
