package com.campusconnect.backend.dto;

import java.time.LocalDateTime;

public class AnswerDTO {
    private Long id; // This field can be useful for PUT/PATCH or responses
    private String content;
    private Long userId;
    private Long questionId;
    private LocalDateTime createdAt;

    // Default constructor
    public AnswerDTO() {}

    // Constructor for creating new answers (without ID)
    public AnswerDTO(String content, Long userId, Long questionId) {
        this.content = content;
        this.userId = userId;
        this.questionId = questionId;
    }

    // Constructor for responses with full data
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
