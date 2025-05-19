package com.campusconnect.backend.dto;

import java.time.LocalDateTime;

public class AnswerDTO {

    private Long id;
    private String content;
    private Long userId;
    private Long questionId;
    private LocalDateTime createdAt;

    public AnswerDTO() {
        // Default constructor for form binding
    }

    // Constructor for creating a new answer (no ID or timestamp yet)
    public AnswerDTO(String content, Long userId) {
        this.content = content;
        this.userId = userId;
    }

    // Full constructor (e.g., for service-to-controller DTO response)
    public AnswerDTO(Long id, String content, Long userId, Long questionId, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.questionId = questionId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
