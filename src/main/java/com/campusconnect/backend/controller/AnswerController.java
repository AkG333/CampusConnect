package com.campusconnect.backend.controller;

import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.model.Answer;
import com.campusconnect.backend.model.Question;
import com.campusconnect.backend.service.AnswerService;
import com.campusconnect.backend.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;

    public AnswerController(AnswerService answerService, QuestionService questionService) {
        this.answerService = answerService;
        this.questionService = questionService;
    }

    // Show question detail page with answers
    @GetMapping("/{id}")
    public String showQuestionDetail(@PathVariable("id") Long questionId, Model model) {
        Question question = questionService.getQuestionById(questionId);
        List<Answer> answers = answerService.getAnswersForQuestion(questionId);

        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        model.addAttribute("answerDTO", new AnswerDTO()); // for the answer form

        return "question_detail";  // renders templates/question_detail.html
    }

    // Handle posting new answer
    @PostMapping("/{id}/answers")
    public String postAnswer(@PathVariable("id") Long questionId, @ModelAttribute AnswerDTO answerDTO) {
        // Set the question ID in the DTO
        answerDTO.setQuestionId(questionId);

        // TODO: Replace userId with actual logged-in user ID
        if (answerDTO.getUserId() == null) {
            answerDTO.setUserId(1L);
        }

        // Call the updated service method
        answerService.saveAnswer(questionId, answerDTO);

        return "redirect:/questions/" + questionId;
    }
}
