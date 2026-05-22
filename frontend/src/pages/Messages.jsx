import { useEffect, useMemo, useState } from "react";
import { useI18n } from "../context/I18nContext.jsx";
import { Badge } from "../components/Badge.jsx";
import { Modal } from "../components/Modal.jsx";
import { UserPicker } from "../components/UserPicker.jsx";
import { useToast } from "../components/Toast.jsx";
import * as api from "../api/index.js";

const URGENCIES = ["LOW", "MEDIUM", "HIGH"];

export function Messages() {
  const { toast, Toasts } = useToast();
  const { t } = useI18n();
  const [inbox, setInbox] = useState([]);
  const [sent, setSent] = useState([]);
  const [directory, setDirectory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [tab, setTab] = useState("inbox");
  const [search, setSearch] = useState("");
  const [showCompose, setShowCompose] = useState(false);
  const [viewing, setViewing] = useState(null);
  const [form, setForm] = useState({ recipient: "", subject: "", body: "", urgency: "LOW" });

  const load = () => {
    Promise.all([api.inbox(), api.sentMessages()])
      .then(([inboxData, sentData]) => { setInbox(inboxData); setSent(sentData); })
      .finally(() => setLoading(false));
  };

  useEffect(() => { load(); api.userDirectory().then(setDirectory).catch(() => {}); }, []);

  async function handleSend(e) {
    e.preventDefault();
    try {
      await api.sendMessage(form);
      toast(t("ui.message_sent"));
      setShowCompose(false);
      setForm({ recipient: "", subject: "", body: "", urgency: "LOW" });
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  const msgs = tab === "inbox" ? inbox : sent;

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return msgs;
    return msgs.filter(m =>
      m.subject?.toLowerCase().includes(q) ||
      m.body?.toLowerCase().includes(q) ||
      m.sender?.toLowerCase().includes(q) ||
      m.senderFullName?.toLowerCase().includes(q) ||
      m.recipient?.toLowerCase().includes(q)
    );
  }, [msgs, search]);

  const viewingItem = useMemo(() => {
    if (!viewing) return null;
    return [...inbox, ...sent].find(m => m.id === viewing) ?? null;
  }, [viewing, inbox, sent]);

  if (loading) return <div className="page"><div className="spinner" /></div>;

  return (
    <div className="page">
      <Toasts />
      <div className="page-header flex-between">
        <div>
          <h1>{t("ui.messages_page")}</h1>
          <p>{t("ui.0_in_inbox", inbox.length)}</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowCompose(true)}>
          ✉️ {t("ui.compose")}
        </button>
      </div>

      <div style={{ display: "flex", gap: 8, marginBottom: 16 }}>
        <button
          className={`btn ${tab === "inbox" ? "btn-primary" : "btn-secondary"} btn-sm`}
          onClick={() => { setTab("inbox"); setSearch(""); setViewing(null); }}
        >
          📥 {t("ui.inbox_tab")}
        </button>
        <button
          className={`btn ${tab === "sent" ? "btn-primary" : "btn-secondary"} btn-sm`}
          onClick={() => { setTab("sent"); setSearch(""); setViewing(null); }}
        >
          📤 {t("ui.sent_tab")}
        </button>
      </div>

      <input
        className="form-control mb-3"
        style={{ maxWidth: 360 }}
        placeholder={t("ui.search_messages")}
        value={search}
        onChange={e => setSearch(e.target.value)}
      />

      <div className="card">
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>{t("ui.subject")}</th>
                <th>{tab === "inbox" ? t("ui.from") : t("ui.recipient")}</th>
                <th>{t("ui.urgency")}</th>
                <th>{t("ui.status")}</th>
                <th>{t("ui.sent")}</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(m => (
                <tr key={m.id} style={{ cursor: "pointer" }} onClick={() => setViewing(m.id)}>
                  <td className="fw-600">{m.subject}</td>
                  <td className="text-muted">
                    {tab === "inbox" ? (
                      m.senderFullName
                        ? <><strong>{m.senderFullName}</strong> <span className="text-muted text-sm">@{m.sender}</span></>
                        : m.sender
                    ) : (
                      m.recipient
                    )}
                  </td>
                  <td><Badge tone={m.urgency} label={t(m.urgency)} /></td>
                  <td><Badge tone={m.status} label={t(m.status)} /></td>
                  <td className="text-muted text-sm">{m.sentAt?.slice(0, 16).replace("T", " ")}</td>
                  <td>
                    <button
                      className="btn btn-secondary btn-sm"
                      onClick={e => { e.stopPropagation(); setViewing(m.id); }}
                    >
                      {t("ui.read_more")}
                    </button>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr>
                  <td colSpan="6" style={{ textAlign: "center", color: "var(--text-2)" }}>
                    {tab === "inbox" ? t("inbox.empty") : t("ui.no_sent")}
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {viewingItem && (
        <MessageDetailModal
          m={viewingItem}
          t={t}
          tab={tab}
          onClose={() => setViewing(null)}
        />
      )}

      {showCompose && (
        <Modal title={t("ui.compose")} onClose={() => setShowCompose(false)}
          actions={<>
            <button className="btn btn-secondary" onClick={() => setShowCompose(false)}>{t("common.back")}</button>
            <button className="btn btn-primary" form="compose-form">{t("ui.send")}</button>
          </>}>
          <form id="compose-form" onSubmit={handleSend} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.recipient")}</label>
              <UserPicker
                users={directory}
                value={form.recipient}
                onChange={v => setForm({ ...form, recipient: v })}
                placeholder={t("ui.to_username")}
              />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.subject")}</label>
              <input className="form-control" required value={form.subject}
                     onChange={e => setForm({ ...form, subject: e.target.value })} />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.body")}</label>
              <textarea className="form-control" rows="4" value={form.body}
                        onChange={e => setForm({ ...form, body: e.target.value })} />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.urgency")}</label>
              <select className="form-control" value={form.urgency}
                      onChange={e => setForm({ ...form, urgency: e.target.value })}>
                {URGENCIES.map(u => <option key={u} value={u}>{t(u)}</option>)}
              </select>
            </div>
          </form>
        </Modal>
      )}
    </div>
  );
}

function MessageDetailModal({ m, t, tab, onClose }) {
  const isInbox = tab === "inbox";
  const fromLabel = m.senderFullName
    ? `${m.senderFullName} (@${m.sender})`
    : m.sender;

  return (
    <Modal title={m.subject} onClose={onClose}
      actions={<button className="btn btn-secondary" onClick={onClose}>{t("common.back")}</button>}>
      <div style={{ display: "flex", flexDirection: "column", gap: 14 }}>
        <div style={{ display: "flex", gap: 8, flexWrap: "wrap", alignItems: "center" }}>
          <Badge tone={m.urgency} label={t(m.urgency)} />
          <Badge tone={m.status} label={t(m.status)} />
          {m.sentAt && (
            <span className="text-muted text-sm">{m.sentAt.slice(0, 16).replace("T", " ")}</span>
          )}
        </div>

        <div style={{ display: "flex", flexDirection: "column", gap: 4 }}>
          <div className="text-muted text-sm">
            <strong>{t("ui.from")}:</strong> {fromLabel}
          </div>
          <div className="text-muted text-sm">
            <strong>{t("ui.recipient")}:</strong> {m.recipient}
          </div>
        </div>

        <div style={{ borderTop: "1px solid var(--border)", paddingTop: 12 }}>
          <div className="fw-600" style={{ marginBottom: 6, fontSize: 13 }}>{t("ui.body")}</div>
          <p style={{ whiteSpace: "pre-wrap", lineHeight: 1.8, color: "var(--text-2)", fontSize: 14 }}>
            {m.body || <span className="text-muted text-sm">{t("ui.no_description")}</span>}
          </p>
        </div>
      </div>
    </Modal>
  );
}
