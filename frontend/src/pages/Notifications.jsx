import { useEffect, useState } from "react";
import { useI18n } from "../context/I18nContext.jsx";
import { useToast } from "../components/Toast.jsx";
import * as api from "../api/index.js";

export function Notifications() {
  const { t } = useI18n();
  const { toast, Toasts } = useToast();
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  const load = () => api.listNotifications()
    .then(n => setNotifications(Array.isArray(n) ? n : []))
    .catch(() => setNotifications([]))
    .finally(() => setLoading(false));

  useEffect(() => { load(); }, []);

  async function handleClear() {
    try {
      await api.clearNotifications();
      toast(t("ui.mark_read"));
      window.dispatchEvent(new Event("notifications-cleared"));
      load();
    } catch (e) {
      toast(t("ui.error_generic"), "error");
    }
  }

  if (loading) return <div className="page"><div className="spinner" /></div>;

  return (
    <div className="page">
      <Toasts />
      <div className="page-header flex-between">
        <div>
          <h1>{t("ui.notifications")}</h1>
          <p>{notifications.length} {t("ui.notifications").toLowerCase()}</p>
        </div>
        {notifications.length > 0 && (
          <button className="btn btn-secondary" onClick={handleClear}>
            ✓ {t("ui.mark_read")}
          </button>
        )}
      </div>

      {notifications.length === 0 ? (
        <div className="empty">
          <div className="empty-icon">🔔</div>
          <p>{t("ui.no_notifications")}</p>
        </div>
      ) : (
        <div className="card">
          <div style={{ display: "flex", flexDirection: "column", gap: 0 }}>
            {notifications.map((n, i) => (
              <div key={i} style={{
                padding: "14px 20px",
                borderBottom: i < notifications.length - 1 ? "1px solid var(--border)" : "none",
                display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: 16
              }}>
                <div style={{ flex: 1 }}>
                  <p style={{ fontSize: 14, color: "var(--text)", lineHeight: 1.6 }}>{n.text}</p>
                </div>
                {n.at && (
                  <span className="text-muted text-sm" style={{ whiteSpace: "nowrap", marginTop: 2 }}>
                    {n.at.slice(0, 16).replace("T", " ")}
                  </span>
                )}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
