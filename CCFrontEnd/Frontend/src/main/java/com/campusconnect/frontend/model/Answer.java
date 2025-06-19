package com.campusconnect.frontend.model;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for Answer.
 * Mirrors the Answer model/entity from the backend.
 * Field names (id, content, createdAt, userId, questionId)
 * exactly match the JSON keys returned by your backend's AnswerRestController.
 */
public class Answer {
    private Long id;
    private String content; // Assuming your backend's Answer has a 'content' field
    private LocalDateTime createdAt;
    private Long userId;
    private Long questionId;

    // You might want to add a field for the author's username here if your backend
    // can provide it, to display who posted the answer.
    // e.g., private String authorUsername;

    public Answer() {
    }

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

    public void setUserId(Long userId) { // Corrected: public void setUserId(Long userId)
        this.userId = userId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        // Customize how an Answer appears in the ListView
        return String.format("Posted on %s:\n  %s",
                (createdAt != null ? createdAt.toLocalDate() : "N/A"),
                content);
    }
}
