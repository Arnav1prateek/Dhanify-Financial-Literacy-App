package com.example.dhanify;

public class Goal {
    private String id;
    private String description;

    // Default constructor required for Firestore serialization
    public Goal() {
    }

    // Constructor with parameters
    public Goal(String description) {
        this.description = description;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
