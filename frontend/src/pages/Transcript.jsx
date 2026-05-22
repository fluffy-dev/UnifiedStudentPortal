import { useEffect, useState } from "react";
import { useI18n } from "../context/I18nContext.jsx";
import { Badge } from "../components/Badge.jsx";
import * as api from "../api/index.js";

function GpaBar({ gpa }) {
  const pct = Math.min((gpa / 4.0) * 100, 100);
  const color = gpa >= 3.5 ? "#10b981" : gpa >= 2.5 ? "#6366f1" : gpa >= 1.5 ? "#f59e0b" : "#ef4444";
  return (
    <div style={{ marginTop: 6 }}>
      <div style={{
        height: 6, borderRadius: 3, background: "var(--border)",
        overflow: "hidden", width: "100%", maxWidth: 120, margin: "0 auto"
      }}>
        <div style={{ height: "100%", width: `${pct}%`, background: color, borderRadius: 3, transition: "width 0.6s ease" }} />
      </div>
      <div style={{ fontSize: 10, color: "var(--text-3)", marginTop: 2, textAlign: "center" }}>/ 4.0</div>
    </div>
  );
}

export function Transcript() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { t } = useI18n();

  useEffect(() => {
    api.transcript()
      .then(setData)
      .catch(e => setError(e.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="page"><div className="spinner" /></div>;
  if (error)   return <div className="page"><div className="empty"><div className="empty-icon">⚠️</div><p>{t("ui.error_loading")}</p></div></div>;
  if (!data)   return <div className="page"><div className="empty"><p>{t("ui.no_transcript_data")}</p></div></div>;

  const graded   = (data.courses || []).filter(c => c.letter && c.letter !== "—");
  const ungraded = (data.courses || []).filter(c => !c.letter || c.letter === "—");
  const passing  = graded.filter(c => c.passing).length;

  return (
    <div className="page">
      <div className="page-header">
        <h1>{t("student.menu.transcript")}</h1>
        <p>{data.student} · {t(data.degree)} · {t("ui.year")} {data.year}</p>
      </div>

      {/* Stats */}
      <div className="stat-grid" style={{ marginBottom: 24 }}>
        <div className="stat-card">
          <div className="stat-value">{(data.gpa ?? 0).toFixed(2)}</div>
          <GpaBar gpa={data.gpa ?? 0} />
          <div className="stat-label">{t("ui.gpa")}</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{data.failCount}</div>
          <div className="stat-label">{t("ui.failed_courses")}</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{graded.length}</div>
          <div className="stat-label">{t("ui.courses_graded")}</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{passing}</div>
          <div className="stat-label">{t("ui.courses_passing")}</div>
        </div>
      </div>

      {/* Graded courses */}
      {graded.length > 0 && (
        <div className="card" style={{ marginBottom: 16 }}>
          <div className="section-title">{t("ui.graded_courses")}</div>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>{t("ui.course")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.1st_att")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.2nd_att")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.exam")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.total")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.grade")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.passing")}</th>
                </tr>
              </thead>
              <tbody>
                {graded.map((c, i) => {
                  const hasBreakdown = c.firstHalf != null && c.firstHalf >= 0;
                  const attTotal = hasBreakdown ? (c.firstHalf + c.secondHalf) : null;
                  const admitted = attTotal == null || attTotal >= 30;
                  return (
                    <tr key={i}>
                      <td className="fw-600">{c.course}</td>
                      <td style={{ textAlign: "center" }}>
                        {hasBreakdown ? (
                          <span style={{ fontVariantNumeric: "tabular-nums" }}>{c.firstHalf}</span>
                        ) : "—"}
                      </td>
                      <td style={{ textAlign: "center" }}>
                        {hasBreakdown ? (
                          <span style={{ fontVariantNumeric: "tabular-nums" }}>{c.secondHalf}</span>
                        ) : "—"}
                      </td>
                      <td style={{ textAlign: "center" }}>
                        {hasBreakdown ? (
                          admitted
                            ? <span style={{ fontVariantNumeric: "tabular-nums" }}>{c.exam}</span>
                            : <span className="badge badge-red" title={t("ui.not_admitted_hint")}>—</span>
                        ) : "—"}
                      </td>
                      <td style={{ textAlign: "center", fontWeight: 600 }}>
                        {c.total > 0 ? c.total : "—"}
                      </td>
                      <td style={{ textAlign: "center" }}><Badge label={c.letter} /></td>
                      <td style={{ textAlign: "center" }}>
                        <Badge
                          tone={c.passing ? "PASSING" : (c.letter === "FX" ? "FAILING" : "FAILING")}
                          label={c.passing ? t("PASSING") : (c.letter === "FX" ? t("ui.retake_exam") : t("FAILING"))}
                        />
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* In-progress / ungraded courses */}
      {ungraded.length > 0 && (
        <div className="card">
          <div className="section-title">{t("ui.courses_in_progress")}</div>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>{t("ui.course")}</th>
                  <th style={{ textAlign: "center" }}>{t("ui.status")}</th>
                </tr>
              </thead>
              <tbody>
                {ungraded.map((c, i) => (
                  <tr key={i}>
                    <td className="fw-600">{c.course}</td>
                    <td style={{ textAlign: "center" }}>
                      <span className="badge badge-blue">{t("ui.in_progress")}</span>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {graded.length === 0 && ungraded.length === 0 && (
        <div className="empty">
          <div className="empty-icon">📄</div>
          <p>{t("ui.no_transcript_data")}</p>
        </div>
      )}

      {/* GPA scale legend */}
      <div style={{
        marginTop: 20, padding: "12px 16px", borderRadius: 10,
        background: "var(--bg-3)", border: "1px solid var(--border)",
        display: "flex", gap: 20, flexWrap: "wrap", fontSize: 12, color: "var(--text-2)"
      }}>
        <span style={{ fontWeight: 600, color: "var(--text)" }}>GPA scale (4.0):</span>
        {[["A","4.0","#10b981"],["B","3.0","#6366f1"],["C","2.0","#f59e0b"],["D","1.0","#f59e0b"],["FX/F","0.0","#ef4444"]].map(([l,p,c]) => (
          <span key={l} style={{ display: "flex", alignItems: "center", gap: 4 }}>
            <span style={{ background: c, color: "#fff", borderRadius: 4, padding: "1px 6px", fontSize: 11, fontWeight: 700 }}>{l}</span>
            = {p}
          </span>
        ))}
      </div>
    </div>
  );
}
