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
     * has a finalised grade. Returns null when no finalisable grades exist yet.
     *
     * FX grades are EXCLUDED: FX is a provisional conditional-fail where the student
     * may retake the exam — it is not a final outcome. Including FX at 0.0 would
     * misrepresent a student who has one A and one pending FX as having a 2.0 GPA.
     * This matches the RecordMarks policy which does not increment failCount for FX.
     *
     * Scale: A=4.0, B=3.0, C=2.0, D=1.0, F=0.0 | FX=excluded
     */
    public Double of(Student student) {
        int count = 0;
        double sum = 0;
        for (Course c : courses.findAll()) {
            if (!c.hasStudent(student.username())) continue;
            Grade g = c.gradeOf(student.username()).orElse(null);
            if (g == null || g.isFx()) continue; // skip ungraded and provisional FX
            sum += letterToGpa(g.letter());
            count++;
        }
        if (count == 0) return null; // no finalisable grades yet — GPA is undefined, not 0.0
        return Math.round((sum / count) * 100.0) / 100.0;
    }

    private static double letterToGpa(String letter) {
        return switch (letter) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default  -> 0.0; // F (all variants)
        };
    }
}
