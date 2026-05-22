import { useEffect, useState } from "react";
import { useI18n } from "../context/I18nContext.jsx";
import { Badge } from "../components/Badge.jsx";
import * as api from "../api/index.js";

export function Transcript() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(true);
  const { t } = useI18n();

  useEffect(() => { api.transcript().then(setData).finally(() => setLoading(false)); }, []);

  if (loading) return <div className="page"><div className="spinner" /></div>;
  if (!data) return <div className="page"><div className="empty"><p>{t("ui.no_transcript_data")}</p></div></div>;

  return (
    <div className="page">
      <div className="page-header">
        <h1>{t("student.menu.transcript")}</h1>
        <p>{data.student} · {t(data.degree)} · {t("ui.year")} {data.year}</p>
      </div>

      <div className="stat-grid" style={{ marginBottom: 24 }}>
        <div className="stat-card">
          <div className="stat-value">{(data.gpa ?? 0).toFixed(2)}</div>
          <div className="stat-label">{t("ui.gpa")}</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{data.failCount}</div>
          <div className="stat-label">{t("ui.failed_courses")}</div>
        </div>
        <div className="stat-card">
          <div className="stat-value">{data.courses?.length ?? 0}</div>
          <div className="stat-label">{t("ui.total_courses")}</div>
        </div>
      </div>

      <div className="card">
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>{t("ui.course")}</th>
                <th>{t("ui.total")}</th>
                <th>{t("ui.grade")}</th>
                <th>{t("ui.passing")}</th>
              </tr>
            </thead>
            <tbody>
              {data.courses?.map((c, i) => {
                const letter = c.letter ?? "—";
                const passing = letter !== "F" && letter !== "FX" && letter !== "—";
                return (
                  <tr key={i}>
                    <td className="fw-600">{c.course}</td>
                    <td>{c.total ?? "—"}</td>
                    <td><Badge label={letter} /></td>
                    <td>
                      <Badge
                        tone={passing ? "PASSING" : "FAILING"}
                        label={t(passing ? "PASSING" : "FAILING")}
                      />
                    </td>
                  </tr>
                );
              })}
              {(!data.courses || data.courses.length === 0) && (
                <tr>
                  <td colSpan="4" style={{ textAlign: "center", color: "var(--text-2)" }}>
                    {t("ui.no_transcript_data")}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
