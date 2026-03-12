package com.example.ecostay;

public class Notification {
    private int id;
    private int userId;
    private String title;
    private String message;
    private String date;
    private boolean isRead;

    public Notification(int id, int userId, String title, String message, String date, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.date = date;
        this.isRead = isRead;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}