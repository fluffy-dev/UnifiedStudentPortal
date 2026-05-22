import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext.jsx";
import { useI18n } from "../context/I18nContext.jsx";
import * as api from "../api/index.js";

const DEMO_ACCOUNTS = [
  { group: "Administration",
    accounts: [
      { username: "rauan",     role: "Admin" },
      { username: "serdar",    role: "Manager" },
      { username: "bayan",     role: "Dean" },
    ]},
  { group: "Teaching Staff",
    accounts: [
      { username: "zhomart",   role: "Teacher · Researcher" },
      { username: "anel",      role: "Teacher" },
      { username: "marat",     role: "Teacher" },
    ]},
  { group: "Support",
    accounts: [
      { username: "bekasyl",   role: "TechSupport" },
      { username: "assylzhan", role: "Librarian" },
    ]},
  { group: "Students",
    accounts: [
      { username: "dariya",    role: "Student yr2" },
      { username: "bexultan",  role: "Student yr3" },
      { username: "assel",     role: "Student yr1" },
      { username: "arman",     role: "Student yr2" },
      { username: "zarina",    role: "Student yr3" },
      { username: "dias",      role: "Student yr1" },
      { username: "aizat",     role: "Student yr2" },
      { username: "alibek",    role: "Student yr4" },
      { username: "madina",    role: "Student yr1" },
      { username: "yerlan",    role: "Student yr2" },
      { username: "sanzhar",   role: "Student yr4" },
      { username: "kamila",    role: "Student yr1" },
      { username: "temirlan",  role: "Student yr3" },
    ]},
  { group: "Graduate",
    accounts: [
      { username: "nurasyl",   role: "Master · Researcher" },
      { username: "aigerim",   role: "Doctorate · Researcher" },
    ]},
];

const ROLE_COLORS = {
  "Admin":                  { bg: "rgba(239,68,68,0.08)",   text: "#b91c1c"  },
  "Manager":                { bg: "rgba(245,158,11,0.08)",  text: "#b45309"  },
  "Dean":                   { bg: "rgba(16,185,129,0.08)",  text: "#047857"  },
  "Teacher · Researcher":   { bg: "rgba(99,102,241,0.10)",  text: "#4338ca"  },
  "Teacher":                { bg: "rgba(99,102,241,0.08)",  text: "#4338ca"  },
  "TechSupport":            { bg: "rgba(107,114,128,0.08)", text: "#374151"  },
  "Librarian":              { bg: "rgba(124,109,249,0.08)", text: "#6d28d9"  },
  "Master · Researcher":    { bg: "rgba(37,99,235,0.08)",   text: "#1d4ed8"  },
  "Doctorate · Researcher": { bg: "rgba(37,99,235,0.10)",   text: "#1e3a8a"  },
};
function roleColor(role) {
  return ROLE_COLORS[role] || { bg: "rgba(37,99,235,0.07)", text: "#2563eb" };
}

