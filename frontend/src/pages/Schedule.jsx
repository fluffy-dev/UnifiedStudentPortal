import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";
import { useI18n } from "../context/I18nContext.jsx";
import * as api from "../api/index.js";

const WEEK_DAYS = ["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"];
const SHORT_DAYS = {
  MONDAY:"Mon", TUESDAY:"Tue", WEDNESDAY:"Wed", THURSDAY:"Thu",
  FRIDAY:"Fri", SATURDAY:"Sat", SUNDAY:"Sun"
};

const TYPE_COLORS = {
  LECTURE:      { bg:"rgba(99,102,241,0.12)",  border:"#6366f1", text:"#4338ca" },
  PRACTICE:     { bg:"rgba(16,185,129,0.12)",   border:"#10b981", text:"#047857" },
  OFFICE_HOURS: { bg:"rgba(245,158,11,0.12)",   border:"#f59e0b", text:"#b45309" },
  EXAM:         { bg:"rgba(239,68,68,0.12)",     border:"#ef4444", text:"#b91c1c" },
  DEFAULT:      { bg:"rgba(107,114,128,0.1)",    border:"#9ca3af", text:"#4b5563" },
};
function typeColor(type) { return TYPE_COLORS[type] || TYPE_COLORS.DEFAULT; }

export function Schedule() {
  const { auth } = useAuth();
  const { t } = useI18n();
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const isTeacher = auth?.role === "Teacher";

  useEffect(() => {
    api.listCourses().then(setCourses).finally(() => setLoading(false));
  }, []);

  const myCourses = useMemo(() => {
    if (!auth?.username) return [];
    return courses.filter(c => {
      if (!Array.isArray(c.lessons) || c.lessons.length === 0) return false;
      if (isTeacher) return Array.isArray(c.teachers) && c.teachers.includes(auth.username);
      return Array.isArray(c.students) && c.students.includes(auth.username);
    });
  }, [courses, auth, isTeacher]);

  const byDay = useMemo(() => {
    const map = {};
    for (const day of WEEK_DAYS) map[day] = [];
    for (const course of myCourses) {
      for (const lesson of (course.lessons || [])) {
        const day = lesson.day;
        if (!day || !WEEK_DAYS.includes(day)) continue; // skip malformed
        map[day].push({ ...lesson, courseName: course.name, courseId: course.id });
      }
    }
    for (const day of WEEK_DAYS) {
      map[day].sort((a, b) => (a.time || "").localeCompare(b.time || ""));
    }
    return map;
  }, [myCourses]);

  // Unique times sorted; lessons with no time get a fallback bucket "—"
  const allTimes = useMemo(() => {
    const set = new Set();
    for (const day of WEEK_DAYS)
      for (const l of byDay[day]) set.add(l.time || "—");
    return [...set].sort((a, b) =>
      a === "—" ? 1 : b === "—" ? -1 : a.localeCompare(b)
    );
  }, [byDay]);

  const activeDays = WEEK_DAYS.filter(d => byDay[d]?.length > 0);
  const totalLessons = myCourses.reduce((s, c) => s + (c.lessons?.length ?? 0), 0);

  if (loading) return <div className="page"><div className="spinner" /></div>;

  const emptyHint = isTeacher ? t("ui.no_lessons_teacher") : t("ui.enroll_to_see_schedule");

  if (activeDays.length === 0) {
    return (
      <div className="page" style={{ display:"flex", alignItems:"center", justifyContent:"center" }}>
        <div style={{ textAlign:"center", padding:"60px 20px" }}>
          <div style={{ fontSize:64, marginBottom:16 }}>📅</div>
          <h2 style={{ fontSize:20, fontWeight:700, marginBottom:8, color:"var(--text)" }}>
            {t("ui.no_lessons_scheduled")}
          </h2>
          <p style={{ color:"var(--text-2)", fontSize:14 }}>{emptyHint}</p>
        </div>
      </div>
    );
  }

  const timeColStyle = (rowIdx) => ({
    padding: "10px 12px",
    fontSize: 12, fontWeight: 600, color: "var(--text-2)",
    textAlign: "center", whiteSpace: "nowrap",
    borderBottom: "1px solid var(--border)",
    position: "sticky", left: 0, zIndex: 1,
    background: rowIdx % 2 === 0 ? "var(--bg-2)" : "var(--bg-3)",
  });

  return (
    <div className="page">
      <div className="page-header">
        <h1>{t("student.menu.schedule")}</h1>
        <p>{myCourses.length} {t("ui.courses_1").toLowerCase()} &middot; {totalLessons} {t("ui.lessons")}</p>
      </div>

      {/* Legend */}
      <div style={{ display:"flex", gap:16, marginBottom:20, flexWrap:"wrap" }}>
        {Object.entries(TYPE_COLORS).filter(([k]) => k !== "DEFAULT").map(([type, c]) => (
          <div key={type} style={{ display:"flex", alignItems:"center", gap:6, fontSize:12 }}>
            <div style={{ width:10, height:10, borderRadius:2, background:c.border }} aria-hidden="true" />
            <span style={{ color:"var(--text-2)" }}>{t(type) || type}</span>
          </div>
        ))}
      </div>

      {/* Timetable */}
      <div style={{ overflow:"hidden", borderRadius:"var(--radius)", boxShadow:"var(--shadow-sm)", border:"1px solid var(--border)", marginBottom:24 }}>
        <div style={{ overflowX:"auto" }}>
          <table
            aria-label={t("ui.schedule")}
            style={{ width:"100%", borderCollapse:"collapse", minWidth: activeDays.length * 150 + 90 }}
          >
            <thead>
              <tr>
                <th scope="col" style={{
                  padding:"12px 16px", fontSize:11, fontWeight:600, textTransform:"uppercase",
                  letterSpacing:"0.06em", color:"var(--text-2)", background:"var(--bg-3)",
                  borderBottom:"2px solid var(--border)", width:90, textAlign:"center",
                  position:"sticky", left:0, zIndex:3
                }}>
                  {t("ui.time")}
                </th>
                {activeDays.map(day => (
                  <th key={day} scope="col" style={{
                    padding:"12px 16px", fontSize:12, fontWeight:600, textTransform:"uppercase",
                    letterSpacing:"0.06em", color:"var(--text-2)", background:"var(--bg-3)",
                    borderBottom:"2px solid var(--border)", borderLeft:"1px solid var(--border)",
                    textAlign:"center", minWidth:150
                  }}>
                    {t(day) || SHORT_DAYS[day]}
                  </th>
                ))}
              </tr>
            </thead>
            <tbody>
              {allTimes.map((time, ti) => {
                const rowBg = ti % 2 === 0 ? "var(--bg-2)" : "var(--bg-3)";
                return (
                  <tr key={time} style={{ background: rowBg }}>
                    <th scope="row" style={timeColStyle(ti)}>{time}</th>
                    {activeDays.map(day => {
                      const lessons = byDay[day].filter(l => (l.time || "—") === time);
                      return (
                        <td key={day} style={{
                          padding:"6px 8px", borderBottom:"1px solid var(--border)",
                          borderLeft:"1px solid var(--border)", verticalAlign:"top",
                          background: rowBg
                        }}>
                          {lessons.map((lesson, i) => {
                            const c = typeColor(lesson.type);
                            return (
                              <div key={i} style={{
                                background: c.bg, borderLeft:`3px solid ${c.border}`,
                                borderRadius:6, padding:"6px 10px",
                                marginBottom: i < lessons.length - 1 ? 4 : 0
                              }}>
                                <div style={{ fontSize:11, fontWeight:600, color:c.text, marginBottom:2 }}>
                                  {t(lesson.type) || lesson.type}
                                </div>
                                <div style={{ fontSize:12, fontWeight:500, color:"var(--text)", lineHeight:1.4 }}>
                                  {lesson.courseName}
                                </div>
                                {lesson.room && (
                                  <div style={{ fontSize:11, color:"var(--text-2)", marginTop:2 }}>
                                    {lesson.room}
                                  </div>
                                )}
                              </div>
                            );
                          })}
                        </td>
                      );
                    })}
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>

      {/* Course list */}
      <div className="section-title">{t("ui.courses_1")}</div>
      <div className="card-grid" style={{ marginTop:8 }}>
        {myCourses.map(c => (
          <div key={c.id} className="card card-sm" style={{ display:"flex", flexDirection:"column", gap:4 }}>
            <div className="fw-600" style={{ fontSize:14 }}>{c.name}</div>
            <div className="text-muted text-sm">{c.id} &middot; {c.credits} {t("ui.credits")}</div>
            <div style={{ display:"flex", gap:4, flexWrap:"wrap", marginTop:4 }}>
              {(c.lessons || []).filter(l => l.day).map((l, i) => {
                const col = typeColor(l.type);
                return (
                  <span key={i} style={{
                    fontSize:10, padding:"2px 6px", borderRadius:4, fontWeight:500,
                    background:col.bg, color:col.text, border:`1px solid ${col.border}33`
                  }}>
                    {t(l.type) || l.type} &middot; {SHORT_DAYS[l.day]} {l.time}
                  </span>
                );
              })}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
