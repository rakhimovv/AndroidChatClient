package ru.mail.ruslan.androidchatclient;

import android.content.SharedPreferences;

public class MyPreferences {
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASS = "pass";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SESSION_ID = "session_id";

    public static void saveLogin(SharedPreferences prefs, String login) {
        prefs.edit().putString(KEY_LOGIN, login).apply();
    }

    public static String loadLogin(SharedPreferences prefs) {
        return prefs.getString(KEY_LOGIN, null);
    }

    public static void savePass(SharedPreferences prefs, String pass) {
        prefs.edit().putString(KEY_PASS, pass).apply();
    }

    public static String loadPass(SharedPreferences prefs) {
        return prefs.getString(KEY_PASS, null);
    }

    public static void saveUserId(SharedPreferences prefs, String userId) {
        prefs.edit().putString(KEY_USER_ID, userId).apply();
    }

    public static String loadUserId(SharedPreferences prefs) {
        return prefs.getString(KEY_USER_ID, null);
    }

    public static void saveSessionId(SharedPreferences prefs, String sessionId) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply();
    }

    public static String loadSessionId(SharedPreferences prefs) {
        return prefs.getString(KEY_SESSION_ID, null);
    }

    public static boolean isLoggedIn(SharedPreferences prefs) {
        return prefs.contains(KEY_LOGIN) && prefs.contains(KEY_PASS);
    }

    public static void deleteAuthData(SharedPreferences prefs) {
        prefs.edit().remove(KEY_LOGIN).remove(KEY_PASS).remove(KEY_USER_ID).remove(KEY_SESSION_ID).apply();
    }
}