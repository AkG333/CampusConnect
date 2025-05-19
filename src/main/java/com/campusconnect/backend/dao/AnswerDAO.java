package com.campusconnect.backend.dao;

import com.campusconnect.backend.model.Answer;

import java.util.List;

public interface AnswerDAO {

    void saveAnswer(Answer answer);

    List<Answer> getAnswersByQuestionId(Long questionId);

    Answer getAnswerById(Long id);
}
