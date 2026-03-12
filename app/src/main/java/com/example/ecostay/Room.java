package com.example.ecostay;

public class Room {
    private int id;
    private String name;
    private String type;
    private String description;
    private double price;
    private boolean isAvailable;
    private int capacity;
    private String imageUrl;
    private String amenities;

    public Room() {}

    public Room(int id, String name, String type, String description, double price,
                boolean isAvailable, int capacity, String imageUrl, String amenities) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.price = price;
        this.isAvailable = isAvailable;
        this.capacity = capacity;
        this.imageUrl = imageUrl;
        this.amenities = amenities;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }
}