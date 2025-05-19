package com.campusconnect.backend.service;

import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.model.Answer;

import java.util.List;

public interface AnswerService {

    // Method to post a new answer
    void postAnswer(AnswerDTO answerDTO);

    // Method to get answers by question ID
    List<Answer> getAnswersByQuestionId(Long questionId);

    // Method to get a specific answer by ID
    Answer getAnswerById(Long id);
}
