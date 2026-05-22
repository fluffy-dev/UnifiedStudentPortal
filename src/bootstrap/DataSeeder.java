package bootstrap;

import domain.course.*;
import domain.enums.*;
import domain.library.*;
import domain.messaging.*;
import domain.research.*;
import domain.shared.*;
import domain.user.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class DataSeeder {
    private final AppContext ctx;

    public DataSeeder(AppContext ctx) { this.ctx = ctx; }

    public void seedIfEmpty() {
        if (!ctx.userRepository.findAll().isEmpty()) return;
        seedUsers();
        seedCourses();
        seedGrades();
        seedBooks();
        seedMessages();
        seedNews();
        seedRequests();
        seedOrders();
        seedResearch();
    }

    // ── USERS ────────────────────────────────────────────────────────────────

    private void seedUsers() {
        save(new Admin(u("rauan"), h("rauan"),
                n("Rauan","Assetov"), Gender.MALE, d(1988,3,12), e("rauan@uni.edu"), Faculty.SITE));

        save(new Manager(u("serdar"), h("serdar"),
                n("Serdar","Dundar"), Gender.MALE, d(1984,7,20), e("serdar@uni.edu"), Faculty.SITE,
                m(2400), LocalDate.of(2019,9,1), "INS-001", ManagerPosition.OR));

        Teacher zhomart = new Teacher(u("zhomart"), h("zhomart"),
                n("Zhomart","Aldamuratov"), Gender.MALE, d(1978,5,3), e("zhomart@uni.edu"), Faculty.SITE,
                m(2800), LocalDate.of(2016,9,1), "INS-002", "PhD", TeacherPosition.PROFESSOR);
        save(zhomart);

        save(new Teacher(u("anel"), h("anel"),
                n("Anel","Yeraliyeva"), Gender.FEMALE, d(1985,11,17), e("anel@uni.edu"), Faculty.SITE,
                m(2200), LocalDate.of(2020,2,1), "INS-003", "MSc", TeacherPosition.SENIOR_LECTOR));

        save(new Teacher(u("marat"), h("marat"),
                n("Marat","Ospanov"), Gender.MALE, d(1980,6,9), e("marat@uni.edu"), Faculty.SEOGI,
                m(2600), LocalDate.of(2017,9,1), "INS-007", "PhD", TeacherPosition.PROFESSOR));

        save(new Dean(u("bayan"), h("bayan"),
                n("Bayan","Assetova"), Gender.FEMALE, d(1971,4,8), e("bayan@uni.edu"), Faculty.SITE,
                m(3800), LocalDate.of(2014,1,1), "INS-004", "Doctor of Sciences"));

        save(new TechSupport(u("bekasyl"), h("bekasyl"),
                n("Bekasyl","Amanshayev"), Gender.MALE, d(1995,8,30), e("bekasyl@uni.edu"), Faculty.SITE,
                m(1700), LocalDate.of(2022,3,1), "INS-005"));

        save(new Librarian(u("assylzhan"), h("assylzhan"),
                n("Assylzhan","Izbassar"), Gender.MALE, d(1992,1,25), e("assylzhan@uni.edu"), Faculty.SITE,
                m(1500), LocalDate.of(2021,9,1), "INS-006"));

        // Students
        String[][] students = {
            {"dariya",   "Dariya",   "Yergaliyeva", "F", "2003", "6","14", "2"},
            {"bexultan", "Bexultan", "Yeraliyev",   "M", "2002", "2","28", "3"},
            {"assel",    "Assel",    "Bazhikey",    "F", "2004","10", "5", "1"},
            {"arman",    "Arman",    "Seitkali",    "M", "2003", "3","20", "2"},
            {"zarina",   "Zarina",   "Bekova",      "F", "2002", "9","11", "3"},
            {"dias",     "Dias",     "Nurmagambetov","M","2004", "1","30", "1"},
            {"aizat",    "Aizat",    "Sultanova",   "F", "2003","12", "5", "2"},
            {"alibek",   "Alibek",   "Dzhaksybekov","M", "2001", "7","17", "4"},
            {"madina",   "Madina",   "Ospanova",    "F", "2004", "5", "3", "1"},
            {"yerlan",   "Yerlan",   "Abenov",      "M", "2003", "8","25", "2"},
            {"sanzhar",  "Sanzhar",  "Mukhanov",    "M", "2001","11","14", "4"},
            {"kamila",   "Kamila",   "Dzhunusova",  "F", "2004", "2","19", "1"},
            {"temirlan", "Temirlan", "Sabenov",     "M", "2002", "4", "8", "3"},
        };
        for (String[] s : students) {
            Gender g = s[3].equals("M") ? Gender.MALE : Gender.FEMALE;
            int yr = Integer.parseInt(s[7]);
            save(new Student(u(s[0]), h(s[0]),
                    n(s[1], s[2]), g,
                    d(Integer.parseInt(s[4]), Integer.parseInt(s[5]), Integer.parseInt(s[6])),
                    e(s[0]+"@uni.edu"), Faculty.SITE, DegreeType.BACHELOR, yr));
        }

        save(new GraduateStudent(u("nurasyl"), h("nurasyl"),
                n("Nurasyl","Dulat"), Gender.MALE, d(2000,9,18), e("nurasyl@uni.edu"), Faculty.SITE,
                DegreeType.MASTER, 1));
        save(new GraduateStudent(u("aigerim"), h("aigerim"),
                n("Aigerim","Bekzhanova"), Gender.FEMALE, d(1999,3,7), e("aigerim@uni.edu"), Faculty.SITE,
                DegreeType.DOCTORATE, 2));
    }

    // ── COURSES ──────────────────────────────────────────────────────────────

    private void seedCourses() {
        // 1 Discrete Mathematics
        Course dm = c("Discrete Mathematics", 5, DisciplineType.MAJOR, 60);
        dm.assignTeacher(u("zhomart"));
        dm.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "09:00", "Aud 401"));
        dm.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY, "09:00", "Aud 401"));
        dm.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,    "10:00", "Lab 2"));
        dm.addLesson(l(LessonType.OFFICE_HOURS, WeekDay.THURSDAY,"14:00","Rm 305"));
        for (String s : new String[]{"dariya","bexultan","arman","zarina","aizat","madina","yerlan"}) dm.enroll(u(s));
        ctx.courseRepository.save(dm);

        // 2 OOP
        Course oop = c("Object-Oriented Programming", 5, DisciplineType.MAJOR, 45);
        oop.addPrerequisite(dm.id());
        oop.assignTeacher(u("zhomart")); oop.assignTeacher(u("anel"));
        oop.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "10:00","Aud 401"));
        oop.addLesson(l(LessonType.LECTURE,  WeekDay.THURSDAY, "10:00","Aud 401"));
        oop.addLesson(l(LessonType.PRACTICE, WeekDay.TUESDAY,  "13:00","Lab 1"));
        oop.addLesson(l(LessonType.PRACTICE, WeekDay.SATURDAY, "09:00","Lab 1"));
        oop.addLesson(l(LessonType.OFFICE_HOURS, WeekDay.WEDNESDAY,"15:00","Rm 210"));
        for (String s : new String[]{"dariya","bexultan","assel","arman","dias","kamila","temirlan","sanzhar"}) oop.enroll(u(s));
        ctx.courseRepository.save(oop);

        // 3 Algorithms
        Course algo = c("Algorithms & Data Structures", 4, DisciplineType.MAJOR, 35);
        algo.addPrerequisite(dm.id());
        algo.assignTeacher(u("anel"));
        algo.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "11:00","Aud 402"));
        algo.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY, "11:00","Aud 402"));
        algo.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,    "13:00","Lab 3"));
        algo.addLesson(l(LessonType.OFFICE_HOURS,WeekDay.TUESDAY,"16:00","Rm 210"));
        for (String s : new String[]{"bexultan","zarina","alibek","sanzhar","nurasyl","temirlan"}) algo.enroll(u(s));
        ctx.courseRepository.save(algo);

        // 4 Database Systems
        Course db = c("Database Systems", 4, DisciplineType.MAJOR, 35);
        db.addPrerequisite(oop.id());
        db.assignTeacher(u("zhomart"));
        db.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "14:00","Aud 403"));
        db.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY, "14:00","Lab 2"));
        db.addLesson(l(LessonType.OFFICE_HOURS,WeekDay.FRIDAY,"15:00","Rm 305"));
        for (String s : new String[]{"bexultan","alibek","sanzhar","nurasyl","zarina"}) db.enroll(u(s));
        ctx.courseRepository.save(db);

        // 5 Computer Networks
        Course nets = c("Computer Networks", 3, DisciplineType.MAJOR, 30);
        nets.assignTeacher(u("marat"));
        nets.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY,"13:00","Aud 401"));
        nets.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,   "11:00","Lab 4"));
        for (String s : new String[]{"nurasyl","aigerim","alibek","yerlan"}) nets.enroll(u(s));
        ctx.courseRepository.save(nets);

        // 6 Linear Algebra
        Course la = c("Linear Algebra", 5, DisciplineType.MAJOR, 50);
        la.assignTeacher(u("zhomart"));
        la.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "13:00","Aud 402"));
        la.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY, "13:00","Aud 402"));
        la.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY,  "11:00","Lab 2"));
        la.addLesson(l(LessonType.EXAM,     WeekDay.SATURDAY,  "10:00","Hall A"));
        for (String s : new String[]{"dariya","assel","aizat","madina","kamila","dias"}) la.enroll(u(s));
        ctx.courseRepository.save(la);

        // 7 Technical English
        Course eng = c("Technical English", 2, DisciplineType.MINOR, 80);
        eng.assignTeacher(u("anel"));
        eng.addLesson(l(LessonType.PRACTICE,WeekDay.MONDAY,  "15:00","Rm 112"));
        eng.addLesson(l(LessonType.PRACTICE,WeekDay.FRIDAY,  "09:00","Rm 112"));
        for (String s : new String[]{"dariya","assel","nurasyl","arman","aizat","madina","dias","kamila","yerlan"}) eng.enroll(u(s));
        ctx.courseRepository.save(eng);

        // 8 Intro to AI & ML
        Course ai = c("Introduction to AI & ML", 3, DisciplineType.MINOR, 30);
        ai.assignTeacher(u("zhomart"));
        ai.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "16:00","Aud 401"));
        ai.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY, "16:00","Lab 1"));
        for (String s : new String[]{"assel","nurasyl","aigerim","arman","yerlan","temirlan"}) ai.enroll(u(s));
        ctx.courseRepository.save(ai);

        // 9 Web Development
        Course web = c("Web Development", 3, DisciplineType.FREE, 40);
        web.assignTeacher(u("anel"));
        web.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "16:00","Aud 403"));
        web.addLesson(l(LessonType.PRACTICE, WeekDay.WEDNESDAY, "16:00","Lab 1"));
        web.addLesson(l(LessonType.OFFICE_HOURS,WeekDay.FRIDAY, "16:00","Rm 210"));
        for (String s : new String[]{"assel","dariya","dias","kamila","madina","arman"}) web.enroll(u(s));
        ctx.courseRepository.save(web);

        // 10 Software Engineering
        Course se = c("Software Engineering", 4, DisciplineType.MAJOR, 35);
        se.addPrerequisite(oop.id());
        se.assignTeacher(u("marat"));
        se.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "14:00","Aud 403"));
        se.addLesson(l(LessonType.PRACTICE, WeekDay.WEDNESDAY, "14:00","Lab 3"));
        se.addLesson(l(LessonType.OFFICE_HOURS,WeekDay.TUESDAY,"17:00","Rm 310"));
        for (String s : new String[]{"alibek","sanzhar","zarina","temirlan","bexultan"}) se.enroll(u(s));
        ctx.courseRepository.save(se);

        // 11 Operating Systems
        Course os = c("Operating Systems", 4, DisciplineType.MAJOR, 30);
        os.addPrerequisite(algo.id());
        os.assignTeacher(u("marat"));
        os.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "11:00","Aud 402"));
        os.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY, "11:00","Lab 4"));
        for (String s : new String[]{"alibek","sanzhar","aigerim","nurasyl"}) os.enroll(u(s));
        ctx.courseRepository.save(os);

        // 12 Statistics & Probability
        Course stats = c("Statistics & Probability", 3, DisciplineType.MAJOR, 40);
        stats.assignTeacher(u("marat"));
        stats.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY,"10:00","Aud 401"));
        stats.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,   "14:00","Lab 2"));
        for (String s : new String[]{"dariya","arman","aizat","dias","yerlan","madina","kamila"}) stats.enroll(u(s));
        ctx.courseRepository.save(stats);

        // 13 Cybersecurity Fundamentals
        Course sec = c("Cybersecurity Fundamentals", 3, DisciplineType.MINOR, 25);
        sec.assignTeacher(u("marat"));
        sec.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "15:00","Aud 402"));
        sec.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY, "15:00","Lab 3"));
        for (String s : new String[]{"bexultan","alibek","nurasyl","aigerim","sanzhar"}) sec.enroll(u(s));
        ctx.courseRepository.save(sec);

        // 14 Mobile Development
        Course mob = c("Mobile Development", 3, DisciplineType.FREE, 30);
        mob.addPrerequisite(oop.id());
        mob.assignTeacher(u("anel"));
        mob.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "10:00","Aud 403"));
        mob.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY,  "13:00","Lab 1"));
        for (String s : new String[]{"arman","kamila","aizat","temirlan","dias"}) mob.enroll(u(s));
        ctx.courseRepository.save(mob);

        // 15 Calculus I
        Course calc = c("Calculus I", 5, DisciplineType.MAJOR, 70);
        calc.assignTeacher(u("zhomart"));
        calc.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "08:00","Aud 401"));
        calc.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY, "08:00","Aud 401"));
        calc.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,    "08:00","Lab 2"));
        for (String s : new String[]{"dias","madina","kamila","aizat","arman","yerlan"}) calc.enroll(u(s));
        ctx.courseRepository.save(calc);

        // 16 Cloud Computing
        Course cloud = c("Cloud Computing", 3, DisciplineType.MINOR, 25);
        cloud.assignTeacher(u("marat"));
        cloud.addLesson(l(LessonType.LECTURE,  WeekDay.WEDNESDAY,"15:00","Aud 403"));
        cloud.addLesson(l(LessonType.PRACTICE, WeekDay.FRIDAY,   "15:00","Lab 4"));
        for (String s : new String[]{"nurasyl","aigerim","alibek","sanzhar"}) cloud.enroll(u(s));
        ctx.courseRepository.save(cloud);

        // 17 Data Mining
        Course dm2 = c("Data Mining", 3, DisciplineType.MINOR, 20);
        dm2.addPrerequisite(stats.id());
        dm2.assignTeacher(u("zhomart"));
        dm2.addLesson(l(LessonType.LECTURE,  WeekDay.TUESDAY,  "17:00","Aud 402"));
        dm2.addLesson(l(LessonType.PRACTICE, WeekDay.THURSDAY, "17:00","Lab 1"));
        for (String s : new String[]{"aigerim","nurasyl","zarina"}) dm2.enroll(u(s));
        ctx.courseRepository.save(dm2);

        // 18 Project Management
        Course pm = c("Project Management", 2, DisciplineType.FREE, 50);
        pm.assignTeacher(u("anel"));
        pm.addLesson(l(LessonType.LECTURE, WeekDay.TUESDAY,"18:00","Aud 401"));
        for (String s : new String[]{"alibek","sanzhar","zarina","temirlan","bexultan","dariya","assel"}) pm.enroll(u(s));
        ctx.courseRepository.save(pm);

        // 19 Computer Architecture
        Course arch = c("Computer Architecture", 4, DisciplineType.MAJOR, 30);
        arch.assignTeacher(u("marat"));
        arch.addLesson(l(LessonType.LECTURE,  WeekDay.MONDAY,    "15:00","Aud 402"));
        arch.addLesson(l(LessonType.PRACTICE, WeekDay.WEDNESDAY, "15:00","Lab 3"));
        for (String s : new String[]{"bexultan","alibek","temirlan","sanzhar"}) arch.enroll(u(s));
        ctx.courseRepository.save(arch);

        // 20 Ethics in Computing
        Course ethics = c("Ethics in Computing", 2, DisciplineType.MINOR, 60);
        ethics.assignTeacher(u("anel"));
        ethics.addLesson(l(LessonType.LECTURE,WeekDay.FRIDAY,"17:00","Aud 401"));
        for (String s : new String[]{"dariya","assel","arman","kamila","dias","madina","yerlan","aizat"}) ethics.enroll(u(s));
        ctx.courseRepository.save(ethics);
    }

    // ── GRADES ───────────────────────────────────────────────────────────────

    private void seedGrades() {
        for (var c : ctx.courseRepository.findAll()) {
            switch (c.name()) {
                case "Discrete Mathematics" -> {
                    c.recordGrade(u("dariya"),   new Grade(27,25,30)); // 82 B
                    c.recordGrade(u("bexultan"), new Grade(18,16,12)); // FX
                    c.recordGrade(u("arman"),    new Grade(28,26,35)); // 89 B
                    c.recordGrade(u("zarina"),   new Grade(25,24,33)); // 82 B
                    c.recordGrade(u("aizat"),    new Grade(29,28,38)); // 95 A
                    c.recordGrade(u("yerlan"),   new Grade(15,14, 8)); // att=29 not admitted → F
                    c.recordGrade(u("madina"),   new Grade(20,19,11)); // FX: att=39, exam=11
                }
                case "Object-Oriented Programming" -> {
                    c.recordGrade(u("bexultan"), new Grade(29,28,38)); // 95 A
                    c.recordGrade(u("assel"),    new Grade(13,14, 0)); // not admitted F
                    c.recordGrade(u("dariya"),   new Grade(24,22,26)); // 72 C
                    c.recordGrade(u("arman"),    new Grade(26,24,30)); // 80 B
                    c.recordGrade(u("dias"),     new Grade(20,18,15)); // FX: exam=15
                    c.recordGrade(u("temirlan"), new Grade(28,27,35)); // 90 A
                    c.recordGrade(u("sanzhar"),  new Grade(22,20,22)); // 64 D
                    c.recordGrade(u("kamila"),   new Grade(25,23,28)); // 76 C
                }
                case "Algorithms & Data Structures" -> {
                    c.recordGrade(u("bexultan"), new Grade(22,20, 5)); // needsRetake
                    c.recordGrade(u("nurasyl"),  new Grade(26,25,30)); // 81 B
                    c.recordGrade(u("zarina"),   new Grade(25,23,27)); // 75 C
                    c.recordGrade(u("alibek"),   new Grade(29,28,39)); // 96 A
                    c.recordGrade(u("sanzhar"),  new Grade(20,18,22)); // 60 D
                    c.recordGrade(u("temirlan"), new Grade(15,13, 0)); // not admitted F
                }
                case "Database Systems" -> {
                    c.recordGrade(u("nurasyl"),  new Grade(28,27,36)); // 91 A
                    c.recordGrade(u("alibek"),   new Grade(27,26,35)); // 88 B
                    c.recordGrade(u("zarina"),   new Grade(23,21,25)); // 69 D
                    c.recordGrade(u("sanzhar"),  new Grade(19,17,14)); // FX: exam=14
                }
                case "Linear Algebra" -> {
                    c.recordGrade(u("dariya"),   new Grade(20,18,14)); // 52 D
                    c.recordGrade(u("assel"),    new Grade(24,22,29)); // 75 C
                    c.recordGrade(u("aizat"),    new Grade(28,27,37)); // 92 A
                    c.recordGrade(u("madina"),   new Grade(18,16,13)); // FX: exam=13
                    c.recordGrade(u("kamila"),   new Grade(25,23,30)); // 78 C
                }
                case "Software Engineering" -> {
                    c.recordGrade(u("alibek"),   new Grade(28,27,36)); // 91 A
                    c.recordGrade(u("sanzhar"),  new Grade(24,22,28)); // 74 C
                    c.recordGrade(u("zarina"),   new Grade(26,24,32)); // 82 B
                    c.recordGrade(u("temirlan"), new Grade(22,20,25)); // 67 D
                }
                case "Statistics & Probability" -> {
                    c.recordGrade(u("dariya"),   new Grade(25,23,28)); // 76 C
                    c.recordGrade(u("arman"),    new Grade(27,26,34)); // 87 B
                    c.recordGrade(u("aizat"),    new Grade(29,28,38)); // 95 A
                    c.recordGrade(u("yerlan"),   new Grade(20,18,22)); // 60 D
                    c.recordGrade(u("kamila"),   new Grade(16,15, 0)); // not admitted F
                }
                case "Technical English" -> {
                    c.recordGrade(u("dariya"),   new Grade(28,27,36)); // 91 A
                    c.recordGrade(u("assel"),    new Grade(25,24,32)); // 81 B
                    c.recordGrade(u("nurasyl"),  new Grade(27,26,35)); // 88 B
                    c.recordGrade(u("arman"),    new Grade(24,22,29)); // 75 C
                    c.recordGrade(u("aizat"),    new Grade(26,25,33)); // 84 B
                    c.recordGrade(u("madina"),   new Grade(22,20,26)); // 68 D
                    c.recordGrade(u("yerlan"),   new Grade(20,18,23)); // 61 D
                }
            }
            ctx.courseRepository.save(c);
        }
    }

    // ── BOOKS ────────────────────────────────────────────────────────────────

    private void seedBooks() {
        String[][] books = {
            {"Clean Code","Robert C. Martin"},
            {"Effective Java","Joshua Bloch"},
            {"Design Patterns","Gang of Four"},
            {"Introduction to Algorithms","Cormen, Leiserson, Rivest, Stein"},
            {"Computer Networks","Andrew Tanenbaum"},
            {"Database System Concepts","Silberschatz, Korth, Sudarshan"},
            {"The Pragmatic Programmer","Hunt & Thomas"},
            {"Artificial Intelligence: A Modern Approach","Russell & Norvig"},
            {"Clean Architecture","Robert C. Martin"},
            {"Domain-Driven Design","Eric Evans"},
            {"Designing Data-Intensive Applications","Martin Kleppmann"},
            {"The Algorithm Design Manual","Steven Skiena"},
            {"Structure and Interpretation of Computer Programs","Abelson & Sussman"},
            {"Code Complete","Steve McConnell"},
            {"Refactoring","Martin Fowler"},
            {"Operating System Concepts","Silberschatz & Galvin"},
            {"Computer Organization and Design","Patterson & Hennessy"},
            {"Cryptography and Network Security","Stallings"},
            {"Pattern Recognition and Machine Learning","Bishop"},
            {"Deep Learning","Goodfellow, Bengio, Courville"},
            {"The Mythical Man-Month","Frederick Brooks"},
            {"Head First Design Patterns","Freeman & Robson"},
            {"JavaScript: The Good Parts","Douglas Crockford"},
            {"You Don't Know JS","Kyle Simpson"},
            {"Pro Git","Scott Chacon"},
            {"Docker Deep Dive","Nigel Poulton"},
            {"Kubernetes in Action","Luksa"},
            {"Spring in Action","Craig Walls"},
            {"Python Crash Course","Eric Matthes"},
            {"Learning SQL","Alan Beaulieu"},
        };
        String[] borrowed = {"nurasyl","assel","bexultan","aigerim","alibek",null,null,null,
                             null,null,"arman",null,null,"sanzhar",null,null,null,null,null,null,
                             null,null,null,null,null,null,null,null,null,null};
        for (int i = 0; i < books.length; i++) {
            Book bk = new Book(new BookId(ctx.bookIds.next()), books[i][0], books[i][1]);
            if (borrowed[i] != null) bk.lendTo(u(borrowed[i]));
            ctx.bookRepository.save(bk);
        }
    }

    // ── MESSAGES ─────────────────────────────────────────────────────────────

    private void seedMessages() {
        // dariya ↔ zhomart
        msg(u("dariya"),u("zhomart"),"Lab 2 extended?",
            "hi! heard the lab 2 deadline was extended?? our group just finished but wanted to double check",
            UrgencyLevel.MEDIUM, ago(2,9,30));
        msg(u("zhomart"),u("dariya"),"Re: Lab 2 extended?",
            "hey dariya yes pushed to friday 23:59. upload to portal as usual. good work finishing!",
            UrgencyLevel.LOW, ago(2,11,0));
        msg(u("dariya"),u("zhomart"),"Discrete Math midterm question",
            "sorry to bug again but can we use a formula sheet on the midterm? or strictly closed book?",
            UrgencyLevel.MEDIUM, ago(7,20,15));
        msg(u("zhomart"),u("dariya"),"Re: Discrete Math midterm question",
            "closed book, but you can bring ONE A4 sheet handwritten notes. announce this Monday in class too",
            UrgencyLevel.LOW, ago(7,21,0));

        // bexultan ↔ anel
        msg(u("bexultan"),u("anel"),"Practice 4 — cyclic graph output",
            "Good day Anel! The expected output for cyclic graphs in Practice 4 doesnt match my implementation. "
            +"My DFS returns nodes in different order. Is there a required traversal order?",
            UrgencyLevel.MEDIUM, ago(4,14,15));
        msg(u("anel"),u("bexultan"),"Re: Practice 4 — cyclic graph output",
            "Hi Bexultan! Yes there was a typo in the handout — we expect lexicographic neighbor ordering. "
            +"Updated version on Teams, see slide 34. Good catch!",
            UrgencyLevel.LOW, ago(4,16,40));
        msg(u("bexultan"),u("anel"),"Algorithms retake request",
            "Hello Anel, I saw my algorithms grade is an F. Is there any way to do a retake or extra credit? "
            +"I had a medical situation during the exam week. I have a doctor's note.",
            UrgencyLevel.HIGH, ago(1,10,0));
        msg(u("anel"),u("bexultan"),"Re: Algorithms retake request",
            "Hi Bexultan, I'm sorry to hear that. Please submit the doctor's note to the Academic Affairs office "
            +"(Room 205) and file a formal appeal. I'll support your case.",
            UrgencyLevel.MEDIUM, ago(1,14,0));

        // assel ↔ anel
        msg(u("assel"),u("anel"),"web dev project topic??",
            "hi anel!! missed thursday bc was sick 😭 did we pick project topics already or still open? "
            +"wanted to do react + interactive maps",
            UrgencyLevel.LOW, ago(1,20,30));
        msg(u("anel"),u("assel"),"Re: web dev project topic??",
            "Hi Assel! Hope you're better :) Topics finalized Thursday. React + maps is great — check Teams "
            +"pinned post for the list. If yours isn't taken you're good. Deadline Sunday midnight!",
            UrgencyLevel.LOW, ago(1,21,10));
        msg(u("assel"),u("anel"),"web project — partner question",
            "can we work in pairs or is it individual? i want to team up with kamila if allowed",
            UrgencyLevel.LOW, ago(0,9,0));
        msg(u("anel"),u("assel"),"Re: web project — partner question",
            "Pairs are allowed! Just make sure both names are in the submission and the scope is larger than solo. "
            +"Let me know your combined topic.",
            UrgencyLevel.LOW, ago(0,10,30));

        // nurasyl ↔ zhomart
        msg(u("nurasyl"),u("zhomart"),"Thesis topic consultation",
            "Dear Professor Aldamuratov, I would like to request a consultation about my master's thesis. "
            +"Interested in attention mechanisms for Kazakh NLP. Available next week?",
            UrgencyLevel.MEDIUM, ago(3,11,0));
        msg(u("zhomart"),u("nurasyl"),"Re: Thesis topic consultation",
            "Hi Nurasyl, excellent direction! Meet me Monday 15:00 room 305. Bring a 1-2 page proposal "
            +"with problem statement and methodology sketch.",
            UrgencyLevel.LOW, ago(3,14,30));
        msg(u("nurasyl"),u("zhomart"),"Proposal draft attached",
            "Professor, I've prepared the proposal draft as discussed. Key contributions: "
            +"(1) sparse attention for morphological richness, (2) cross-lingual transfer from Turkish. "
            +"Please review when you have a moment. Thank you.",
            UrgencyLevel.MEDIUM, ago(1,16,0));
        msg(u("zhomart"),u("nurasyl"),"Re: Proposal draft attached",
            "Nurasyl this is very solid! Point 2 on Turkish transfer is novel. "
            +"Minor comment: clarify your evaluation metrics in section 3. Otherwise ready to proceed. Great work.",
            UrgencyLevel.LOW, ago(1,18,30));

        // serdar → staff
        msg(u("serdar"),u("anel"),"ADS Syllabus — accreditation",
            "Dear Anel, please submit updated syllabus and learning outcomes for Algorithms & Data Structures "
            +"by end of Friday. Use standard template from faculty portal. Thank you.",
            UrgencyLevel.HIGH, ago(6,10,0));
        msg(u("serdar"),u("zhomart"),"Course materials request",
            "Zhomart, could you please send me an updated list of reference materials for Discrete Math and "
            +"Linear Algebra? Accreditation committee meeting is next Tuesday.",
            UrgencyLevel.HIGH, ago(5,9,0));
        msg(u("zhomart"),u("serdar"),"Re: Course materials request",
            "Serdar, I'll send everything by Thursday EOD. I'll include the updated bibliography and "
            +"assessment rubrics as well.",
            UrgencyLevel.MEDIUM, ago(5,11,30));

        // bayan → teachers
        msg(u("bayan"),u("zhomart"),"Faculty meeting Friday 14:00",
            "Zhomart, mandatory faculty meeting this Friday at 14:00, Conference Room B. "
            +"Agenda: grade distribution review, semester-end procedures, accreditation prep.",
            UrgencyLevel.HIGH, ago(1,9,0));
        msg(u("bayan"),u("anel"),"Faculty meeting Friday 14:00",
            "Anel, please attend the faculty meeting this Friday at 14:00 in Conference Room B. "
            +"Bring summary of your course grade distributions.",
            UrgencyLevel.HIGH, ago(1,9,5));
        msg(u("bayan"),u("marat"),"Faculty meeting Friday 14:00",
            "Marat, mandatory faculty meeting Friday 14:00 Conference Room B. "
            +"Please prepare progress update on your courses.",
            UrgencyLevel.HIGH, ago(1,9,10));

        // arman ↔ zhomart
        msg(u("arman"),u("zhomart"),"Discrete math — graph theory question",
            "hi professor! in the graph coloring problem from hw3 — does the chromatic number need to be "
            +"minimal or just valid? the problem statement is a bit ambiguous",
            UrgencyLevel.MEDIUM, ago(5,21,0));
        msg(u("zhomart"),u("arman"),"Re: Discrete math — graph theory question",
            "Good question Arman. We want the CHROMATIC number i.e. minimum colors needed. "
            +"Hint: think about the clique number as a lower bound.",
            UrgencyLevel.LOW, ago(6,8,30));

        // zarina ↔ anel
        msg(u("zarina"),u("anel"),"algorithms — missing lecture notes",
            "Hi Anel! I missed Monday's lecture (family emergency). Could you share the notes "
            +"or recording if available? Also was there homework assigned?",
            UrgencyLevel.MEDIUM, ago(3,19,0));
        msg(u("anel"),u("zarina"),"Re: algorithms — missing lecture notes",
            "Hi Zarina, sorry to hear that! Slides posted on Teams — 'Week 9 - Dynamic Programming'. "
            +"Homework 5 assigned, due Sunday. Hope everything is okay with your family.",
            UrgencyLevel.LOW, ago(3,20,15));

        // alibek ↔ marat
        msg(u("alibek"),u("marat"),"Software Engineering — team project team",
            "Hello Marat! Our team of 4 is formed: me, sanzhar, zarina, temirlan. "
            +"Can we work on a task management web app as our project? "
            +"Would love your thoughts on the scope.",
            UrgencyLevel.MEDIUM, ago(8,12,0));
        msg(u("marat"),u("alibek"),"Re: Software Engineering — team project",
            "Sounds great Alibek! Task management is perfect scope. "
            +"Requirements: REST API + frontend, authentication, at least 5 entity types. "
            +"Submit a 1-page spec by next Wednesday.",
            UrgencyLevel.MEDIUM, ago(8,15,0));

        // days and temirlan questions
        msg(u("dias"),u("anel"),"web dev — CSS framework allowed?",
            "quick question — are we allowed to use Tailwind or Bootstrap for the web project "
            +"or must it be vanilla CSS only?",
            UrgencyLevel.LOW, ago(2,17,30));
        msg(u("anel"),u("dias"),"Re: web dev — CSS framework allowed?",
            "Dias — frameworks are allowed! Just make sure you understand what you're using, "
            +"we may ask questions during demo. Tailwind is fine.",
            UrgencyLevel.LOW, ago(2,18,0));

        // kamila → zhomart (OOP confusion)
        msg(u("kamila"),u("zhomart"),"OOP lab 4 — inheritance question",
            "Professor can abstract classes have constructors in Java? I thought they couldn't "
            +"but my code compiled with one and now im confused",
            UrgencyLevel.LOW, ago(3,23,0));
        msg(u("zhomart"),u("kamila"),"Re: OOP lab 4 — inheritance question",
            "Yes they can! Abstract classes can have constructors — they're called by subclass constructors "
            +"via super(). They just can't be instantiated directly. Good exploration!",
            UrgencyLevel.LOW, ago(4,8,0));

        // madina → anel (nervous first-year)
        msg(u("madina"),u("anel"),"first midterm — im so nervous",
            "hi Anel.. i know this is a bit personal but i'm really anxious about the linear algebra midterm. "
            +"i've been studying but i freeze during exams. any advice?",
            UrgencyLevel.LOW, ago(10,21,0));
        msg(u("anel"),u("madina"),"Re: first midterm — im so nervous",
            "Madina, I appreciate you sharing that. Exam anxiety is very common. "
            +"Practice timed conditions at home — do past papers under a timer. "
            +"Also: if you need extra support, we have a student wellness center (Room 108). "
            +"You're prepared. Trust your work 💙",
            UrgencyLevel.LOW, ago(10,22,15));

        // aigerim ↔ zhomart (doctoral)
        msg(u("aigerim"),u("zhomart"),"Dissertation chapter 2 feedback",
            "Dear Zhomart, I have completed Chapter 2 (Related Works) and attached it for your review. "
            +"I'm planning to start the experimental setup next week. "
            +"Could we schedule a meeting to discuss the methodology before I proceed?",
            UrgencyLevel.MEDIUM, ago(5,10,0));
        msg(u("zhomart"),u("aigerim"),"Re: Dissertation chapter 2 feedback",
            "Aigerim, excellent work on ch.2 — very thorough survey. "
            +"Suggestion: add the 2024 ACL papers on instruction tuning, they're directly relevant. "
            +"Meeting Tuesday 11:00 or 16:00 — which works?",
            UrgencyLevel.MEDIUM, ago(5,14,0));

        // sanzhar → marat (OS question)
        msg(u("sanzhar"),u("marat"),"OS — page replacement question",
            "yo Marat, quick one — for the assignment on page replacement, "
            +"do we implement BOTH LRU and FIFO or just pick one?",
            UrgencyLevel.LOW, ago(2,18,45));
        msg(u("marat"),u("sanzhar"),"Re: OS — page replacement question",
            "Both please — the comparison is the point. Show performance difference with the same reference string.",
            UrgencyLevel.LOW, ago(2,19,0));

        // yerlan → zhomart (concerning)
        msg(u("yerlan"),u("zhomart"),"missed classes — explanation",
            "Professor Zhomart, I wanted to explain my absences. I've been working night shifts "
            +"to support my family and it's been affecting my attendance and the discrete math exam. "
            +"Is there any way to discuss my situation?",
            UrgencyLevel.HIGH, ago(4,22,0));
        msg(u("zhomart"),u("yerlan"),"Re: missed classes — explanation",
            "Yerlan, thank you for being honest with me. Please come to office hours Thursday 14:00 "
            +"and we'll discuss options. Your academic success matters — let's find a solution together.",
            UrgencyLevel.MEDIUM, ago(5,9,0));

        // bekasyl announcements
        msg(u("bekasyl"),u("rauan"),"Server rack replacement — approval needed",
            "Rauan, we need to replace the server rack in the data center. "
            +"Three vendors submitted quotes ranging from 2.1M to 2.8M tenge. "
            +"Can you review and approve the procurement request?",
            UrgencyLevel.HIGH, ago(3,9,0));
        msg(u("rauan"),u("bekasyl"),"Re: Server rack replacement",
            "Bekasyl, reviewed the quotes. Go with the middle option (vendor 2, 2.4M). "
            +"Better warranty terms. I'll sign the purchase order today.",
            UrgencyLevel.HIGH, ago(3,11,30));

        // assylzhan → bayan
        msg(u("assylzhan"),u("bayan"),"Library renovation proposal",
            "Dear Dean Assetova, I've prepared a proposal for the library reading room renovation. "
            +"The current furniture is deteriorating and we have student complaints. "
            +"Estimated cost: 800K tenge. Please review at your convenience.",
            UrgencyLevel.MEDIUM, ago(7,10,0));
        msg(u("bayan"),u("assylzhan"),"Re: Library renovation proposal",
            "Assylzhan, reviewed the proposal. Good justification. "
            +"Submit it through the capital budget request form on the faculty portal. "
            +"I'll add my recommendation.",
            UrgencyLevel.LOW, ago(7,15,0));

        // temirlan → anel (honest about struggling)
        msg(u("temirlan"),u("anel"),"struggling with algorithms content",
            "hi anel. so i got a failing grade on algorithms which is really discouraging. "
            +"im trying hard but dynamic programming is just not clicking. "
            +"would extra office hours help or is there a tutor service?",
            UrgencyLevel.MEDIUM, ago(6,20,0));
        msg(u("anel"),u("temirlan"),"Re: struggling with algorithms content",
            "Hi Temirlan, I really appreciate you reaching out. DP is genuinely hard! "
            +"Come Wednesday 15:00 — extra session just for DP problems. "
            +"Also check the CS tutoring center (Room 115, Mon/Wed 18:00-20:00).",
            UrgencyLevel.LOW, ago(6,21,0));
    }

    // ── NEWS ─────────────────────────────────────────────────────────────────

    private void seedNews() {
        // 1
        News n1 = news("Welcome to Fall 2025 Semester! 🎓",
            "Dear students and faculty, welcome back to SITE University for the Fall 2025 semester. "
            +"Academic year begins September 1st. All course rosters are now available on the portal. "
            +"Please review schedules and confirm enrollment by Friday. Student IDs available in Room 101. "
            +"We wish everyone a productive and successful semester.",
            u("serdar"), true);
        n1.addComment(Comment.now(u("dariya"),  "finally it's starting!! so hyped for this sem ngl 🔥"));
        n1.addComment(Comment.now(u("assel"),   "hi everyone!! first sem here lowkey nervous but let's gooo 🥲"));
        n1.addComment(Comment.now(u("bexultan"),"year 3 let's get it. last stretch before internship apps 💪"));
        n1.addComment(Comment.now(u("arman"),   "anyone else's schedule look absolutely cooked? i have 8am calculus 💀"));
        n1.addComment(Comment.now(u("zhomart"), "Welcome everyone! Office hours posted on course pages. Don't hesitate to reach out."));
        n1.addComment(Comment.now(u("dias"),    "1st year let's go!! excited and terrified at the same time"));
        n1.addComment(Comment.now(u("nurasyl"), "New graduate student here. Looking forward to a productive semester 🙏"));
        ctx.newsRepository.save(n1);

        // 2
        News n2 = news("Final Examination Schedule — Fall 2025",
            "Dear students, final exams run December 15–27. All examinations in designated halls — "
            +"check individual timetable on portal. Conflicts must be reported to Academic Affairs (Room 205) within 48 hours. "
            +"Academic integrity strictly enforced. Good luck.",
            u("bayan"), false);
        n2.addComment(Comment.now(u("dariya"),  "discrete math and OOP back to back on the 17th?? who approved this schedule 💀"));
        n2.addComment(Comment.now(u("bexultan"),"at least its not 8am. praying the exam hall is heated this year"));
        n2.addComment(Comment.now(u("assel"),   "wait when is web dev exam? i don't see it on my portal"));
        n2.addComment(Comment.now(u("anel"),    "Assel — Web Dev is project submission, no written exam! Due Dec 21st."));
        n2.addComment(Comment.now(u("assel"),   "OHHHH okay phew. thank youuuu 🙏🙏"));
        n2.addComment(Comment.now(u("arman"),   "the gap between my exams is 4 hours. living the dream"));
        n2.addComment(Comment.now(u("kamila"),  "does anyone want to form a study group for linear algebra?? pls"));
        ctx.newsRepository.save(n2);

        // 3
        News n3 = news("OOP Lab 3 Deadline Extended 🙌",
            "hey everyone — lab 3 deadline is pushed to Friday 23:59 (was Wednesday). "
            +"a few groups had grading system issues so giving everyone extra time. "
            +"please test on lab machines before submitting — 'works on my machine' is not an excuse lol. "
            +"come to office hours Wed 15:00 if stuck. good luck!",
            u("anel"), false);
        n3.addComment(Comment.now(u("dariya"),  "THANK YOU ANEL omg we were literally pulling an all-nighter 😭😭😭"));
        n3.addComment(Comment.now(u("assel"),   "bestie behavior fr 🙏💯"));
        n3.addComment(Comment.now(u("bexultan"),"submitted already lol. y'all should've started earlier 😂"));
        n3.addComment(Comment.now(u("kamila"),  "bexultan you're actually not normal"));
        n3.addComment(Comment.now(u("dias"),    "this is the only time procrastination paid off ngl"));
        n3.addComment(Comment.now(u("anel"),    "haha bexultan well done! others — use the time wisely 😄"));
        n3.addComment(Comment.now(u("temirlan"),"still doesn't compile 🙃 guess im coming wednesday"));
        ctx.newsRepository.save(n3);

        // 4
        News n4 = news("Research: New Publication on Transformer Optimization 🎉",
            "Excited to share our paper 'Efficient Attention Mechanisms for Low-Resource NLP' "
            +"was accepted to IEEE Neural Networks and Learning Systems! "
            +"Focuses on reducing O(n²) complexity for morphologically rich languages like Kazakh. "
            +"Big thanks to my graduate students. Full paper in library research section.",
            u("zhomart"), false);
        n4.addComment(Comment.now(u("nurasyl"),  "Congratulations Professor! This is directly related to my thesis — so inspiring!"));
        n4.addComment(Comment.now(u("bayan"),    "Outstanding achievement Zhomart. This brings great visibility to our department. Well done."));
        n4.addComment(Comment.now(u("anel"),     "Congrats! The low-resource NLP work is so important 🎉"));
        n4.addComment(Comment.now(u("dariya"),   "our prof is literally famous 🤩 putting 'studied under IEEE author' on my cv"));
        n4.addComment(Comment.now(u("aigerim"),  "Professor congratulations! Hope my dissertation can reach this level one day."));
        n4.addComment(Comment.now(u("zhomart"),  "thanks everyone! recognition really belongs to the students who ran the experiments 🙏"));
        ctx.newsRepository.save(n4);

        // 5
        News n5 = news("Library Update: 30 New Titles Added 📚",
            "The university library has acquired 30 new titles including 'Designing Data-Intensive Applications' "
            +"by Kleppmann, 'Deep Learning' by Goodfellow et al., and 'Domain-Driven Design' by Evans. "
            +"All available immediately. Max loan period 14 days. Library extends to 22:00 during exam period. "
            +"Return any overdue books to avoid fines.",
            u("assylzhan"), false);
        n5.addComment(Comment.now(u("nurasyl"),  "Kleppmann's book was on my reading list for months. Finally! 🙏"));
        n5.addComment(Comment.now(u("bexultan"), "finally some actual good books not just ancient textbooks from 2003 💀"));
        n5.addComment(Comment.now(u("aigerim"),  "The Bishop ML book is essential for my research. Thank you!"));
        n5.addComment(Comment.now(u("assel"),    "ooh can we borrow multiple at once??"));
        n5.addComment(Comment.now(u("assylzhan"),"Assel — yes, up to 3 books per student. Welcome!"));
        n5.addComment(Comment.now(u("arman"),    "the deep learning book is huge but worth it. grab it before someone else does 👀"));
        ctx.newsRepository.save(n5);

        // 6
        News n6 = news("⚠️ Scheduled Network Maintenance — Thursday Night",
            "heads up everyone 📢 — campus network and servers going down Thursday 22:00–02:00.\n"
            +"what's affected: campus WiFi, VPN, student portal.\n"
            +"if you have submissions due Thursday — SUBMIT BEFORE 21:30. do not wait until midnight, i'm begging.\n"
            +"library stays open with separate connection. urgent issues after: bekasyl@uni.edu. touch grass 🌱",
            u("bekasyl"), false);
        n6.addComment(Comment.now(u("dariya"),  "WHAT why is maintenance always before a deadline 😭"));
        n6.addComment(Comment.now(u("assel"),   "setting 5 alarms right now. thank you for warning us!!"));
        n6.addComment(Comment.now(u("bexultan"),"already submitted. you're welcome."));
        n6.addComment(Comment.now(u("dariya"),  "bexultan i genuinely can't stand you"));
        n6.addComment(Comment.now(u("bekasyl"), "dariya i'd submit now if i were you 😅 no drama Thursday night pls"));
        n6.addComment(Comment.now(u("sanzhar"), "maintenance at 22:00... that's peak coding hours bro 😭"));
        n6.addComment(Comment.now(u("arman"),   "real ones use the library internet. see you all there lol"));
        ctx.newsRepository.save(n6);

        // 7
        News n7 = news("Kaspi Bank × SITE: Internship Program 2026 🏦",
            "Partnership with Kaspi Bank offering Summer 2026 internships: "
            +"Backend Engineering, Data Engineering, Mobile Dev, QA Automation. "
            +"Eligibility: 2nd year+, GPA 3.0+. "
            +"Apply: careers.kaspi.kz/university by November 30. "
            +"Info session with engineers: November 12, 14:00, Aud 401.",
            u("bayan"), true);
        n7.addComment(Comment.now(u("bexultan"),"LETS GO this is what i was waiting for. kaspi engineering team is 🔥"));
        n7.addComment(Comment.now(u("dariya"),  "wait does 2nd year mean i qualify?? asking for a friend (me)"));
        n7.addComment(Comment.now(u("serdar"),  "Dariya — yes 2nd year eligible. Prepare your CV now!"));
        n7.addComment(Comment.now(u("assel"),   "1st year crying but bookmarking for next year 🥲"));
        n7.addComment(Comment.now(u("alibek"),  "already sent my application. backend track 💪"));
        n7.addComment(Comment.now(u("nurasyl"), "Will graduate students be considered for data/research tracks?"));
        n7.addComment(Comment.now(u("bayan"),   "Nurasyl — yes, contact careers.kaspi.kz directly for grad opportunities."));
        n7.addComment(Comment.now(u("zarina"),  "everyone's applying to kaspi. the competition is gonna be insane"));
        ctx.newsRepository.save(n7);

        // 8
        News n8 = news("Week 8 Check-in — How's Everyone Holding Up? 😅",
            "real talk, week 8 is the rough one — midterms done, finals not yet, but labs and projects ALL hitting at once. "
            +"just checking in: office hours always open, come even without a specific question. "
            +"struggling with workload? talk to your academic advisor. you've all been working hard. keep going. "
            +"also the vending machine near Lab 1 finally got restocked 🍫",
            u("anel"), false);
        n8.addComment(Comment.now(u("dariya"),  "anel we LOVE you. i haven't slept properly since week 5 💀"));
        n8.addComment(Comment.now(u("assel"),   "this made me tear up a little. we see you anel 🙏"));
        n8.addComment(Comment.now(u("bexultan"),"week 8 diff is real. sending strength everyone 💪"));
        n8.addComment(Comment.now(u("nurasyl"), "Appreciate the support. Graduate schedule is intense but manageable. Thank you."));
        n8.addComment(Comment.now(u("dariya"),  "the vending machine choco pie update is the most important part of this post"));
        n8.addComment(Comment.now(u("madina"),  "thank you for caring 😭 first sem has been a lot"));
        n8.addComment(Comment.now(u("anel"),    "haha I'm glad the important news got through 😄 you're all doing great. last push!"));
        n8.addComment(Comment.now(u("kamila"),  "the vending machine better have churros or i riot"));
        ctx.newsRepository.save(n8);

        // 9
        News n9 = news("AI & Robotics Club — First Meetup This Friday! 🤖",
            "Hey SITE! The AI & Robotics Club is holding its first meetup this Friday at 18:00 in Lab 1. "
            +"We'll be doing a quick intro to computer vision with OpenCV, then forming project teams for the semester. "
            +"Absolute beginners welcome — just bring a laptop. Free pizza after. "
            +"No registration needed, just show up!",
            u("zhomart"), false);
        n9.addComment(Comment.now(u("assel"),   "FREE PIZZA. i'm there regardless of the topic lol"));
        n9.addComment(Comment.now(u("arman"),   "finally a cs club at this uni. been waiting for this!!"));
        n9.addComment(Comment.now(u("nurasyl"), "I'll be there. Working on NLP so curious about the vision angle too."));
        n9.addComment(Comment.now(u("aigerim"), "Coming! Great initiative Professor."));
        n9.addComment(Comment.now(u("dias"),    "openCV sounds scary but free pizza is free pizza"));
        n9.addComment(Comment.now(u("bexultan"),"zhomart out here running a club AND teaching 3 courses. a legend fr"));
        ctx.newsRepository.save(n9);

        // 10
        News n10 = news("Competitive Programming Club — ICPC Qualifiers Prep 💻",
            "The competitive programming club is starting preparation sessions for the ICPC Kazakhstan qualifier. "
            +"Sessions every Tuesday 19:00–21:00 in Lab 3. We'll cover graph algorithms, DP, and number theory. "
            +"Codeforces rating 1200+ recommended but all skill levels welcome. "
            +"Register in the form below to get updates.",
            u("marat"), false);
        n10.addComment(Comment.now(u("alibek"),  "been waiting for this!! Codeforces grind starts now 💪"));
        n10.addComment(Comment.now(u("sanzhar"), "been grinding CF for a month for this. let's go"));
        n10.addComment(Comment.now(u("bexultan"),"finally the real stuff. who's forming a team?"));
        n10.addComment(Comment.now(u("alibek"),  "bexultan + sanzhar + me? need a 4th"));
        n10.addComment(Comment.now(u("arman"),   "i'm in if you'll have me. my CF is 1450"));
        n10.addComment(Comment.now(u("alibek"),  "arman PERFECT. team formed 🔥"));
        ctx.newsRepository.save(n10);

        // 11
        News n11 = news("Football Tournament — Sign Up Your Team! ⚽",
            "The annual SITE intramural football tournament is back! "
            +"Sign up your 7-a-side team by October 20. Matches every Saturday from 10:00 on the main field. "
            +"Top team wins a 50K tenge prize + eternal glory. "
            +"Register your team at the Student Council office (Room 103).",
            u("serdar"), false);
        n11.addComment(Comment.now(u("bexultan"),"let's GO. who's playing? forming a CS team rn"));
        n11.addComment(Comment.now(u("arman"),   "i'm in. midfield or defense, flexible"));
        n11.addComment(Comment.now(u("sanzhar"), "goalkeeper secured 🧤"));
        n11.addComment(Comment.now(u("temirlan"),"i can't code but i can kick a ball. count me in lol"));
        n11.addComment(Comment.now(u("dariya"),  "where's the women's bracket?? asking for me and the girls 💪"));
        n11.addComment(Comment.now(u("serdar"),  "Dariya — women's division available! Register as separate team."));
        n11.addComment(Comment.now(u("aizat"),   "dariya let's GOOO. who else is in??"));
        ctx.newsRepository.save(n11);

        // 12
        News n12 = news("Photography Contest — 'Campus Life' Theme 📷",
            "Calling all photographers! The university photography contest is open with the theme 'Campus Life'. "
            +"Submit 1–3 photos by November 1st to the Student Council email. "
            +"Winners displayed in the main hall + 30K tenge prize. Any camera allowed including phone. "
            +"Full rules on the portal.",
            u("anel"), false);
        n12.addComment(Comment.now(u("assel"),   "lowkey been wanting to do something like this!! submitting for sure"));
        n12.addComment(Comment.now(u("madina"),  "the aesthetic of this campus is actually really nice in morning light"));
        n12.addComment(Comment.now(u("kamila"),  "someone should capture the vibe of the library at 23:55 before a deadline 😂"));
        n12.addComment(Comment.now(u("dariya"),  "kamila that would actually WIN"));
        ctx.newsRepository.save(n12);

        // 13
        News n13 = news("Student Council Elections — Vote Now! 🗳️",
            "The Fall 2025 Student Council elections are open. "
            +"6 candidates are running for President, VP Academic, and VP Events. "
            +"Voting open October 15–17 on the student portal. "
            +"Each candidate's platform is posted in the main hall and on Teams. "
            +"Your voice matters — every vote counts.",
            u("serdar"), false);
        n13.addComment(Comment.now(u("dariya"),  "voted! took 2 minutes, please everyone just do it"));
        n13.addComment(Comment.now(u("arman"),   "finally a candidate that actually talks about lab conditions lmao"));
        n13.addComment(Comment.now(u("bexultan"),"did anyone read the platforms?? some of them are actually good"));
        n13.addComment(Comment.now(u("dias"),    "first time voting in a uni election kinda exciting ngl"));
        n13.addComment(Comment.now(u("kamila"),  "voted for whoever promised to fix the projector in 401 👀"));
        ctx.newsRepository.save(n13);

        // 14
        News n14 = news("Bolashak Scholarship Applications Open 🌍",
            "The Presidential 'Bolashak' scholarship for study abroad is now accepting applications for 2026. "
            +"Requirements: Kazakh citizenship, GPA 3.5+, language certificate (IELTS 6.5+ or TOEFL 79+). "
            +"Universities include Oxford, MIT, ETH Zurich, and 200+ others. "
            +"Information session: October 25, 13:00, Aud 401. Applications due December 1.",
            u("bayan"), true);
        n14.addComment(Comment.now(u("zarina"),  "this is literally my dream. applying for sure. anyone done it before?"));
        n14.addComment(Comment.now(u("alibek"),  "my cousin did it — said the language requirement is the hardest part. start IELTS prep NOW"));
        n14.addComment(Comment.now(u("nurasyl"), "MIT and ETH both listed. This is incredible. Graduate students eligible?"));
        n14.addComment(Comment.now(u("bayan"),   "Nurasyl — yes, Master's and PhD tracks available!"));
        n14.addComment(Comment.now(u("aigerim"), "Already applying for doctoral extension at ETH. This is the push I needed."));
        ctx.newsRepository.save(n14);

        // 15
        News n15 = news("Guest Lecture: Google Engineer on Distributed Systems 🎤",
            "We're thrilled to host Aidar Bekzhanov (Google, SWE L6) for a guest lecture on "
            +"'Designing Distributed Systems at Google Scale'. "
            +"Friday November 8, 15:00, Aud 401. Open to all students and staff. "
            +"Q&A session after. Limited seating — register on portal to reserve your spot.",
            u("marat"), false);
        n15.addComment(Comment.now(u("bexultan"),"a GOOGLE L6 engineer?? registering immediately 🚀"));
        n15.addComment(Comment.now(u("alibek"),  "this is insane. distributed systems is literally what i want to work on"));
        n15.addComment(Comment.now(u("nurasyl"), "Registered. Infrastructure at scale is always fascinating."));
        n15.addComment(Comment.now(u("sanzhar"), "please someone record this i have a conflicting exam"));
        n15.addComment(Comment.now(u("marat"),   "Sanzhar — we'll record and post. No need to miss the exam!"));
        n15.addComment(Comment.now(u("dariya"),  "never thought I'd get to meet a google engineer irl 👀"));
        n15.addComment(Comment.now(u("arman"),   "lowkey more excited for this than any lecture this semester"));
        ctx.newsRepository.save(n15);

        // 16
        News n16 = news("New JetBrains Student Licenses Available 🛠️",
            "Good news: SITE University has renewed our JetBrains educational license! "
            +"All students can now activate free access to IntelliJ IDEA, PyCharm, WebStorm, DataGrip, and more. "
            +"Activation instructions posted on the IT portal. Use your university email. "
            +"Licenses valid for 1 year and renewable while enrolled.",
            u("bekasyl"), false);
        n16.addComment(Comment.now(u("bexultan"),"FINALLY. i've been using the community edition like a peasant 💀"));
        n16.addComment(Comment.now(u("assel"),   "wait WebStorm too?? my life is changed"));
        n16.addComment(Comment.now(u("dias"),    "as a first year this timing is perfect. setting up now"));
        n16.addComment(Comment.now(u("arman"),   "DataGrip for DB Systems lab. absolute W for us"));
        n16.addComment(Comment.now(u("alibek"),  "bekasyl goat behavior fr 🙌"));
        n16.addComment(Comment.now(u("bekasyl"), "glad y'all are happy! dm me if activation doesn't work"));
        ctx.newsRepository.save(n16);

        // 17
        News n17 = news("Mental Health Awareness Week — Free Counseling Sessions 💙",
            "This week is Mental Health Awareness Week. "
            +"The Student Wellness Center (Room 108) is offering free 30-min counseling sessions all week. "
            +"Book online or walk in. Anonymous and confidential. "
            +"Also: yoga sessions Tuesday and Thursday 12:00–13:00 in the gym. "
            +"You're not alone, and asking for help is strength.",
            u("bayan"), false);
        n17.addComment(Comment.now(u("madina"),  "thank you for this 🙏 first semester has genuinely been overwhelming"));
        n17.addComment(Comment.now(u("assel"),   "booked a session already. everyone deserves support"));
        n17.addComment(Comment.now(u("dariya"),  "the yoga sessions are godsent. 12-1 is actually perfect timing between classes"));
        n17.addComment(Comment.now(u("kamila"),  "really appreciate that this is being taken seriously at our uni"));
        n17.addComment(Comment.now(u("temirlan"),"doing the yoga sessions. i need this after algorithms 😭"));
        n17.addComment(Comment.now(u("anel"),    "So glad to see this initiative. Take care of yourselves first 💙"));
        ctx.newsRepository.save(n17);

        // 18
        News n18 = news("Course Registration for Spring 2026 Opens November 1 📋",
            "Spring 2026 course registration opens November 1st on the student portal. "
            +"Priority registration for 4th year students October 28-31. "
            +"New courses offered: Machine Learning (3cr), iOS Development (3cr), Game Development (FREE, 2cr). "
            +"Check prerequisite requirements before registering. Academic advisor meetings recommended.",
            u("serdar"), false);
        n18.addComment(Comment.now(u("bexultan"),"4th year priority registration hits different. power 😌"));
        n18.addComment(Comment.now(u("alibek"),  "ios development AND game development?? choosing between these is a crime"));
        n18.addComment(Comment.now(u("dariya"),  "machine learning let's GOOO. zhomart teaching it??"));
        n18.addComment(Comment.now(u("zhomart"), "Dariya — yes! ML will be in my schedule for Spring. Looking forward to it!"));
        n18.addComment(Comment.now(u("assel"),   "game dev is literally my dream elective. please let there be space"));
        n18.addComment(Comment.now(u("dias"),    "reminder to check prerequisites. i almost registered for DB before taking OOP 💀"));
        ctx.newsRepository.save(n18);

        // 19
        News n19 = news("Dean's List — Fall 2025 Midterm Standings 🏆",
            "Congratulations to the following students who have achieved Dean's List standing (GPA 3.8+) "
            +"at the midterm point: Alibek Dzhaksybekov, Aizat Sultanova, Zhomart Aldamuratov (grad research award). "
            +"These students exemplify academic excellence. Keep up the outstanding work. "
            +"Full list posted on the faculty portal.",
            u("bayan"), false);
        n19.addComment(Comment.now(u("alibek"),  "🙏 thank you. the grind continues."));
        n19.addComment(Comment.now(u("bexultan"),"alibek you literally deserve it. insane work ethic"));
        n19.addComment(Comment.now(u("aizat"),   "Thank you! Wasn't expecting this 😭 so happy"));
        n19.addComment(Comment.now(u("arman"),   "congrats aizat and alibek!! next semester it's me 💪"));
        n19.addComment(Comment.now(u("dariya"),  "inspiration. going to study 3x harder next semester fr"));
        ctx.newsRepository.save(n19);

        // 20
        News n20 = news("Hacakathon Results — SITE Hackathon 2025 🥇",
            "After 48 intense hours, here are the results of SITE Hackathon 2025! "
            +"1st Place: Team 'Null Pointers' — AI-powered student scheduling assistant. "
            +"2nd Place: Team 'Stack Overflowers' — campus navigation app. "
            +"3rd Place: Team 'Runtime Errors' — mental health chatbot. "
            +"All projects will be reviewed for potential university adoption. Congratulations to everyone who participated!",
            u("marat"), false);
        n20.addComment(Comment.now(u("alibek"),  "team null pointers LETS GO!! 48 hours no sleep worth it 🏆"));
        n20.addComment(Comment.now(u("sanzhar"), "we built something real in 48h. will never forget this weekend"));
        n20.addComment(Comment.now(u("dariya"),  "the mental health chatbot team is SO real for that. brilliant idea"));
        n20.addComment(Comment.now(u("bexultan"),"all 3 projects are genuinely good. proud of this uni rn"));
        n20.addComment(Comment.now(u("marat"),   "Incredible work from everyone. This is what SITE is about. 👏"));
        ctx.newsRepository.save(n20);

        // 21
        News n21 = news("Reminder: Academic Integrity Policy",
            "As we approach exam season, a reminder of our academic integrity policy. "
            +"All submitted work must be your own. AI tools (ChatGPT, Copilot) may only be used where explicitly permitted by the instructor. "
            +"Plagiarism and cheating result in course failure and potential expulsion. "
            +"When in doubt, ask your instructor. Honest work is always better.",
            u("bayan"), false);
        n21.addComment(Comment.now(u("bexultan"),"the ai policy is fair tbh. if you understand what it outputs fine, if not 🤷"));
        n21.addComment(Comment.now(u("arman"),   "the 'when in doubt ask' part is underrated advice"));
        n21.addComment(Comment.now(u("assel"),   "this is lowkey scary but also good. keeps things fair"));
        n21.addComment(Comment.now(u("nurasyl"), "Research integrity is paramount. Agreed with this policy."));
        ctx.newsRepository.save(n21);

        // 22
        News n22 = news("Lab 4 Equipment Upgraded — New Workstations! 💻",
            "Great news: Lab 4 has been completely upgraded with 32 new workstations "
            +"(Intel Core i9, 32GB RAM, RTX 4060). "
            +"The upgrades support running local LLMs, game development tools, and complex simulations. "
            +"Lab access hours also extended to 23:00 Monday–Thursday. "
            +"No more fighting over the two good computers!",
            u("bekasyl"), false);
        n22.addComment(Comment.now(u("bexultan"),"RTX 4060 in a uni lab?? are you serious right now 🤯"));
        n22.addComment(Comment.now(u("nurasyl"),  "Perfect timing for my research experiments. Thank you!!"));
        n22.addComment(Comment.now(u("aigerim"),  "Running local models is now feasible. This changes my workflow completely."));
        n22.addComment(Comment.now(u("alibek"),   "23:00 lab access is everything. comp programming sessions incoming"));
        n22.addComment(Comment.now(u("arman"),    "RIP to fighting over that one good PC in Lab 3. we won 🙏"));
        ctx.newsRepository.save(n22);

        // 23
        News n23 = news("Teaching Assistant Positions Available — Apply Now",
            "We are looking for undergraduate and graduate TAs for the following courses next semester: "
            +"Discrete Mathematics, OOP, Algorithms, Web Development, and Technical English. "
            +"Requirements: B+ or above in the course, good communication skills, 6–8 hours/week commitment. "
            +"Monthly stipend: 45,000 tenge. Apply via the HR portal by November 15.",
            u("serdar"), false);
        n23.addComment(Comment.now(u("alibek"),   "applying for algorithms TA. perfect for my schedule."));
        n23.addComment(Comment.now(u("zarina"),   "web dev TA sounds ideal. been wanting to do this for a while"));
        n23.addComment(Comment.now(u("bexultan"), "oop ta would be great experience before working as an actual dev"));
        n23.addComment(Comment.now(u("nurasyl"),  "Applying for Discrete Math. Great way to reinforce fundamentals."));
        n23.addComment(Comment.now(u("aizat"),    "45k stipend per month is actually solid for a student. worth it"));
        ctx.newsRepository.save(n23);

        // 24
        News n24 = news("Language Clubs Starting This Month 🌐",
            "New language clubs are forming this semester:\n"
            +"• English Conversation Club — Wednesdays 17:00, Room 112\n"
            +"• German Club — Thursdays 18:00, Room 115\n"
            +"• Japanese Club — Fridays 17:00, Room 115\n"
            +"• Korean Club — Saturdays 12:00, Room 112\n"
            +"All levels welcome. Learn languages, make friends, prepare for international programs.",
            u("anel"), false);
        n24.addComment(Comment.now(u("assel"),   "KOREAN CLUB?? I've been self-studying for 2 years joining immediately"));
        n24.addComment(Comment.now(u("kamila"),  "english conversation club is actually useful for IELTS prep. smart idea"));
        n24.addComment(Comment.now(u("madina"),  "german club sounds interesting!! might try it"));
        n24.addComment(Comment.now(u("arman"),   "japanese for anime reasons but also JLPT prep. i'm in 😂"));
        ctx.newsRepository.save(n24);

        // 25
        News n25 = news("Midterm Results Posted — Check Your Portal",
            "Midterm grades have been posted to the student portal for all courses. "
            +"Students with a grade below 60% are strongly encouraged to meet with their course instructor. "
            +"Academic advisors available for schedule review and academic plan adjustments. "
            +"Appeals deadline: 5 working days from posting date.",
            u("serdar"), false);
        n25.addComment(Comment.now(u("dariya"),  "checked mine. some good some... not great. office hours incoming 😅"));
        n25.addComment(Comment.now(u("bexultan"),"algorithms midterm was brutal but the rest was fine."));
        n25.addComment(Comment.now(u("assel"),   "OOP grade was a surprise (bad surprise). gonna actually study this time"));
        n25.addComment(Comment.now(u("temirlan"),"going to office hours tuesday. this is the wake up call i needed"));
        n25.addComment(Comment.now(u("arman"),   "discrete math A!! zhomart the GOAT 🙏"));
        n25.addComment(Comment.now(u("aizat"),   "really happy with my grades! the studying paid off"));
        ctx.newsRepository.save(n25);

        // 26
        News n26 = news("New Cafeteria Menu Starting Monday 🍜",
            "The university cafeteria is launching a new menu starting Monday! "
            +"Additions: beshbarmak Fridays, expanded vegetarian options daily, "
            +"fresh juice bar open from 8:00. Prices unchanged. "
            +"Feedback forms at every table — tell us what you want!",
            u("serdar"), false);
        n26.addComment(Comment.now(u("bexultan"),"BESHBARMAK FRIDAYS. this university has peaked."));
        n26.addComment(Comment.now(u("dariya"),  "the vegetarian options expansion is long overdue. thank you!!"));
        n26.addComment(Comment.now(u("kamila"),  "juice bar at 8am?? i might actually wake up for 8am calculus now"));
        n26.addComment(Comment.now(u("arman"),   "finally some real food and not just that sad sandwich"));
        n26.addComment(Comment.now(u("madina"),  "the vegetarian food has been a struggle tbh. this is great news"));
        n26.addComment(Comment.now(u("dias"),    "beshbarmak friday is peak kazakh uni culture ngl"));
        ctx.newsRepository.save(n26);

        // 27
        News n27 = news("Research Grants Available — Applications Open",
            "The University Research Fund is accepting applications for student research grants. "
            +"Undergraduate: up to 300,000 tenge. Graduate: up to 1,000,000 tenge. "
            +"Eligible fields: CS, Engineering, Applied Mathematics, Data Science. "
            +"Applications due November 30. Supervisors must be confirmed. "
            +"Info: research.uni.edu/grants",
            u("bayan"), false);
        n27.addComment(Comment.now(u("nurasyl"),  "Applying for the graduate grant. My thesis topic fits perfectly."));
        n27.addComment(Comment.now(u("aigerim"),  "Already have a project, submitting with Prof. Aldamuratov as supervisor."));
        n27.addComment(Comment.now(u("zhomart"),  "Happy to supervise strong applications. Come talk to me in office hours."));
        n27.addComment(Comment.now(u("arman"),    "undergrad grant of 300k tenge is REAL. can i write a paper as a 2nd year?"));
        n27.addComment(Comment.now(u("zhomart"),  "Arman — absolutely yes. Start small, scope appropriately, talk to a faculty member."));
        ctx.newsRepository.save(n27);

        // 28
        News n28 = news("Campus WiFi Upgrade Complete — 2x Faster 📶",
            "The campus-wide WiFi infrastructure upgrade is complete! "
            +"Network capacity doubled, dead zones in buildings B and C eliminated. "
            +"New SSIDs: SITE-Student (personal devices), SITE-Lab (lab machines). "
            +"Reconnect using your university credentials. Speed should be noticeable immediately. "
            +"Report any issues to bekasyl@uni.edu.",
            u("bekasyl"), false);
        n28.addComment(Comment.now(u("bexultan"),"downloading at 50MB/s in building B rn. FINALLY 🙌"));
        n28.addComment(Comment.now(u("assel"),   "no more 'connected but no internet' moment. we've made it"));
        n28.addComment(Comment.now(u("dariya"),  "lab 3 used to drop every 10 minutes. brb testing"));
        n28.addComment(Comment.now(u("sanzhar"), "competitive programming server connections were so laggy before. this is huge"));
        n28.addComment(Comment.now(u("bekasyl"), "enjoy! let me know if anything's weird. tested everything but you never know 😅"));
        ctx.newsRepository.save(n28);

        // 29
        News n29 = news("Semester Abroad Info Session — November 5",
            "Interested in studying a semester at a partner university? "
            +"Info session on November 5, 14:00, Aud 402. "
            +"Partners include: TU Berlin, KTH Stockholm, KAIST Korea, EPFL Switzerland, TU Delft. "
            +"Requirements: 2nd year+, GPA 3.2+, language proficiency. "
            +"Seats limited. Financial support available for most programs.",
            u("bayan"), false);
        n29.addComment(Comment.now(u("zarina"),   "KAIST and EPFL are on the list 😭 this is my dream"));
        n29.addComment(Comment.now(u("bexultan"), "TU Berlin has an amazing CS program. definitely going to this session"));
        n29.addComment(Comment.now(u("nurasyl"),  "KTH Stockholm for ML/AI research. My dream collaboration university."));
        n29.addComment(Comment.now(u("alibek"),   "the fact that financial support is available changes everything. applying for sure"));
        ctx.newsRepository.save(n29);

        // 30
        News n30 = news("Important: Retake Exam Registration Deadline October 28",
            "Students who need to retake a failed or FX-graded exam must register by October 28 via the student portal. "
            +"Retake exams scheduled November 4–8. You may retake maximum 2 exams per semester. "
            +"Retake fee: 5,000 tenge per exam. "
            +"Contact Academic Affairs (Room 205) for special circumstances.",
            u("serdar"), false);
        n30.addComment(Comment.now(u("bexultan"),"okay real talk this FX retake policy is actually fair. better than just failing out"));
        n30.addComment(Comment.now(u("temirlan"),"registering today. the algorithms retake is my redemption arc"));
        n30.addComment(Comment.now(u("dariya"),  "the 5k fee is a bit rough but fair enough"));
        n30.addComment(Comment.now(u("sanzhar"), "database retake. i'll come back stronger 💪"));
        ctx.newsRepository.save(n30);

        // 31
        News n31 = news("OOP Lab 4 Released — Design Patterns 🏗️",
            "Lab 4 is now available on the course page. Topic: Design Patterns — you'll implement "
            +"Observer, Strategy, and Factory patterns in a mini event management system. "
            +"Starter code on Teams. Due in 2 weeks. "
            +"This lab is worth 15% of your practice grade — don't leave it to the last day.",
            u("anel"), false);
        n31.addComment(Comment.now(u("bexultan"),"observer pattern is actually elegant once it clicks"));
        n31.addComment(Comment.now(u("kamila"),  "starter code is really helpful. last lab had nothing lol"));
        n31.addComment(Comment.now(u("assel"),   "factory pattern still confuses me. coming to office hours"));
        n31.addComment(Comment.now(u("temirlan"),"2 weeks is generous. i'll definitely not wait until day 13 again 😅"));
        n31.addComment(Comment.now(u("dias"),    "observer + strategy + factory in ONE lab. Anel does not miss with assignments"));
        ctx.newsRepository.save(n31);

        // 32
        News n32 = news("Career Fair — November 20, Main Hall 💼",
            "The Fall 2025 Career Fair is coming! Companies attending: Kaspi Bank, Kolesa Group, Chocofood, "
            +"Beeline Kazakhstan, Jusan Bank, Halyk Bank, Kazatomprom Digital, EPAM Systems. "
            +"Bring updated CVs. Business casual dress. Doors open 10:00–17:00. "
            +"Pre-register companies of interest on the portal to get priority meeting slots.",
            u("serdar"), true);
        n32.addComment(Comment.now(u("alibek"),  "EPAM and Kaspi in the same day?? prepping 2 CVs tonight"));
        n32.addComment(Comment.now(u("bexultan"),"kolesa group's engineering team is underrated. gonna check them out"));
        n32.addComment(Comment.now(u("arman"),   "what's the dress code exactly? can i wear smart jeans?"));
        n32.addComment(Comment.now(u("serdar"),  "Arman — smart casual is fine. No shorts, no track suits. Use common sense 😄"));
        n32.addComment(Comment.now(u("zarina"),  "Jusan Bank's fintech team has some interesting ML work. going for sure"));
        n32.addComment(Comment.now(u("sanzhar"), "pre-registered for EPAM and Kaspi. they hire CP people regularly"));
        n32.addComment(Comment.now(u("madina"),  "this is for 1st years too right? or is it mainly for older students?"));
        n32.addComment(Comment.now(u("serdar"),  "Madina — open to all years! Great for networking even if you're not job hunting yet."));
        ctx.newsRepository.save(n32);

        // 33
        News n33 = news("Algorithms Workshop — Recursion & DP Deep Dive 🧠",
            "Struggling with recursion or dynamic programming? "
            +"Special 3-hour workshop this Saturday 13:00–16:00 in Lab 2. "
            +"We'll work through 15 problems together — from easy to competitive-level. "
            +"Bring your laptop and be ready to code. Free registration, link on Teams.",
            u("anel"), false);
        n33.addComment(Comment.now(u("temirlan"),"this is exactly what i need. registering NOW"));
        n33.addComment(Comment.now(u("sanzhar"), "DP finally explained by someone good. i'm in"));
        n33.addComment(Comment.now(u("dias"),    "3 hours of DP on a saturday... worth it. see everyone there"));
        n33.addComment(Comment.now(u("bexultan"),"anel really said let me fix everyone's DP comprehension in one session. respect 🫡"));
        n33.addComment(Comment.now(u("arman"),   "this university's teaching staff are actually amazing. shoutout to all of them"));
        ctx.newsRepository.save(n33);

        // 34
        News n34 = news("CS Department Research Seminar — Every Friday 16:00",
            "Starting next Friday, the CS department will host a weekly research seminar (30 min + Q&A). "
            +"All students and staff welcome. First three topics: "
            +"- Attention is All You Need: 7 years later (Zhomart)\n"
            +"- Formal verification in distributed systems (Marat)\n"
            +"- Ethics and bias in AI systems (Guest speaker)\n"
            +"Calendar on Teams. Light refreshments provided.",
            u("bayan"), false);
        n34.addComment(Comment.now(u("nurasyl"),  "The attention seminar is something I need to see. Will be there."));
        n34.addComment(Comment.now(u("aigerim"),  "Bias in AI systems is so relevant to my research. Excited for that one."));
        n34.addComment(Comment.now(u("arman"),    "refreshments make every seminar better tbh"));
        n34.addComment(Comment.now(u("alibek"),   "formal verification is something i barely understand. going to learn"));
        ctx.newsRepository.save(n34);

        // 35
        News n35 = news("Winter Break Schedule Announced ❄️",
            "Winter break runs December 28 – January 10. "
            +"University offices closed December 30 – January 2 (skeleton staff). "
            +"Library open December 28–29 and January 6–10. "
            +"New semester begins January 13. "
            +"Take time to rest and recover — you've earned it!",
            u("serdar"), false);
        n35.addComment(Comment.now(u("bexultan"),"january 13 start is actually reasonable. enough time to decompress"));
        n35.addComment(Comment.now(u("dariya"),  "two weeks of break sounds like a lot but it genuinely goes fast"));
        n35.addComment(Comment.now(u("assel"),   "first sem almost done 😭 can't believe how fast it went"));
        n35.addComment(Comment.now(u("kamila"),  "need those 2 weeks to recover from this semester's suffering lol"));
        n35.addComment(Comment.now(u("nurasyl"), "Winter break = writing time for my thesis chapter 3. The grind never stops."));
        n35.addComment(Comment.now(u("aigerim"), "Same Nurasyl 😅 dissertation doesn't take holidays"));
        ctx.newsRepository.save(n35);

        // 36 — short maintenance
        News n36 = news("Portal Maintenance Sunday 3:00–6:00 AM",
            "Quick maintenance window Sunday night/Monday morning 3:00–6:00 AM. "
            +"Portal will be down briefly. Nothing urgent should be affected at that hour. "
            +"If you have insomnia and need to submit something — do it before 3 AM.",
            u("bekasyl"), false);
        n36.addComment(Comment.now(u("sanzhar"), "3am maintenance hits different when you're still coding at 2:59"));
        n36.addComment(Comment.now(u("alibek"),  "bekasyl knows the student schedule better than we do 😂"));
        ctx.newsRepository.save(n36);

        // 37
        News n37 = news("Discrete Mathematics — Extra Practice Problems Posted",
            "I've uploaded 50 extra practice problems covering all exam topics to the course page. "
            +"Solutions posted 48 hours before the exam. "
            +"Focus especially on: graph theory, combinatorics, and proof techniques — "
            +"these were most commonly weak in the midterm.",
            u("zhomart"), false);
        n37.addComment(Comment.now(u("dariya"),  "50 problems... respectfully terrified but grateful"));
        n37.addComment(Comment.now(u("arman"),   "graph theory is the one. doing those first"));
        n37.addComment(Comment.now(u("aizat"),   "thank you! exactly what i needed to prepare properly"));
        n37.addComment(Comment.now(u("bexultan"),"proofs are still my nemesis but i'm fighting. thanks professor"));
        n37.addComment(Comment.now(u("madina"),  "50 problems + office hours = i am no longer scared. maybe."));
        ctx.newsRepository.save(n37);

        // 38
        News n38 = news("Thesis Defense Schedule — Fall 2025 📄",
            "Graduate thesis defenses scheduled for December 16–20. "
            +"Defending this semester: Aigerim Bekzhanova (Doctorate, Dec 16), "
            +"and two external students. "
            +"All defenses are open to the university community. "
            +"Full schedule on the graduate studies portal.",
            u("bayan"), false);
        n38.addComment(Comment.now(u("aigerim"),  "Thank you Dean. Prepared and ready! Nervous but confident 🙏"));
        n38.addComment(Comment.now(u("zhomart"),  "Aigerim you're more than ready. Your research is excellent."));
        n38.addComment(Comment.now(u("nurasyl"),  "Congratulations Aigerim! Inspiring for all of us in the graduate program."));
        n38.addComment(Comment.now(u("dariya"),   "wow a real PhD defense?? can we actually attend?? asking seriously"));
        n38.addComment(Comment.now(u("bayan"),    "Dariya — yes, open to everyone! Great learning experience."));
        ctx.newsRepository.save(n38);

        // 39
        News n39 = news("Dormitory Notice: Quiet Hours Reminder 🔇",
            "Friendly reminder: dorm quiet hours are 23:00–07:00 on weekdays and 00:00–09:00 on weekends. "
            +"With exams approaching, respect your neighbors who may have different schedules. "
            +"Repeated violations result in formal warnings. "
            +"Study rooms in Building A are open 24/7 — use them for late-night sessions.",
            u("serdar"), false);
        n39.addComment(Comment.now(u("kamila"),  "the room above mine was playing music at 2am. THANK YOU for this reminder"));
        n39.addComment(Comment.now(u("arman"),   "24/7 study rooms in building A is info i needed. didn't know this!!"));
        n39.addComment(Comment.now(u("dias"),    "the guy next door snores louder than any music but okay 😭"));
        n39.addComment(Comment.now(u("dariya"),  "dias report him to the dorm admin lmaoo 💀"));
        ctx.newsRepository.save(n39);

        // 40
        News n40 = news("End of Semester Celebration — December 27 🎉",
            "Join us for the end of semester celebration in the main hall December 27, 18:00! "
            +"Live music, food, games, and photo booth. "
            +"Best project showcase: top 5 projects from all courses will be displayed. "
            +"Free entry for all students and staff. Bring good vibes only.",
            u("anel"), false);
        n40.addComment(Comment.now(u("dariya"),  "OKAY THIS IS WHAT I LIVE FOR. marking my calendar rn"));
        n40.addComment(Comment.now(u("assel"),   "best project showcase?? hope web dev gets a spot 🤞"));
        n40.addComment(Comment.now(u("bexultan"),"top 5 projects better include something CP related or i riot"));
        n40.addComment(Comment.now(u("kamila"),  "photo booth at a uni event?? finally some fun activities"));
        n40.addComment(Comment.now(u("arman"),   "survived first semester (barely). time to celebrate"));
        n40.addComment(Comment.now(u("madina"),  "this was the motivation i didn't know i needed to finish strong 🥹"));
        n40.addComment(Comment.now(u("anel"),    "Can't wait to celebrate with you all! You've all worked so hard this semester 🎉"));
        ctx.newsRepository.save(n40);
    }

    // ── REQUESTS ─────────────────────────────────────────────────────────────

    private void seedRequests() {
        String[][] reqs = {
            {"dariya",   "Transcript for Bolashak Scholarship",            "TRANSCRIPT_FOR_YEAR",             "HIGH",   "PENDING",
             "Applying for the Presidential Bolashak scholarship. Need official academic transcript for Fall 2025 and cumulative. Deadline November 15th. Please process urgently."},
            {"bexultan", "Academic Mobility — EPFL Switzerland",           "ACADEMIC_MOBILITY",               "MEDIUM", "PENDING",
             "Applying for semester exchange at EPFL for Spring 2026. Require official enrollment and academic standing letter. Application deadline January 20."},
            {"assel",    "Certificate of Enrollment — Bank Account",       "CERTIFICATE_OF_EDUCATION",        "LOW",    "APPROVED",
             "Need official enrollment certificate to open student account at Halyk Bank."},
            {"nurasyl",  "Master's Thesis Topic Coordination",             "COORDINATION_OF_DIPLOMA_TOPIC",   "MEDIUM", "PENDING",
             "Formal approval requested for thesis topic: 'Cross-lingual Transfer Learning for Kazakh NER'. Proposed supervisor: Prof. Aldamuratov."},
            {"arman",    "Semester Transcript — Visa Application",         "TRANSCRIPT_FOR_SEMESTER",         "HIGH",   "APPROVED",
             "Applying for German student visa for exchange program. Need official semester transcript stamped and signed."},
            {"zarina",   "Academic Mobility — TU Berlin",                  "ACADEMIC_MOBILITY",               "MEDIUM", "PENDING",
             "Applying for Winter 2026 exchange at TU Berlin. Department: Computer Science. Need official recommendation + academic record."},
            {"alibek",   "Coordination of Diploma Topic",                  "COORDINATION_OF_DIPLOMA_TOPIC",   "MEDIUM", "PENDING",
             "Requesting formal coordination for bachelor's thesis: 'High-Performance Key-Value Store in Go'. Supervisor: Prof. Marat Ospanov."},
            {"sanzhar",  "Certificate of Education — Scholarship",         "CERTIFICATE_OF_EDUCATION",        "LOW",    "APPROVED",
             "Need certificate confirming enrollment for private scholarship application deadline October 30."},
            {"kamila",   "Transcript for Year — Internship Application",   "TRANSCRIPT_FOR_YEAR",             "HIGH",   "PENDING",
             "Kaspi Bank internship requires official academic transcript. Deadline November 20."},
            {"temirlan", "Request for Creating Organization",              "REQUEST_FOR_CREATING_ORGANIZATION","LOW",    "PENDING",
             "Requesting official registration for 'SITE Competitive Programming Society'. 12 founding members confirmed."},
            {"dariya",   "Certificate of Enrollment — Second Copy",        "CERTIFICATE_OF_EDUCATION",        "LOW",    "APPROVED",
             "Previous certificate lost. Need replacement for dormitory administration."},
            {"aizat",    "Academic Mobility — KTH Stockholm",              "ACADEMIC_MOBILITY",               "HIGH",   "PENDING",
             "KTH Royal Institute of Technology exchange for Spring 2026. Full scholarship available — need university letter of support."},
            {"bexultan", "Semester Transcript — Duplicate Copy",           "TRANSCRIPT_FOR_SEMESTER",         "LOW",    "REJECTED",
             "Requested in error — duplicate of previous request. Please disregard."},
            {"madina",   "Transcript for Semester 1",                      "TRANSCRIPT_FOR_SEMESTER",         "LOW",    "PENDING",
             "First semester results for family records and personal scholarship application."},
            {"nurasyl",  "Certificate of Graduate Enrollment",             "CERTIFICATE_OF_EDUCATION",        "MEDIUM", "APPROVED",
             "Need graduate enrollment certificate for research conference registration at ICML 2026."},
            {"aigerim",  "Doctoral Dissertation Coordination",             "COORDINATION_OF_DIPLOMA_TOPIC",   "HIGH",   "APPROVED",
             "Formal coordination for doctoral dissertation: 'Multilingual Instruction Following in Low-Resource Settings'. Supervisor: Prof. Aldamuratov."},
            {"yerlan",   "Transcript for Year",                            "TRANSCRIPT_FOR_YEAR",             "MEDIUM", "PENDING",
             "Need academic record for family support documentation."},
            {"alibek",   "Year Transcript for Internship",                 "TRANSCRIPT_FOR_YEAR",             "HIGH",   "APPROVED",
             "EPAM Systems internship application requires official transcript. Interview scheduled November 8."},
            {"arman",    "Creating Photography Club",                      "REQUEST_FOR_CREATING_ORGANIZATION","LOW",    "PENDING",
             "Requesting official registration for SITE Photography Club. Purpose: campus events, competitions, and skill building. 8 founding members."},
            {"kamila",   "Academic Mobility — Korea University",           "ACADEMIC_MOBILITY",               "MEDIUM", "PENDING",
             "Application for exchange at Korea University Seoul, Spring 2026. K-government scholarship candidate."},
            {"dias",     "Certificate of Enrollment",                      "CERTIFICATE_OF_EDUCATION",        "LOW",    "APPROVED",
             "First year certificate needed for bank account opening."},
            {"zarina",   "Semester Transcript",                            "TRANSCRIPT_FOR_SEMESTER",         "MEDIUM", "APPROVED",
             "Needed for private merit scholarship documentation."},
            {"sanzhar",  "Coordination of Thesis Topic",                   "COORDINATION_OF_DIPLOMA_TOPIC",   "MEDIUM", "PENDING",
             "Bachelor's thesis: 'Cache-Efficient Graph Algorithms for Modern Processors'. Supervisor: Prof. Marat Ospanov."},
        };
        for (String[] r : reqs) {
            Request req = new Request(ctx.requestIds.next(), u(r[0]), r[1],
                    HelpType.valueOf(r[2]), Faculty.SITE, UrgencyLevel.valueOf(r[3]), r[5]);
            if (!r[4].equals("PENDING")) req.changeStatus(RequestStatus.valueOf(r[4]));
            ctx.requestRepository.save(req);
        }
    }

    // ── IT ORDERS ────────────────────────────────────────────────────────────

    private void seedOrders() {
        Object[][] orders = {
            {u("dariya"),   "Laptop won't connect to campus eduroam WiFi. Authentication keeps failing. MacBook Air M2. Urgent — lab submission tomorrow.", "NEW", null},
            {u("bexultan"), "Projector in Aud 401 flickering during OOP lectures Tuesday and Thursday. Prof Aldamuratov confirmed issue.", "ACCEPTED", u("bekasyl")},
            {u("assel"),    "Need USB-C to HDMI adapter for web dev project presentation in Lab 1 Friday. Personal laptop has no HDMI port.", "DONE", u("bekasyl")},
            {u("nurasyl"),  "Lab 4 access card not working after 18:00. Need after-hours access for research sessions. ID: NUR-2025-001.", "NEW", null},
            {u("arman"),    "Desktop PC #7 in Lab 2 has a dead keyboard. Half the keys not registering. Affects everyone using that station.", "DONE", u("bekasyl")},
            {u("alibek"),   "Request 4 additional monitors for the competitive programming room (Lab 3). Current setup has only 2.", "ACCEPTED", u("bekasyl")},
            {u("kamila"),   "Printer in the library isn't working. Jam error won't clear. Need to print thesis draft today.", "DONE", u("bekasyl")},
            {u("zarina"),   "VS Code auto-update broke my Java extension on Lab 1 machines. Can someone rollback or reinstall?", "ACCEPTED", u("bekasyl")},
            {u("temirlan"), "Smart board in Aud 402 is not connecting to the PC. Touch input not working at all.", "DONE", u("bekasyl")},
            {u("sanzhar"),  "Need IntelliJ IDEA Ultimate license activated on Lab 3 machine #12. Shows trial expired.", "DONE", u("bekasyl")},
            {u("madina"),   "Campus eduroam password reset not working on my phone. QR code method also fails.", "NEW", null},
            {u("aizat"),    "Request: install Docker Desktop on Lab 2 machines for software engineering course.", "ACCEPTED", u("bekasyl")},
            {u("dias"),     "My student portal account is locked after wrong password attempts. Need reset.", "DONE", u("bekasyl")},
            {u("yerlan"),   "Webcam on Lab 1 machine #5 not detected. Need for online exam session.", "NEW", null},
            {u("alibek"),   "Building B second floor has no working power outlets near the desks. Laptop dies during long sessions.", "ACCEPTED", u("bekasyl")},
            {u("bexultan"), "Request additional whiteboards in Lab 3 for algorithm visualization during study sessions.", "DONE", u("bekasyl")},
            {u("assel"),    "Teams meeting audio echo in Room 112. External speaker causes feedback loop.", "DONE", u("bekasyl")},
            {u("arman"),    "Python 3.12 not installed on Lab 3 machines, only 3.9. Need update for course assignments.", "DONE", u("bekasyl")},
            {u("kamila"),   "Lock on Lab 1 door sometimes won't open with student card — had to wait 20 min yesterday.", "ACCEPTED", u("bekasyl")},
            {u("nurasyl"),  "Request GPU server access for ML model training. Current quota insufficient for dissertation experiments.", "NEW", null},
            {u("zarina"),   "Can we get MATLAB licenses for Lab 2? Needed for statistics course assignments.", "ACCEPTED", u("bekasyl")},
            {u("dariya"),   "Library printing credits not loading after top-up. Payment processed but balance shows 0.", "DONE", u("bekasyl")},
            {u("sanzhar"),  "External display support needed in study room A3. No HDMI cables available.", "NEW", null},
            {u("temirlan"), "Request: repair the broken chair in Lab 1 row 3. It collapses slightly when you sit down.", "DONE", u("bekasyl")},
            {u("dias"),     "Projector remote in Room 112 is missing. Instructor has to get up to switch slides.", "DONE", u("bekasyl")},
            {u("aizat"),    "Database client (DBeaver) not installed on lab machines. Needed for DB Systems assignments.", "DONE", u("bekasyl")},
            {u("madina"),   "My student email isn't syncing on mobile. IT portal shows account active.", "DONE", u("bekasyl")},
            {u("arman"),    "Network drive mapping failing on Lab 2 machines. Can't access shared course files.", "ACCEPTED", u("bekasyl")},
            {u("aigerim"),  "Request dedicated partition on the research server for doctoral dissertation data (500GB).", "NEW", null},
            {u("alibek"),   "Node.js and npm not in PATH on Lab 1 machines. Web dev students can't run projects.", "DONE", u("bekasyl")},
            {u("kamila"),   "Broken ventilation in Lab 3 — it gets extremely hot with 30 people in there.", "ACCEPTED", u("bekasyl")},
            {u("yerlan"),   "PC in Aud 401 front desk freezes when projector connected. Affects lectures.", "DONE", u("bekasyl")},
            {u("bexultan"), "Noise-cancelling headphones for study room A3 — some students bring speakers which is distracting.", "NEW", null},
            {u("nurasyl"),  "VPN access request for accessing research databases from off-campus.", "DONE", u("bekasyl")},
            {u("assel"),    "Request: fix the time display on the clock in Room 112. It's 45 minutes slow.", "DONE", u("bekasyl")},
            {u("dias"),     "Zoom not installed on Lab 4 machines. Need for online guest lecture.", "DONE", u("bekasyl")},
            {u("zarina"),   "Can we get a recycling bin for Lab 2? Currently no separate trash separation.", "NEW", null},
            {u("sanzhar"),  "Mouse scroll wheel broken on Lab 3 machine #8. Makes coding painful.", "DONE", u("bekasyl")},
            {u("temirlan"), "Request IntelliJ IDEA license for personal laptop (student license activation failed).", "DONE", u("bekasyl")},
            {u("arman"),    "Aud 401 air conditioning remote missing. Room is freezing in mornings.", "ACCEPTED", u("bekasyl")},
            {u("madina"),   "First year student — need help setting up VPN for accessing library e-resources from home.", "DONE", u("bekasyl")},
            {u("alibek"),   "Request: Postman installation on Lab 1 machines for API testing in SE course.", "DONE", u("bekasyl")},
            {u("kamila"),   "Building C fire alarm went off at 7:30 AM causing everyone to evacuate in the cold. False alarm?", "DONE", u("bekasyl")},
            {u("dariya"),   "Can we have a phone charging station in the library? Outlets are all occupied or too far from seats.", "NEW", null},
            {u("nurasyl"),  "Thesis LaTeX template has broken fonts on the university's LaTeX installation. MiKTeX update needed.", "DONE", u("bekasyl")},
            {u("aigerim"),  "Research cluster SSH key regenerated without notice — all running jobs terminated.", "NEW", null},
            {u("aizat"),    "Forgot student card in dorm, locked out of Lab 2 before exam. Emergency access needed.", "DONE", u("bekasyl")},
            {u("bexultan"), "PostgreSQL version mismatch between Lab machines (14.2) and expected course version (15). Breaking assignments.", "DONE", u("bekasyl")},
        };
        for (Object[] o : orders) {
            Order ord = new Order(ctx.orderIds.next(), (Username)o[0], (String)o[1]);
            String status = (String)o[2];
            Username executor = (Username)o[3];
            if (!status.equals("NEW")) {
                ord.accept(executor);
                if (status.equals("DONE")) ord.complete();
            }
            ctx.orderRepository.save(ord);
        }
    }

    // ── RESEARCH ─────────────────────────────────────────────────────────────

    private void seedResearch() {
        Teacher zhomart = (Teacher) ctx.userRepository.findByUsername(u("zhomart")).orElse(null);
        if (zhomart == null) return;
        zhomart.activateResearcher("Natural Language Processing & Machine Learning");
        zhomart.researcherProfile().subscribe("IEEE Neural Networks");
        zhomart.researcherProfile().subscribe("ACL Anthology");
        zhomart.researcherProfile().subscribe("Nature Machine Intelligence");
        ctx.userRepository.save(zhomart);

        GraduateStudent nurasyl = (GraduateStudent) ctx.userRepository.findByUsername(u("nurasyl")).orElse(null);
        if (nurasyl != null) {
            nurasyl.activateResearcher("Low-Resource NLP");
            nurasyl.researcherProfile().subscribe("IEEE Neural Networks");
            nurasyl.researcherProfile().subscribe("ACL Anthology");
            ctx.userRepository.save(nurasyl);
        }

        GraduateStudent aigerim = (GraduateStudent) ctx.userRepository.findByUsername(u("aigerim")).orElse(null);
        if (aigerim != null) {
            aigerim.activateResearcher("Multilingual AI & Instruction Tuning");
            aigerim.researcherProfile().subscribe("Nature Machine Intelligence");
            aigerim.researcherProfile().subscribe("ACL Anthology");
            ctx.userRepository.save(aigerim);
        }

        // Papers by zhomart
        ResearchPaper p1 = paper("Efficient Attention Mechanisms for Low-Resource NLP",
                u("zhomart"), "IEEE Neural Networks",
                "We propose sparse attention reducing O(n²) complexity for morphologically rich languages.", 18,
                "10.1109/TNNLS.2025.001234");

        ResearchPaper p2 = paper("Kazakh Named Entity Recognition via Cross-lingual Transfer",
                u("zhomart"), "ACL Anthology",
                "Cross-lingual BERT adaptation for Kazakh NER with limited labeled data.", 12,
                "10.18653/v1/2025.acl-long.042");

        ResearchPaper p3 = paper("Morphology-Aware Tokenization for Turkic Languages",
                u("zhomart"), "ACL Anthology",
                "Novel subword tokenization approach preserving Turkic morphological structure.", 10,
                "10.18653/v1/2025.acl-short.089");

        ResearchPaper p4 = paper("Benchmark Evaluation of LLMs on Kazakh-Language Tasks",
                u("zhomart"), "Nature Machine Intelligence",
                "Comprehensive evaluation of 12 major LLMs on Kazakh reading comprehension and generation tasks.", 24,
                "10.1038/s42256-025-00901-4");

        // Paper by aigerim
        ResearchPaper p5 = paper("Multilingual Instruction Following with Cultural Context",
                u("aigerim"), "ACL Anthology",
                "Instruction fine-tuning with culturally-grounded prompts for Central Asian languages.", 15,
                "10.18653/v1/2025.acl-long.201");

        // Papers by nurasyl (in progress — shorter, conference workshop)
        ResearchPaper p6 = paper("Preliminary Analysis of Kazakh POS Tagging with mBERT",
                u("nurasyl"), "ACL Anthology",
                "Workshop paper on part-of-speech tagging for Kazakh using multilingual BERT.", 6,
                null);

        // Research projects
        ResearchProject proj1 = new ResearchProject(ctx.projectIds.next(),
                new JournalName("IEEE Neural Networks"),
                "Low-Resource Language Modeling for Central Asian Languages",
                u("zhomart"));
        if (nurasyl != null) proj1.addParticipant(u("nurasyl"));
        if (aigerim != null) proj1.addParticipant(u("aigerim"));
        proj1.recordPublication(p1.id());
        proj1.recordPublication(p4.id());
        ctx.projectRepository.save(proj1);

        ResearchProject proj2 = new ResearchProject(ctx.projectIds.next(),
                new JournalName("ACL Anthology"),
                "Kazakh NLP Resources and Benchmarks",
                u("zhomart"));
        if (nurasyl != null) proj2.addParticipant(u("nurasyl"));
        proj2.recordPublication(p2.id());
        proj2.recordPublication(p3.id());
        proj2.recordPublication(p6.id());
        ctx.projectRepository.save(proj2);

        ResearchProject proj3 = new ResearchProject(ctx.projectIds.next(),
                new JournalName("Nature Machine Intelligence"),
                "Multilingual Foundation Models for Instruction Following",
                u("zhomart"));
        if (aigerim != null) proj3.addParticipant(u("aigerim"));
        proj3.recordPublication(p5.id());
        ctx.projectRepository.save(proj3);
    }

    // ── HELPERS ──────────────────────────────────────────────────────────────

    private void save(domain.user.User u) { ctx.userRepository.save(u); }
    private Username u(String s) { return new Username(s); }
    private String h(String s) { return ctx.passwordHasher.hash(s); }
    private PersonName n(String f, String l) { return new PersonName(f, l); }
    private Email e(String a) { return new Email(a); }
    private LocalDate d(int y, int m, int day) { return LocalDate.of(y, m, day); }
    private Money m(int v) { return new Money(v); }

    private Course c(String name, int credits, DisciplineType t, int cap) {
        return new Course(CourseId.of(ctx.courseIds.next()), name, new Credits(credits), t, new Capacity(cap));
    }
    private Lesson l(LessonType t, WeekDay day, String time, String room) {
        return new Lesson(t, new TimeSlot(day, time), new Room(room));
    }
    private Book bk(String title, String author) {
        return new Book(new BookId(ctx.bookIds.next()), title, author);
    }
    private News news(String title, String body, Username author, boolean pinned) {
        return new News(ctx.newsIds.next(), title, body, author, pinned);
    }
    private void msg(Username from, Username to, String subject, String body,
                     UrgencyLevel urgency, LocalDateTime sentAt) {
        ctx.messageRepository.save(new Message(ctx.messageIds.next(), from, to, subject, body, urgency, sentAt));
    }
    private LocalDateTime ago(int days, int hour, int min) {
        return LocalDateTime.now().minusDays(days).withHour(hour).withMinute(min).withSecond(0);
    }
    private void markRead(java.util.List<Message> msgs) {
        msgs.forEach(msg -> { msg.markRead(); ctx.messageRepository.save(msg); });
    }
    private ResearchPaper paper(String title, Username author, String journal,
                                String abs, int pages, String doi) {
        ResearchPaper p = new ResearchPaper(new PaperId(ctx.paperIds.next()),
                title, author, new JournalName(journal), abs, pages, doi);
        ctx.paperRepository.save(p);
        return p;
    }
}
