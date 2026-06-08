import { createContext, useCallback, useContext, useMemo, useState } from "react";

const ToastContext = createContext(null);

export const ToastProvider = ({ children }) => {
  const [toasts, setToasts] = useState([]);

  const removeToast = useCallback((id) => {
    setToasts((current) => current.filter((toast) => toast.id !== id));
  }, []);

  const pushToast = useCallback((payload) => {
    const id = crypto.randomUUID();
    setToasts((current) => [...current, { id, type: "info", ...payload }]);
    window.setTimeout(() => removeToast(id), payload.duration ?? 4000);
  }, [removeToast]);

  const value = useMemo(
    () => ({
      success: (message) => pushToast({ message, type: "success" }),
      error: (message) => pushToast({ message, type: "error", duration: 5000 }),
      info: (message) => pushToast({ message, type: "info" }),
    }),
    [pushToast]
  );

  return (
    <ToastContext.Provider value={value}>
      {children}
      <div className="toastViewport" aria-live="polite" aria-atomic="true">
        {toasts.map((toast) => (
          <div key={toast.id} className={`toast toast--${toast.type}`}>
            {toast.message}
          </div>
        ))}
      </div>
    </ToastContext.Provider>
  );
};

export const useToast = () => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast deve ser usado dentro de ToastProvider");
  }
  return context;
};