export function Login() {
  const { signIn } = useAuth();
  const { t } = useI18n();
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [showAccounts, setShowAccounts] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);
    try {
      const data = await api.login(form.username, form.password);
      signIn(data.token, data.username, data.role, data.isResearcher);
      navigate("/");
    } catch (err) {
      setError(t(err?.message || "login.bad_credentials"));
    } finally {
      setLoading(false);
    }
  }

  function quickLogin(username) {
    setForm({ username, password: username });
    setError("");
  }

  return (
    <div style={{
      minHeight: "100vh", display: "flex", alignItems: "flex-start",
      justifyContent: "center", background: "var(--bg)", padding: "40px 20px", gap: 32,
      flexWrap: "wrap"
    }}>
      {/* Login form */}
      <div style={{ width: "100%", maxWidth: 380, paddingTop: 40 }}>
        <div style={{ textAlign: "center", marginBottom: 32 }}>
          <div style={{ fontSize: 48, marginBottom: 8 }}>🎓</div>
          <h1 style={{ fontSize: 26, fontWeight: 800, color: "var(--text)", letterSpacing: "-0.5px" }}>
            {t("ui.university_system")}
          </h1>
          <p style={{ color: "var(--text-2)", marginTop: 6, fontSize: 14 }}>
            {t("ui.brand_sub")}
          </p>
        </div>

        <form className="card" onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: 18, padding: "28px 24px" }}>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label htmlFor="login-username" style={{ fontSize: 13, fontWeight: 600, color: "var(--text)" }}>
              {t("ui.username")}
            </label>
            <input
              id="login-username"
              className="form-control"
              required
              minLength={1}
              value={form.username}
              onChange={(e) => setForm({ ...form, username: e.target.value })}
              autoFocus
              autoComplete="username"
              style={{ marginTop: 6 }}
            />
          </div>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label htmlFor="login-password" style={{ fontSize: 13, fontWeight: 600, color: "var(--text)" }}>
              {t("ui.password")}
            </label>
            <input
              id="login-password"
              className="form-control"
              type="password"
              required
              minLength={1}
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              autoComplete="current-password"
              style={{ marginTop: 6 }}
            />
          </div>
          {error && (
            <div style={{ color: "var(--danger)", fontSize: 13, background: "rgba(239,68,68,0.08)", borderRadius: 8, padding: "8px 12px" }}>
              {error}
            </div>
          )}
          <button className="btn btn-primary" disabled={loading} style={{ marginTop: 4, height: 42, fontSize: 15, fontWeight: 600 }}>
            {loading ? t("ui.signing_in") : t("ui.sign_in")}
          </button>
        </form>

        <button
          onClick={() => setShowAccounts(v => !v)}
          style={{
            width: "100%", marginTop: 12, background: "none", border: "1px dashed var(--border)",
            borderRadius: 10, padding: "10px 16px", cursor: "pointer", fontSize: 13,
            color: "var(--text-2)", display: "flex", alignItems: "center", justifyContent: "center", gap: 6
          }}
        >
          <span>{showAccounts ? "▲" : "▼"}</span>
          {showAccounts ? "Hide demo accounts" : "Show demo accounts (username = password)"}
        </button>

        {/* Mobile demo accounts */}
        {showAccounts && (
          <div className="card" style={{ marginTop: 8, padding: 0, overflow: "hidden" }}>
            <DemoAccountsTable onSelect={quickLogin} />
          </div>
        )}
      </div>

      {/* Desktop: always-visible demo panel */}
      <div style={{ width: "100%", maxWidth: 420, paddingTop: 40, display: "none" }} className="demo-panel-desktop">
        <div className="card" style={{ padding: 0, overflow: "hidden" }}>
          <div style={{ padding: "16px 20px 12px", borderBottom: "1px solid var(--border)", background: "var(--bg-3)" }}>
            <div style={{ fontSize: 13, fontWeight: 700, color: "var(--text)" }}>Demo Accounts</div>
            <div style={{ fontSize: 11, color: "var(--text-2)", marginTop: 2 }}>username = password · click to fill</div>
          </div>
          <DemoAccountsTable onSelect={quickLogin} />
        </div>
      </div>

      {/* Always-visible sidebar on wider screens using CSS */}
      <style>{`
        @media (min-width: 840px) {
          .demo-panel-desktop { display: block !important; }
        }
      `}</style>
    </div>
  );
}

function DemoAccountsTable({ onSelect }) {
  return (
    <div style={{ overflowY: "auto", maxHeight: 480 }}>
      {DEMO_ACCOUNTS.map(group => (
        <div key={group.group}>
          <div style={{
            padding: "8px 16px", fontSize: 10, fontWeight: 700, textTransform: "uppercase",
            letterSpacing: "0.08em", color: "var(--text-3)", background: "var(--bg-3)",
            borderBottom: "1px solid var(--border)"
          }}>
            {group.group}
          </div>
          {group.accounts.map(({ username, role }) => {
            const col = roleColor(role);
            return (
              <button
                key={username}
                onClick={() => onSelect(username)}
                style={{
                  width: "100%", display: "flex", alignItems: "center", gap: 12,
                  padding: "9px 16px", background: "none", border: "none",
                  borderBottom: "1px solid var(--border)", cursor: "pointer",
                  textAlign: "left", transition: "background 0.15s"
                }}
                onMouseEnter={e => e.currentTarget.style.background = "var(--bg-3)"}
                onMouseLeave={e => e.currentTarget.style.background = "none"}
              >
                <div style={{
                  width: 32, height: 32, borderRadius: "50%", background: col.bg,
                  color: col.text, display: "flex", alignItems: "center", justifyContent: "center",
                  fontSize: 13, fontWeight: 700, flexShrink: 0
                }}>
                  {username[0].toUpperCase()}
                </div>
                <div style={{ flex: 1, minWidth: 0 }}>
                  <div style={{ display: "flex", alignItems: "baseline", gap: 6 }}>
                    <span style={{ fontSize: 13, fontWeight: 600, color: "var(--text)" }}>{username}</span>
                    <span style={{ fontSize: 11, color: "var(--text-3)" }}>:{username}</span>
                  </div>
                  <div style={{ fontSize: 11, marginTop: 1 }}>
                    <span style={{
                      background: col.bg, color: col.text,
                      padding: "1px 6px", borderRadius: 4, fontWeight: 500
                    }}>
                      {role}
                    </span>
                  </div>
                </div>
                <span style={{ fontSize: 11, color: "var(--text-3)", flexShrink: 0 }}>click to fill →</span>
              </button>
            );
          })}
        </div>
      ))}
    </div>
  );
}
