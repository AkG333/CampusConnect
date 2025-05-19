package com.campusconnect.backend.service;

import com.campusconnect.backend.dto.QuestionDTO;
import com.campusconnect.backend.model.Question;

import java.util.List;

public interface QuestionService {
    void postQuestion(QuestionDTO questionDTO);
    Question getQuestionById(Long id);
    List<Question> getAllQuestions();
    void deleteQuestion(Long id);
    List<Question> getQuestionsByUserId(Long userId);
}
