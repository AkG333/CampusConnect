package com.campusconnect.backend.model;

import java.time.LocalDateTime;

public class Answer {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private Long userId; // FK to User
    private Long questionId; // FK to Question

    // Constructors
    public Answer() {}

    public Answer(Long id, String content, LocalDateTime createdAt, Long userId, Long questionId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.questionId = questionId;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
