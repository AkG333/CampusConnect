package com.campusconnect.backend.dto;

public class QuestionDTO {
    private String title;
    private String description;
    private Long userId;

    public QuestionDTO() {}

    public QuestionDTO(String title, String description, Long userId) {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
