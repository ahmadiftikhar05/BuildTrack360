package com.example.buildtrack360.Database;


public class SessionManager {
    private static int currentUserId;
    private static String currentUsername;

    public static void setCurrentUser(int userId, String username) {
        currentUserId = userId;
        currentUsername = username;
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void clearSession() {
        currentUserId = 0;
        currentUsername = null;
    }
}