package com.campusconnect.backend.controller;

import com.campusconnect.backend.dto.QuestionDTO;
import com.campusconnect.backend.model.Question;
import com.campusconnect.backend.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Redirect root URL to /home
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String showAllQuestions(Model model) {
        List<Question> questions = questionService.getAllQuestions();
        model.addAttribute("questions", questions);
        return "home";  // renders templates/home.html
    }

    @GetMapping("/ask")
    public String showAskForm(Model model) {
        model.addAttribute("questionDTO", new QuestionDTO());
        return "ask";   // renders templates/ask.html
    }

    @PostMapping("/ask")
    public String submitQuestion(@ModelAttribute("questionDTO") QuestionDTO questionDTO) {
        if (questionDTO.getUserId() == null) {
            questionDTO.setUserId(1L); // TODO: Replace with actual logged-in user ID
        }
        questionService.postQuestion(questionDTO);
        return "redirect:/home";
    }

}
