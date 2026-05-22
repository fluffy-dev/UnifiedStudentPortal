import { useEffect, useId, useRef } from "react";

export function Modal({ title, onClose, children, actions }) {
  const dialogRef = useRef();
  const titleId = useId();

  // Move focus into dialog on mount, restore on unmount
  useEffect(() => {
    const prev = document.activeElement;
    dialogRef.current?.focus();
    return () => prev?.focus?.();
  }, []);

  // Escape to close
  useEffect(() => {
    const handler = (e) => { if (e.key === "Escape") onClose(); };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose]);

  // Focus trap: keep Tab cycling inside the dialog
  useEffect(() => {
    const el = dialogRef.current;
    if (!el) return;
    const focusable = 'button,[href],input,select,textarea,[tabindex]:not([tabindex="-1"])';
    const handler = (e) => {
      if (e.key !== "Tab") return;
      const nodes = [...el.querySelectorAll(focusable)].filter(n => !n.disabled);
      if (!nodes.length) return;
      const first = nodes[0], last = nodes[nodes.length - 1];
      if (e.shiftKey && document.activeElement === first) {
        e.preventDefault(); last.focus();
      } else if (!e.shiftKey && document.activeElement === last) {
        e.preventDefault(); first.focus();
      }
    };
    el.addEventListener("keydown", handler);
    return () => el.removeEventListener("keydown", handler);
  }, []);

  return (
    <div
      className="modal-overlay"
      onClick={(e) => { if (e.target === e.currentTarget) onClose(); }}
      aria-hidden="false"
    >
      <div
        className="modal"
        ref={dialogRef}
        role="dialog"
        aria-modal="true"
        aria-labelledby={titleId}
        tabIndex={-1}
      >
        <h2 id={titleId}>{title}</h2>
        {children}
        {actions && <div className="modal-actions">{actions}</div>}
      </div>
    </div>
  );
}
