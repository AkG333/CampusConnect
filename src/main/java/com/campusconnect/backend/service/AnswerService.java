package com.campusconnect.backend.service;

import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.model.Answer;

import java.util.List;

public interface AnswerService {

    // Save an answer linked to a specific question
    void saveAnswer(Long questionId, AnswerDTO answerDTO);

    // Get all answers for a specific question
    List<Answer> getAnswersForQuestion(Long questionId);

    // Get an answer by its ID
    Answer getAnswerById(Long id);
}
