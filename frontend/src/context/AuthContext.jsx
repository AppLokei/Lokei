import { createContext, useContext, useEffect, useMemo, useState } from "react";

import { api } from "../api/services.js";
import { TOKEN_STORAGE_KEY, USER_STORAGE_KEY } from "../lib/constants.js";

const AuthContext = createContext(null);

const readStoredUser = () => {
  try {
    const raw = sessionStorage.getItem(USER_STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
};

export const AuthProvider = ({ children }) => {
  const [token, setToken] = useState(() => sessionStorage.getItem(TOKEN_STORAGE_KEY) || "");
  const [user, setUser] = useState(() => readStoredUser());
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (token) {
      sessionStorage.setItem(TOKEN_STORAGE_KEY, token);
    } else {
      sessionStorage.removeItem(TOKEN_STORAGE_KEY);
    }
  }, [token]);

  useEffect(() => {
    if (user) {
      sessionStorage.setItem(USER_STORAGE_KEY, JSON.stringify(user));
    } else {
      sessionStorage.removeItem(USER_STORAGE_KEY);
    }
  }, [user]);

  const applySession = (response) => {
    setToken(response.token);
    setUser(response.usuario);
    return response;
  };

  const login = async (payload) => {
    setLoading(true);
    try {
      return applySession(await api.auth.login(payload));
    } finally {
      setLoading(false);
    }
  };

  const register = async (payload) => {
    setLoading(true);
    try {
      return applySession(await api.auth.register(payload));
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      if (token) {
        await api.auth.logout();
      }
    } finally {
      setToken("");
      setUser(null);
    }
  };

  const refreshProfile = async () => {
    if (!token) return null;
    const profile = await api.profile.get();
    setUser((current) => ({
      ...(current || {}),
      id: profile.id,
      nome: profile.nome,
      email: profile.email,
      papel: profile.papel,
    }));
    return profile;
  };

  const value = useMemo(
    () => ({
      token,
      user,
      loading,
      authenticated: Boolean(token && user),
      isAdmin: user?.papel === "ADMIN",
      login,
      register,
      logout,
      refreshProfile,
      setUser,
    }),
    [token, user, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth deve ser usado dentro de AuthProvider");
  }
  return context;
};
