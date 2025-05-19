package com.campusconnect.backend.dto;

import com.campusconnect.backend.model.Question;
import com.campusconnect.backend.model.Answer;

import java.util.List;

public class QuestionWithAnswersDTO {

    private Question question;
    private List<Answer> answers;

    // Default constructor
    public QuestionWithAnswersDTO() {}

    // Constructor to initialize with question and answers
    public QuestionWithAnswersDTO(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    // Getter for question
    public Question getQuestion() {
        return question;
    }

    // Setter for question
    public void setQuestion(Question question) {
        this.question = question;
    }

    // Getter for answers
    public List<Answer> getAnswers() {
        return answers;
    }

    // Setter for answers
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
