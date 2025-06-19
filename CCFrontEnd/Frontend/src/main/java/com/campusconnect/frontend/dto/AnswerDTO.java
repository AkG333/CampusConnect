package com.campusconnect.frontend.dto;

/**
 * Data Transfer Object (DTO) for posting new Answers.
 * Mirrors the AnswerDTO expected by the backend's POST /api/answers endpoint.
 * Requires questionId, content, and userId.
 */
public class AnswerDTO {
    private Long questionId;
    private String content;
    private Long userId; // Assuming backend AnswerDTO expects userId

    public AnswerDTO() {
    }

    public AnswerDTO(Long questionId, String content, Long userId) {
        this.questionId = questionId;
        this.content = content;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
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

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "questionId=" + questionId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                '}';
    }
}
