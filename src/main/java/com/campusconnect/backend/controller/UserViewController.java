package com.campusconnect.backend.controller;

import com.campusconnect.backend.dto.LoginRequest;
import com.campusconnect.backend.model.User;
import com.campusconnect.backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserViewController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/login";
    }


    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";  // Thymeleaf signup.html
    }

    @PostMapping("/signup")
    public String signupSubmit(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";  // Thymeleaf login.html
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginRequest loginRequest, Model model) {
        boolean loggedIn = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (loggedIn) {
            return "redirect:/home";  // redirect on success
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";  // stay on login page with error
        }
    }

    @GetMapping("/user/home")
    public String homePage() {
        return "home";
    }
}
