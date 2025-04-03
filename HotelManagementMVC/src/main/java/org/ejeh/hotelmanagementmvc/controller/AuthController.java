package org.ejeh.hotelmanagementmvc.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.ejeh.hotelmanagementmvc.dto.LoginRequest;
import org.ejeh.hotelmanagementmvc.dto.RegisterRequest;
import org.ejeh.hotelmanagementmvc.service.ApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final ApiService apiService;

    public AuthController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("registerRequest") RegisterRequest registerRequest,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        try {
            String result = apiService.registerUser(registerRequest);
            return "redirect:/login?registered";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "registration";
        }
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            String jwtToken = apiService.authenticateUser(loginRequest);

            // Debug output to verify
            System.out.println("Extracted token: " + jwtToken);

            // Store just the token value, not the JSON
            session.setAttribute("token", jwtToken);

            return "redirect:/hotels";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password: " + e.getMessage());
            return "login";
        }
    }
}