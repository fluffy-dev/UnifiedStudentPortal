import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../context/AuthContext.jsx";
import { useI18n } from "../context/I18nContext.jsx";
import { Modal } from "../components/Modal.jsx";
import { useToast } from "../components/Toast.jsx";
import * as api from "../api/index.js";

const FEED_PREVIEW_CHARS = 200;

const CAN_PIN_ROLES = ["Manager", "Dean"];

export function News() {
  const { auth } = useAuth();
  const { t } = useI18n();
  const { toast, Toasts } = useToast();
  const role = auth?.role;
  const isEmployee = !["Student", "GraduateStudent", "Admin"].includes(role);
  const canPinAny = CAN_PIN_ROLES.includes(role);
  const [news, setNews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showCompose, setShowCompose] = useState(false);
  const [commenting, setCommenting] = useState(null);
  const [comment, setComment] = useState("");
  const [search, setSearch] = useState("");
  const [viewing, setViewing] = useState(null);
  const [form, setForm] = useState({ title: "", body: "", pinned: false });

  const load = () => api.listNews().then(setNews).finally(() => setLoading(false));
  useEffect(() => { load(); }, []);

  const viewingItem = useMemo(() => news.find(n => n.id === viewing) ?? null, [news, viewing]);

  async function handlePublish(e) {
    e.preventDefault();
    try {
      await api.publishNews(form);
      toast(t("ui.news_published"));
      setShowCompose(false);
      setForm({ title: "", body: "", pinned: false });
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  async function handlePin(id, pin) {
    try {
      await api.pinNews(id, pin);
      toast(pin ? t("ui.pinned_1") : t("ui.unpin"));
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  async function handleComment(id) {
    if (!comment.trim()) return;
    try {
      await api.commentOnNews(id, comment);
      toast(t("ui.comment_posted"));
      setCommenting(null);
      setComment("");
      load();
    } catch (err) {
      toast(t(err?.message || "ui.error_generic"), "error");
    }
  }

  function openComment(id) {
    setCommenting(id);
    setComment("");
  }

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return news;
    return news.filter(n =>
      n.title?.toLowerCase().includes(q) ||
      n.body?.toLowerCase().includes(q) ||
      n.author?.toLowerCase().includes(q) ||
      n.authorFullName?.toLowerCase().includes(q)
    );
  }, [news, search]);

  if (loading) return <div className="page"><div className="spinner" /></div>;

  const pinned = filtered.filter(n => n.pinned);
  const regular = filtered.filter(n => !n.pinned);

  return (
    <div className="page">
      <Toasts />
      <div className="page-header flex-between">
        <div><h1>{t("student.menu.news")}</h1></div>
        {isEmployee && (
          <button className="btn btn-primary" onClick={() => setShowCompose(true)}>
            📰 {t("ui.publish")}
          </button>
        )}
      </div>

      <input
        className="form-control mb-3"
        style={{ maxWidth: 360 }}
        placeholder={t("ui.search_news")}
        value={search}
        onChange={e => setSearch(e.target.value)}
      />

      {pinned.length > 0 && (
        <>
          <div className="section-title">📌 {t("ui.pinned_1")}</div>
          {pinned.map(n => (
            <NewsCard key={n.id} n={n} t={t}
              onView={() => setViewing(n.id)}
              onComment={() => openComment(n.id)} />
          ))}
        </>
      )}

      <div className="section-title mt-3">{t("ui.latest")}</div>
      {regular.map(n => (
        <NewsCard key={n.id} n={n} t={t}
          onView={() => setViewing(n.id)}
          onComment={() => openComment(n.id)} />
      ))}
      {filtered.length === 0 && (
        <div className="empty"><div className="empty-icon">📰</div><p>{t("news.empty")}</p></div>
      )}

      {viewingItem && (
        <NewsDetailModal
          n={viewingItem}
          t={t}
          canPin={isEmployee && (canPinAny || auth?.username === viewingItem.author)}
          onClose={() => setViewing(null)}
          onComment={() => openComment(viewingItem.id)}
          onPin={(pin) => handlePin(viewingItem.id, pin)}
        />
      )}

      {showCompose && (
        <Modal title={t("ui.publish_news")} onClose={() => setShowCompose(false)}
          actions={<>
            <button className="btn btn-secondary" onClick={() => setShowCompose(false)}>{t("common.back")}</button>
            <button className="btn btn-primary" form="news-form">{t("ui.publish")}</button>
          </>}>
          <form id="news-form" onSubmit={handlePublish} style={{ display: "flex", flexDirection: "column", gap: 14 }}>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.title")}</label>
              <input className="form-control" required value={form.title}
                     onChange={e => setForm({ ...form, title: e.target.value })} />
            </div>
            <div className="form-group" style={{ marginBottom: 0 }}>
              <label>{t("ui.body")}</label>
              <textarea className="form-control" rows="8" value={form.body}
                        onChange={e => setForm({ ...form, body: e.target.value })} />
            </div>
            {canPinAny && (
              <label style={{ display: "flex", alignItems: "center", gap: 8, fontSize: 13, color: "var(--text-2)" }}>
                <input type="checkbox" checked={form.pinned}
                       onChange={e => setForm({ ...form, pinned: e.target.checked })} />
                {t("ui.pin_this_post")}
              </label>
            )}
          </form>
        </Modal>
      )}

      {commenting && (
        <Modal title={t("ui.add_comment")} onClose={() => setCommenting(null)}
          actions={<>
            <button className="btn btn-secondary" onClick={() => setCommenting(null)}>{t("common.back")}</button>
            <button className="btn btn-primary" disabled={!comment.trim()} onClick={() => handleComment(commenting)}>{t("ui.post_1")}</button>
          </>}>
          <textarea className="form-control" rows="4" placeholder={t("ui.write_your_comment")}
                    value={comment} onChange={e => setComment(e.target.value)} />
        </Modal>
      )}
    </div>
  );
}

function NewsCard({ n, t, onView, onComment }) {
  const body = n.body ?? "";
  const isLong = body.length > FEED_PREVIEW_CHARS;
  const preview = isLong ? body.slice(0, FEED_PREVIEW_CHARS) + "…" : body;
  const authorLabel = n.authorFullName ? `${n.authorFullName} (@${n.author})` : n.author;
  const publishedLabel = n.publishedAt?.slice(0, 16).replace("T", " ");

  return (
    <div className="card" style={{ marginBottom: 14 }}>
      <div className="flex-between">
        <span className="fw-600">{n.title}</span>
        {n.pinned && <span className="badge badge-yellow">📌 {t("ui.pinned_1")}</span>}
      </div>

      <p style={{ color: "var(--text-2)", fontSize: 13, marginTop: 8, lineHeight: 1.7, whiteSpace: "pre-wrap" }}>
        {preview}
      </p>

      <div className="flex-between mt-2" style={{ borderTop: "1px solid var(--border)", paddingTop: 10, gap: 8, flexWrap: "wrap" }}>
        <span className="text-muted text-sm">
          {t("ui.by")} {authorLabel}
          {publishedLabel && <> · {publishedLabel}</>}
          {" · "}
          {n.comments?.length ?? 0} {t("ui.comments")}
        </span>
        <div style={{ display: "flex", gap: 6 }}>
          <button className="btn btn-link" onClick={onView} style={{ padding: "4px 8px", fontSize: 13 }}>
            {t("ui.read_more")}
          </button>
          <button className="btn btn-secondary btn-sm" onClick={onComment}>💬 {t("ui.comment")}</button>
        </div>
      </div>
    </div>
  );
}

function NewsDetailModal({ n, t, canPin, onClose, onComment, onPin }) {
  const body = n.body ?? "";
  const authorLabel = n.authorFullName ? `${n.authorFullName} (@${n.author})` : n.author;
  const publishedLabel = n.publishedAt?.slice(0, 16).replace("T", " ");

  return (
    <Modal title={n.title} onClose={onClose}
      actions={<>
        {canPin && (
          n.pinned
            ? <button className="btn btn-secondary btn-sm" onClick={() => onPin(false)}>📌 {t("ui.unpin")}</button>
            : <button className="btn btn-secondary btn-sm" onClick={() => onPin(true)}>📌 {t("ui.pinned_1")}</button>
        )}
        <button className="btn btn-secondary btn-sm" onClick={onComment}>💬 {t("ui.comment")}</button>
        <button className="btn btn-secondary" onClick={onClose}>{t("common.back")}</button>
      </>}>
      <div style={{ display: "flex", flexDirection: "column", gap: 16 }}>
        <div style={{ color: "var(--text-2)", fontSize: 13 }}>
          {t("ui.by")} {authorLabel}
          {publishedLabel && <> · {publishedLabel}</>}
          {n.pinned && <> · 📌 {t("ui.pinned_1")}</>}
        </div>

        <p style={{ whiteSpace: "pre-wrap", lineHeight: 1.8, color: "var(--text-2)", fontSize: 14 }}>
          {body}
        </p>

        <div style={{ borderTop: "1px solid var(--border)", paddingTop: 12 }}>
          <div className="fw-600" style={{ marginBottom: 8, fontSize: 13 }}>
            {n.comments?.length ?? 0} {t("ui.comments")}
          </div>
          {n.comments?.length > 0 ? (
            <div style={{ display: "flex", flexDirection: "column", gap: 6 }}>
              {n.comments.map((c, i) => {
                const who = c.authorFullName ? `${c.authorFullName} (@${c.author})` : c.author;
                return (
                  <div key={i} style={{ background: "var(--bg-3)", borderRadius: 8, padding: "8px 12px", fontSize: 13 }}>
                    <div className="flex-between" style={{ marginBottom: 2 }}>
                      <span className="fw-600">{who}</span>
                      {c.postedAt && (
                        <span className="text-muted" style={{ fontSize: 11 }}>
                          {c.postedAt.slice(0, 16).replace("T", " ")}
                        </span>
                      )}
                    </div>
                    <span style={{ whiteSpace: "pre-wrap", color: "var(--text-2)" }}>{c.text}</span>
                  </div>
                );
              })}
            </div>
          ) : (
            <p className="text-muted text-sm">{t("ui.no_comments")}</p>
          )}
        </div>
      </div>
    </Modal>
  );
}
