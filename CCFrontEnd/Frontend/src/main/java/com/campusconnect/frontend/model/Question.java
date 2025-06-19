package com.campusconnect.frontend.model;

import java.time.LocalDateTime; // Import LocalDateTime for the date field

/**
 * Data Transfer Object (DTO) for Question.
 * Mirrors the Question model/entity from the backend.
 * Field names (id, title, description, createdAt, userId) now exactly match
 * the JSON keys returned by your backend's QuestionRestController.
 */
public class Question {
    private Long id;
    private String title;
    private String description; // CHANGED from 'content' to 'description'
    private LocalDateTime createdAt; // Added this field
    private Long userId;        // Added this field

    // Constructors
    public Question() {
    }

    public Question(Long id, String title, String description, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // Getters and Setters - Jackson uses these for serialization/deserialization
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() { // Getter for 'description'
        return description;
    }

    public void setDescription(String description) { // Setter for 'description'
        this.description = description;
    }

    public LocalDateTime getCreatedAt() { // Getter for 'createdAt'
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) { // Setter for 'createdAt'
        this.createdAt = createdAt;
    }

    public Long getUserId() { // Getter for 'userId'
        return userId;
    }

    public void setUserId(Long userId) { // Setter for 'userId'
        this.userId = userId;
    }

    @Override
    public String toString() {
        // This is important for how the Question objects appear in the ListView
        // You can customize this to show more details and make it more readable
        return String.format("%s (Asked on %s)\n  %s",
                title,
                (createdAt != null ? createdAt.toLocalDate() : "N/A"), // Display only date for brevity
                description);
    }
}
