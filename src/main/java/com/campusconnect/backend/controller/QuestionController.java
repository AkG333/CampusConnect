package com.campusconnect.backend.controller;

import com.campusconnect.backend.dto.AnswerDTO;
import com.campusconnect.backend.dto.QuestionDTO;
import com.campusconnect.backend.model.Answer;
import com.campusconnect.backend.model.Question;
import com.campusconnect.backend.service.AnswerService;
import com.campusconnect.backend.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public QuestionController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @GetMapping("/home")
    public String showAllQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "home"; // home.html
    }

    @GetMapping("/ask")
    public String showAskForm(Model model) {
        model.addAttribute("questionDTO", new QuestionDTO());
        return "ask"; // ask.html
    }

    @PostMapping("/ask")
    public String submitQuestion(@ModelAttribute("questionDTO") QuestionDTO questionDTO) {
        if (questionDTO.getUserId() == null) {
            questionDTO.setUserId(1L); // TODO: Replace with session-based user ID
        }
        questionService.postQuestion(questionDTO);
        return "redirect:/home";
    }

    // ✅ View a specific question with its answers
    @GetMapping("/question/{id}")
    public String viewQuestion(@PathVariable Long id, Model model) {
        Question question = questionService.getQuestionById(id);
        List<Answer> answers = answerService.getAnswersForQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        model.addAttribute("answerDTO", new AnswerDTO());
        return "question"; // question.html
    }

    // ✅ Post a new answer for a question
    @PostMapping("/question/{id}/answer")
    public String postAnswer(@PathVariable Long id, @ModelAttribute("answerDTO") AnswerDTO answerDTO) {
        if (answerDTO.getUserId() == null) {
            answerDTO.setUserId(1L); // TODO: Replace with session-based user ID
        }
        answerService.saveAnswer(id, answerDTO);
        return "redirect:/question/" + id;
    }
}
