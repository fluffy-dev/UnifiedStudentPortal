import { useState, useCallback } from "react";

let id = 0;

export function useToast() {
  const [toasts, setToasts] = useState([]);

  const toast = useCallback((msg, type = "success") => {
    const key = ++id;
    setToasts((t) => [...t, { key, msg, type }]);
    setTimeout(() => setToasts((t) => t.filter((x) => x.key !== key)), 3500);
  }, []);

  function Toasts() {
    const hasErrors = toasts.some(t => t.type === "error");
    return (
      <>
        {/* Polite region for success messages */}
        <div className="toast-container" aria-live="polite" aria-atomic="false">
          {toasts.filter(t => t.type !== "error").map((t) => (
            <div key={t.key} className={`toast ${t.type}`} role="status">{t.msg}</div>
          ))}
        </div>
        {/* Assertive region for errors — interrupts screen reader */}
        {hasErrors && (
          <div className="toast-container" style={{ top: "auto", bottom: 80 }} aria-live="assertive" aria-atomic="true">
            {toasts.filter(t => t.type === "error").map((t) => (
              <div key={t.key} className={`toast ${t.type}`} role="alert">{t.msg}</div>
            ))}
          </div>
        )}
      </>
    );
  }

  return { toast, Toasts };
}
