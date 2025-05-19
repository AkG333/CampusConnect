package com.campusconnect.backend.dao;

import com.campusconnect.backend.model.Question;
import java.util.List;

public interface QuestionDAO {
    void saveQuestion(Question question);
    Question getQuestionById(Long id);
    List<Question> getAllQuestions();
    void deleteQuestion(Long id);
    List<Question> getQuestionsByUserId(Long userId);
}
