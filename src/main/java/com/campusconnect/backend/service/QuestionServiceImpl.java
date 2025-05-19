package com.campusconnect.backend.service.impl;

import com.campusconnect.backend.dao.QuestionDAO;
import com.campusconnect.backend.dto.QuestionDTO;
import com.campusconnect.backend.model.Question;
import com.campusconnect.backend.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionDAO questionDAO;

    @Override
    public void postQuestion(QuestionDTO dto) {
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setDescription(dto.getDescription());
        question.setCreatedAt(LocalDateTime.now());
        question.setUserId(dto.getUserId());
        questionDAO.saveQuestion(question);
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionDAO.getQuestionById(id);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDAO.getAllQuestions();
    }

    @Override
    public void deleteQuestion(Long id) {
        questionDAO.deleteQuestion(id);
    }

    @Override
    public List<Question> getQuestionsByUserId(Long userId) {
        return questionDAO.getQuestionsByUserId(userId);
    }
}
