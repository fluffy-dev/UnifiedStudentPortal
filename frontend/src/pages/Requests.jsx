import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";
import { useI18n } from "../context/I18nContext.jsx";
import { Badge } from "../components/Badge.jsx";
import { Modal } from "../components/Modal.jsx";
import { useToast } from "../components/Toast.jsx";
import * as api from "../api/index.js";

const HELP_TYPES = [
  "TRANSCRIPT_FOR_SEMESTER",
  "TRANSCRIPT_FOR_YEAR",
  "CERTIFICATE_OF_EDUCATION",
  "ACADEMIC_MOBILITY",
  "COORDINATION_OF_DIPLOMA_TOPIC",
  "REQUEST_FOR_CREATING_ORGANIZATION",
];
const URGENCIES = ["LOW", "MEDIUM", "HIGH"];

export function Requests() {
  const { auth } = useAuth();
  const { t } = useI18n();
  const { toast, Toasts } = useToast();
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [viewing, setViewing] = useState(null);
  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("ALL");
  const [form, setForm] = useState({ title: "", body: "", type: HELP_TYPES[0], urgency: "MEDIUM" });

  const canProcess = ["Manager", "Dean"].includes(auth?.role);

  const load = () => api.listRequests().then(all => {
    // Non-managers only see their own requests
    const visible = canProcess ? all : all.filter(r => r.requester === auth?.username);
    setRequests(visible);
  }).finally(() => setLoading(false));
  useEffect(() => { load(); }, []);

  const viewingItem = useMemo(() => requests.find(r => r.id === viewing) ?? null, [requests, viewing]);

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      await api.submitRequest(form);
      toast(t("ui.request_submitted"));
      setShowModal(false);
      setForm({ title: "", body: "", type: HELP_TYPES[0], urgency: "MEDIUM" });
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  async function decide(id, status) {
    try {
      await api.processRequest(id, status);
      toast(status === "APPROVED" ? t("ui.request_approved") : t("ui.request_rejected"));
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    return requests.filter(r => {
      if (statusFilter !== "ALL" && r.status !== statusFilter) return false;
      if (!q) return true;
      return (
        r.title?.toLowerCase().includes(q) ||
        r.body?.toLowerCase().includes(q) ||
        r.requester?.toLowerCase().includes(q) ||
        r.requesterFullName?.toLowerCase().includes(q) ||
        t(r.type)?.toLowerCase().includes(q)
      );
    });
  }, [requests, search, statusFilter, t]);

  if (loading) return <div className="page"><div className="spinner" /></div>;

  return (
    <div className="page">
      <Toasts />
      <div className="page-header flex-between">
        <div>
          <h1>{t("ui.help_requests")}</h1>
          <p>{t("ui.0_requests", requests.length)}</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)}>＋ {t("ui.new_request")}</button>
      </div>

      <div style={{ display: "flex", gap: 12, marginBottom: 16, flexWrap: "wrap" }}>
        <input
          className="form-control"
          style={{ maxWidth: 360, flex: 1, minWidth: 200 }}
          placeholder={t("ui.search_requests")}
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <select
          className="form-control"
          style={{ maxWidth: 200 }}
          value={statusFilter}
          onChange={e => setStatusFilter(e.target.value)}
        >
          <option value="ALL">{t("ui.status")}: {t("ui.all")}</option>
          {["PENDING", "APPROVED", "REJECTED"].map(s =>
            <option key={s} value={s}>{t(s)}</option>
          )}
        </select>
      </div>

      <div className="card-grid">
        {filtered.map(r => (
          <div key={r.id} className="card card-sm" style={{ cursor: "pointer" }} onClick={() => setViewing(r.id)}>
            <div className="flex-between">
              <span className="fw-600">{r.title}</span>
              <Badge tone={r.status} label={t(r.status)} />
            </div>
            <div className="text-muted text-sm mt-1">
              {t(r.type)} · <Badge tone={r.urgency} label={t(r.urgency)} />
            </div>
            <div className="text-muted text-sm mt-1">
              {t("ui.by")} <strong>{r.requesterFullName || r.requester}</strong>
              {r.requesterFullName && <span> (@{r.requester})</span>}
              {" · "}
              {r.createdAt?.slice(0, 16).replace("T", " ")}
            </div>
            {r.body && (
              <p className="text-muted text-sm mt-1" style={{ whiteSpace: "pre-wrap", overflow: "hidden", display: "-webkit-box", WebkitLineClamp: 2, WebkitBoxOrient: "vertical" }}>
                {r.body}
              </p>
            )}

            <div className="flex-between mt-2" style={{ gap: 8, flexWrap: "wrap" }}>
              <button className="btn btn-secondary btn-sm" onClick={e => { e.stopPropagation(); setViewing(r.id); }}>
                🔍 {t("ui.read_more")}
              </button>
              {canProcess && r.status === "PENDING" && (
                <div style={{ display: "flex", gap: 6 }}>
                  <button className="btn btn-primary btn-sm" onClick={e => { e.stopPropagation(); decide(r.id, "APPROVED"); }}>
                    ✓ {t("ui.approve")}
                  </button>
                  <button className="btn btn-secondary btn-sm" onClick={e => { e.stopPropagation(); decide(r.id, "REJECTED"); }}>
                    ✗ {t("ui.reject")}
                  </button>
                </div>
              )}
            </div>
          </div>
        ))}
        {filtered.length === 0 && (
          <div className="empty"><div className="empty-icon">🙋</div><p>{t("ui.no_requests")}</p></div>
        )}
      </div>

      {viewingItem && (
        <RequestDetailModal
          r={viewingItem}
          t={t}
          canProcess={canProcess}
          onClose={() => setViewing(null)}
          onDecide={(status) => { decide(viewingItem.id, status); setViewing(null); }}
        />
      )}

      {showModal && (
        <Modal title={t("ui.submit_help_request")} onClose={() => setShowModal(false)}
          actions={<>
            <button className="btn btn-secondary" onClick={() => setShowModal(false)}>{t("common.back")}</button>
            <button className="btn btn-primary" form="req-form">{t("ui.submit")}</button>
          </>}>
          <form id="req-form" onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.title")}</label>
              <input className="form-control" required value={form.title}
                     onChange={e => setForm({ ...form, title: e.target.value })} />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.type")}</label>
              <select className="form-control" value={form.type}
                      onChange={e => setForm({ ...form, type: e.target.value })}>
                {HELP_TYPES.map(opt => <option key={opt} value={opt}>{t(opt)}</option>)}
              </select>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.urgency")}</label>
              <select className="form-control" value={form.urgency}
                      onChange={e => setForm({ ...form, urgency: e.target.value })}>
                {URGENCIES.map(opt => <option key={opt} value={opt}>{t(opt)}</option>)}
              </select>
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.description")}</label>
              <textarea className="form-control" rows="4" value={form.body}
                        onChange={e => setForm({ ...form, body: e.target.value })} />
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}

function RequestDetailModal({ r, t, canProcess, onClose, onDecide }) {
  const authorLabel = r.requesterFullName
    ? `${r.requesterFullName} (@${r.requester})`
    : r.requester;
  const createdLabel = r.createdAt?.slice(0, 16).replace("T", " ");

  return (
    <Modal title={r.title} onClose={onClose}
      actions={<>
        {canProcess && r.status === "PENDING" && (
          <>
            <button className="btn btn-primary btn-sm" onClick={() => onDecide("APPROVED")}>
              ✓ {t("ui.approve")}
            </button>
            <button className="btn btn-secondary btn-sm" onClick={() => onDecide("REJECTED")}>
              ✗ {t("ui.reject")}
            </button>
          </>
        )}
        <button className="btn btn-secondary" onClick={onClose}>{t("common.back")}</button>
      </>}>
      <div style={{ display: "flex", flexDirection: "column", gap: 14 }}>
        <div style={{ display: "flex", gap: 8, flexWrap: "wrap", alignItems: "center" }}>
          <Badge tone={r.status} label={t(r.status)} />
          <Badge tone={r.urgency} label={t(r.urgency)} />
          <span className="text-muted text-sm">{t(r.type)}</span>
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
          <div className="text-muted text-sm">
            <strong>{t("ui.by")}</strong> {authorLabel}
          </div>
          {createdLabel && (
            <div className="text-muted text-sm">
              <strong>{t("ui.created")}:</strong> {createdLabel}
            </div>
          )}
        </div>

        {r.body ? (
          <div style={{ borderTop: "1px solid var(--border)", paddingTop: 12 }}>
            <div className="fw-600" style={{ marginBottom: 6, fontSize: 13 }}>{t("ui.description")}</div>
            <p style={{ whiteSpace: "pre-wrap", lineHeight: 1.8, color: "var(--text-2)", fontSize: 14 }}>
              {r.body}
            </p>
          </div>
        ) : (
          <p className="text-muted text-sm">{t("ui.no_description")}</p>
        )}
      </div>
    </Modal>
  );
}
