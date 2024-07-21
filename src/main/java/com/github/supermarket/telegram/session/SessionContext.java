package com.github.supermarket.telegram.session;


/**
 * Класс предназначен для получения информации о текущей сессии пользователя из любого места для текущего потока
 */
public class SessionContext {
    private static final ThreadLocal<UserSession> SESSIONS = new ThreadLocal<UserSession>();

    private SessionContext() {
    }

    public static UserSession getUserSession() {
        return SESSIONS.get();
    }

    public static void setSession(UserSession session) {
        SESSIONS.set(session);
    }

    public static void removeUserSession() {
        SESSIONS.remove();
    }
}
