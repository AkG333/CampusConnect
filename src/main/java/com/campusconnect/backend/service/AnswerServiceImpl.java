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
    public void saveAnswer(Long questionId, AnswerDTO answerDTO) {
        Answer answer = new Answer();
        answer.setContent(answerDTO.getContent());
        answer.setUserId(answerDTO.getUserId());
        answer.setQuestionId(questionId); // set from path variable
        answer.setCreatedAt(LocalDateTime.now());

        answerDAO.saveAnswer(answer);
    }

    @Override
    public List<Answer> getAnswersForQuestion(Long questionId) {
        return answerDAO.getAnswersByQuestionId(questionId);
    }

    @Override
    public Answer getAnswerById(Long id) {
        return answerDAO.getAnswerById(id);
    }
}
