export const LoadingState = ({ message = "Carregando..." }) => (
  <div className="pageState pageState--loading">{message}</div>
);

export const ErrorState = ({ message = "Falha ao carregar.", action }) => (
  <div className="pageState pageState--error">
    <p>{message}</p>
    {action || null}
  </div>
);

export const EmptyState = ({ title = "Nada por aqui.", description = "" }) => (
  <div className="pageState pageState--empty">
    <strong>{title}</strong>
    {description ? <p>{description}</p> : null}
  </div>
);
