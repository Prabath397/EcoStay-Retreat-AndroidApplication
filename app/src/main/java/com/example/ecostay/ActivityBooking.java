package com.example.ecostay;

public class ActivityBooking {
    private int id;
    private int userId;
    private int activityId;
    private String bookingDate;
    private int participants;
    private String status;
    private String activityName;
    private double activityPrice;

    public ActivityBooking(int id, int userId, int activityId, String bookingDate,
                           int participants, String status, String activityName, double activityPrice) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.bookingDate = bookingDate;
        this.participants = participants;
        this.status = status;
        this.activityName = activityName;
        this.activityPrice = activityPrice;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getActivityId() { return activityId; }
    public void setActivityId(int activityId) { this.activityId = activityId; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public int getParticipants() { return participants; }
    public void setParticipants(int participants) { this.participants = participants; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public double getActivityPrice() { return activityPrice; }
    public void setActivityPrice(double activityPrice) { this.activityPrice = activityPrice; }
}