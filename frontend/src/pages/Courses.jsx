import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";
import { useI18n } from "../context/I18nContext.jsx";
import { Badge } from "../components/Badge.jsx";
import { Modal } from "../components/Modal.jsx";
import { UserPicker } from "../components/UserPicker.jsx";
import { useToast } from "../components/Toast.jsx";
import * as api from "../api/index.js";

const LESSON_TYPES = ["LECTURE", "PRACTICE", "OFFICE_HOURS", "EXAM"];
const WEEK_DAYS    = ["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY"];

export function Courses() {
  const { auth } = useAuth();
  const { t } = useI18n();
  const { toast, Toasts } = useToast();
  const role = auth?.role;
  const isStudent = ["Student", "GraduateStudent"].includes(role);
  const isManager = role === "Manager";
  const [courses, setCourses] = useState([]);
  const [directory, setDirectory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [showCreate, setShowCreate] = useState(false);
  const [viewing, setViewing] = useState(null);
  const [assigningCourse, setAssigningCourse] = useState(null);
  const [lessonCourse, setLessonCourse] = useState(null);
  const [form, setForm] = useState({ name: "", credits: 3, type: "MAJOR", capacity: 30 });
  const [assignForm, setAssignForm] = useState({ teacherUsername: "" });
  const [lessonForm, setLessonForm] = useState({ type: "LECTURE", day: "MONDAY", time: "", room: "" });

  const teachers = directory.filter(u => u.role === "Teacher");

  const load = () => api.listCourses().then(setCourses).finally(() => setLoading(false));
  useEffect(() => {
    load();
    if (isManager) api.userDirectory().then(setDirectory).catch(() => {});
  }, []);

  async function handleEnroll(c) {
    try { await api.enroll(c.id); toast(t("enroll.success", c.name)); load(); }
    catch (e) { toast(t(e?.message || "ui.error_generic"), "error"); }
  }
  async function handleDrop(c) {
    try { await api.drop(c.id); toast(t("drop.success", c.name)); load(); }
    catch (e) { toast(t(e?.message || "ui.error_generic"), "error"); }
  }
  async function handleCreate(e) {
    e.preventDefault();
    try { await api.createCourse(form); toast(t("course.created", form.name)); setShowCreate(false); load(); }
    catch (e) { toast(t(e?.message || "ui.error_generic"), "error"); }
  }
  async function handleAssignTeacher(e) {
    e.preventDefault();
    if (!assignForm.teacherUsername) { toast(t("ui.select_teacher"), "error"); return; }
    try {
      await api.assignTeacher(assigningCourse.id, assignForm);
      toast(t("ui.teacher_assigned"));
      setAssigningCourse(null);
      load();
    } catch (e) { toast(t(e?.message || "ui.error_generic"), "error"); }
  }
  async function handleAddLesson(e) {
    e.preventDefault();
    if (!lessonForm.time || !lessonForm.room) { toast(t("ui.error_generic"), "error"); return; }
    try {
      await api.addLesson(lessonCourse.id, lessonForm);
      toast(t("ui.lesson_added"));
      setLessonCourse(null);
      setLessonForm({ type: "LECTURE", day: "MONDAY", time: "", room: "" });
      load();
    } catch (e) { toast(t(e?.message || "ui.error_generic"), "error"); }
  }

  const filtered = courses.filter(c =>
    c.name?.toLowerCase().includes(search.toLowerCase()) ||
    c.id?.toLowerCase().includes(search.toLowerCase())
  );

  if (loading) return <div className="page"><div className="spinner" /></div>;

  const viewingCourse = viewing ? courses.find(c => c.id === viewing) : null;

  return (
    <div className="page">
      <Toasts />
      <div className="page-header flex-between">
        <div><h1>{t("ui.courses_1")}</h1><p>{t("ui.0_courses_available", courses.length)}</p></div>
        {role === "Manager" && (
          <button className="btn btn-primary" onClick={() => setShowCreate(true)}>＋ {t("ui.new_course")}</button>
        )}
      </div>

      <input
        className="form-control mb-3"
        style={{ maxWidth: 320 }}
        placeholder={t("ui.search_courses")}
        value={search}
        onChange={e => setSearch(e.target.value)}
      />

      <div className="card-grid">
        {filtered.map(c => {
          const enrolled = isStudent && Array.isArray(c.students) && c.students.includes(auth?.username);
          const isFull = c.remainingSeats === 0;
          return (
            <div key={c.id} className="card card-sm" style={{ display: "flex", flexDirection: "column", gap: 8 }}>
              <div className="flex-between">
                <span className="fw-600">{c.name}</span>
                <div style={{ display: "flex", gap: 4 }}>
                  {enrolled && <Badge tone="PASSING" label={t("ui.enrolled")} />}
                  <Badge tone={c.type} label={t(c.type)} />
                </div>
              </div>
              <div className="text-muted text-sm">
                {t("ui.id")}: {c.id} · {c.credits} {t("ui.credits")} · {c.remainingSeats}/{c.capacity} {t("ui.seats")}
              </div>
              {Array.isArray(c.lessons) && c.lessons.length > 0 && (
                <div className="text-muted text-sm">
                  {c.lessons.length} {t("ui.lessons")}
                  {" "}
                  <button
                    className="btn btn-link"
                    style={{ padding: 0, fontSize: 12 }}
                    onClick={() => setViewing(c.id)}
                  >
                    {t("ui.view_schedule")}
                  </button>
                </div>
              )}
              <div style={{ display: "flex", gap: 8, marginTop: 4, flexWrap: "wrap" }}>
                {isStudent && !enrolled && !isFull && (
                  <button className="btn btn-primary btn-sm" onClick={() => handleEnroll(c)}>{t("ui.enroll")}</button>
                )}
                {isStudent && enrolled && (
                  <button className="btn btn-secondary btn-sm" onClick={() => handleDrop(c)}>{t("ui.drop")}</button>
                )}
                {isStudent && !enrolled && isFull && <Badge label={t("ui.full")} />}
                {isManager && (
                  <>
                    <button className="btn btn-secondary btn-sm" onClick={() => { setAssigningCourse(c); setAssignForm({ teacherUsername: "" }); }}>
                      👤 {t("ui.assign_teacher")}
                    </button>
                    <button className="btn btn-secondary btn-sm" onClick={() => { setLessonCourse(c); }}>
                      ＋ {t("ui.add_lesson")}
                    </button>
                  </>
                )}
              </div>
            </div>
          );
        })}
        {filtered.length === 0 && (
          <div className="empty"><div className="empty-icon">📚</div><p>{t("course.list.empty")}</p></div>
        )}
      </div>

      {viewingCourse && viewingCourse.lessons?.length > 0 && (
        <Modal title={`${viewingCourse.name} — ${t("ui.schedule")}`} onClose={() => setViewing(null)}
          actions={<button className="btn btn-secondary" onClick={() => setViewing(null)}>{t("common.back")}</button>}>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>{t("ui.lesson_type")}</th>
                  <th>{t("ui.day")}</th>
                  <th>{t("ui.time")}</th>
                  <th>{t("ui.room")}</th>
                </tr>
              </thead>
              <tbody>
                {viewingCourse.lessons.map((l, i) => (
                  <tr key={i}>
                    <td><Badge label={t(l.type) || l.type} /></td>
                    <td>{l.day}</td>
                    <td>{l.time}</td>
                    <td>{l.room}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Modal>
      )}

      {showCreate && (
        <Modal title={t("manager.menu.create_course")} onClose={() => setShowCreate(false)}
          actions={<>
            <button className="btn btn-secondary" onClick={() => setShowCreate(false)}>{t("common.back")}</button>
            <button className="btn btn-primary" form="create-course-form">{t("ui.create")}</button>
          </>}>
          <form id="create-course-form" onSubmit={handleCreate} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.course_name")}</label>
              <input className="form-control" required value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
            </div>
            <div className="form-row">
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>{t("ui.credits_1")}</label>
                <input className="form-control" type="number" min="1" max="10" value={form.credits} onChange={e => setForm({ ...form, credits: +e.target.value })} />
              </div>
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>{t("ui.capacity")}</label>
                <input className="form-control" type="number" min="1" value={form.capacity} onChange={e => setForm({ ...form, capacity: +e.target.value })} />
              </div>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.type")}</label>
              <select className="form-control" value={form.type} onChange={e => setForm({ ...form, type: e.target.value })}>
                {["MAJOR", "MINOR", "FREE"].map(opt => <option key={opt} value={opt}>{t(opt)}</option>)}
              </select>
            </div>
          </form>
        </Modal>
      )}

      {assigningCourse && (
        <Modal title={`${t("ui.assign_teacher")} — ${assigningCourse.name}`} onClose={() => { setAssigningCourse(null); setAssignForm({ teacherUsername:"" }); }}
          actions={<>
            <button className="btn btn-secondary" onClick={() => { setAssigningCourse(null); setAssignForm({ teacherUsername:"" }); }}>{t("common.back")}</button>
            <button className="btn btn-primary" form="assign-form">{t("ui.save")}</button>
          </>}>
          <form id="assign-form" onSubmit={handleAssignTeacher} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.select_teacher")}</label>
              <UserPicker
                users={teachers}
                value={assignForm.teacherUsername}
                onChange={v => setAssignForm({ teacherUsername: v })}
                placeholder={t("ui.search_users")}
              />
            </div>
            {assigningCourse.teachers?.length > 0 && (
              <div className="text-muted text-sm">
                {t("ui.current")}: {assigningCourse.teachers.join(", ")}
              </div>
            )}
          </form>
        </Modal>
      )}

      {lessonCourse && (
        <Modal title={`${t("ui.add_lesson")} — ${lessonCourse.name}`} onClose={() => { setLessonCourse(null); setLessonForm({ type:"LECTURE", day:"MONDAY", time:"", room:"" }); }}
          actions={<>
            <button className="btn btn-secondary" onClick={() => { setLessonCourse(null); setLessonForm({ type:"LECTURE", day:"MONDAY", time:"", room:"" }); }}>{t("common.back")}</button>
            <button className="btn btn-primary" form="lesson-form">{t("ui.add")}</button>
          </>}>
          <form id="lesson-form" onSubmit={handleAddLesson} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.lesson_type")}</label>
              <select className="form-control" value={lessonForm.type} onChange={e => setLessonForm({ ...lessonForm, type: e.target.value })}>
                {LESSON_TYPES.map(lt => <option key={lt} value={lt}>{t(lt)}</option>)}
              </select>
            </div>
            <div className="form-row">
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>{t("ui.day")}</label>
                <select className="form-control" value={lessonForm.day} onChange={e => setLessonForm({ ...lessonForm, day: e.target.value })}>
                  {WEEK_DAYS.map(d => <option key={d} value={d}>{t(d)}</option>)}
                </select>
              </div>
              <div className="form-group" style={{ marginBottom: 0 }}>
                <label>{t("ui.lesson_time")}</label>
                <input className="form-control" required placeholder={t("ui.lesson_time_hint")} value={lessonForm.time}
                       onChange={e => setLessonForm({ ...lessonForm, time: e.target.value })} />
              </div>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.room")}</label>
              <input className="form-control" required value={lessonForm.room}
                     onChange={e => setLessonForm({ ...lessonForm, room: e.target.value })} />
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}
