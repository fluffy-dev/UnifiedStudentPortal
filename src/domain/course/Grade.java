package domain.course;

public record Grade(int firstHalf, int secondHalf, int exam) {
    public static final int PASSING_TOTAL = 50;
    public static final int MIN_ATTESTATION_TOTAL = 30;

    public Grade {
        if (firstHalf < 0 || firstHalf > 30) throw new IllegalArgumentException("att1 0..30");
        if (secondHalf < 0 || secondHalf > 30) throw new IllegalArgumentException("att2 0..30");
        if (exam < 0 || exam > 40) throw new IllegalArgumentException("exam 0..40");
    }

    public int attestationTotal() { return firstHalf + secondHalf; }
    public int total() { return firstHalf + secondHalf + exam; }
    public boolean isAdmittedToExam() { return attestationTotal() >= MIN_ATTESTATION_TOTAL; }
    public boolean isPassing() { return isAdmittedToExam() && total() >= PASSING_TOTAL; }

    // FX: admitted, not passing, exam score 10–19/40 — student may retake exam once
    public boolean isFx() { return isAdmittedToExam() && !isPassing() && exam >= 10 && exam < 20; }

    // needsRetake: admitted to exam, but exam score < 10/40 — must retake whole course
    public boolean needsRetake() { return isAdmittedToExam() && exam < 10; }

    public String letter() {
        if (!isAdmittedToExam()) return "F";
        int t = total();
        if (t >= 90) return "A";
        if (t >= 80) return "B";
        if (t >= 70) return "C";
        if (t >= 50) return "D";
        // Below passing: classify by exam score
        if (exam < 10) return "F";   // [0,9] exam — absolute fail, retake course
        if (exam < 20) return "FX";  // [10,19] exam — conditional fail, retake exam
        return "F";                   // [20,39] exam but total still < 50 — regular fail
    }

    @Override public String toString() {
        return "att1=" + firstHalf + ", att2=" + secondHalf + ", exam=" + exam
                + ", total=" + total() + " (" + letter() + ")"
                + (isAdmittedToExam() ? "" : " [NOT_ADMITTED]");
    }
}
