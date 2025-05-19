package com.campusconnect.backend.controller;

import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.model.Answer;
import com.campusconnect.backend.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @PostMapping
    public ResponseEntity<String> postAnswer(@RequestBody AnswerDTO answerDTO) {
        answerService.postAnswer(answerDTO);
        return ResponseEntity.ok("Answer posted successfully");
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        Answer answer = answerService.getAnswerById(id);
        return ResponseEntity.ok(answer);
    }
}
