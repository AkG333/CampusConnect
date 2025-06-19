package com.campusconnect.frontend.dto;

/**
 * Data Transfer Object (DTO) for posting new Questions.
 * Mirrors the QuestionDTO expected by the backend's POST /api/questions endpoint.
 */
public class QuestionDTO {
    private String title;
    private String description; // Matches backend's 'description' field
    private Long userId;        // ADDED: userId field

    // Constructors
    public QuestionDTO() {
    }

    public QuestionDTO(String title, String description, Long userId) { // Updated constructor
        this.title = title;
        this.description = description;
        this.userId = userId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() { // Getter for userId
        return userId;
    }

    public void setUserId(Long userId) { // Setter for userId
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}
