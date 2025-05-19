package com.campusconnect.backend.service;

import com.campusconnect.backend.dao.AnswerDAO;
import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerDAO answerDAO;

    @Override
    public void postAnswer(AnswerDTO answerDTO) {
        // Create a new Answer object
        Answer answer = new Answer();
        answer.setContent(answerDTO.getContent());
        answer.setUserId(answerDTO.getUserId());
        answer.setQuestionId(answerDTO.getQuestionId());
        answer.setCreatedAt(LocalDateTime.now());

        // Save the answer using the DAO
        answerDAO.saveAnswer(answer);
    }

    @Override
    public List<Answer> getAnswersByQuestionId(Long questionId) {
        // Get the list of answers by question ID using the DAO
        return answerDAO.getAnswersByQuestionId(questionId);
    }

    @Override
    public Answer getAnswerById(Long id) {
        // Get a specific answer by ID using the DAO
        return answerDAO.getAnswerById(id);
    }


}
