import { useState, useCallback } from "react";

let id = 0;

// Module-scope component — stable identity, receives toasts as a prop.
// Defined outside useToast so React never sees a new component type for this.
function ToastsView({ toasts }) {
  const errors   = toasts.filter(t => t.type === "error");
  const successes = toasts.filter(t => t.type !== "error");
  return (
    <>
      <div className="toast-container" aria-live="polite" aria-atomic="false">
        {successes.map(t => (
          <div key={t.key} className={`toast ${t.type}`} role="status">{t.msg}</div>
        ))}
      </div>
      {errors.length > 0 && (
        <div className="toast-container" style={{ top: "auto", bottom: 80 }}
             aria-live="assertive" aria-atomic="true">
          {errors.map(t => (
            <div key={t.key} className={`toast ${t.type}`} role="alert">{t.msg}</div>
          ))}
        </div>
      )}
    </>
  );
}

export function useToast() {
  const [toasts, setToasts] = useState([]);

  const toast = useCallback((msg, type = "success") => {
    const key = ++id;
    setToasts(prev => [...prev, { key, msg, type }]);
    setTimeout(() => setToasts(prev => prev.filter(x => x.key !== key)), 3500);
  }, []);

  // Callers use <Toasts /> unchanged. We render ToastsView (stable) directly,
  // so the toast-container DOM is never torn down by parent re-renders.
  function Toasts() { return <ToastsView toasts={toasts} />; }

  return { toast, Toasts };
}
