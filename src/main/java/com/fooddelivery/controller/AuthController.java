package com.fooddelivery.controller;

import com.fooddelivery.factory.UserFactoryManager;
import com.fooddelivery.model.User;
import com.fooddelivery.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class AuthController {
    private final UserService userService;
    private final UserFactoryManager userFactoryManager;
    
    public AuthController(UserService userService, UserFactoryManager userFactoryManager) {
        this.userService = userService;
        this.userFactoryManager = userFactoryManager;
    }
    
    @GetMapping("/auth")
    public String showLoginPage() {
        return "auth/login";
    }
    
    @GetMapping("/auth/register")
    public String showRegistrationPage() {
        return "auth/register";
    }
    
    @PostMapping("/auth/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, 
                              @RequestParam String email, @RequestParam String role, 
                              @RequestParam(required = false) String phone, 
                              @RequestParam(required = false) String address, 
                              RedirectAttributes redirectAttributes) {
        try {
            User user;
            if (phone != null && address != null) {
                user = userService.registerUser(username, password, email, role, phone, address);
            } else {
                user = userService.registerUser(username, password, email, role);
            }
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }
    
    @PostMapping("/auth/login")
    public String login(@RequestParam String username, @RequestParam String password, 
                        HttpSession session, RedirectAttributes redirectAttributes) {
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/auth";
        }
        
        User user = userOpt.get();
        session.setAttribute("user", user);
        
        switch (user.getRole()) {
            case "ADMIN":
                return "redirect:/admin/dashboard";
            case "RESTAURANT":
                return "redirect:/restaurant/dashboard";
            case "CUSTOMER":
                return "redirect:/customer/dashboard";
            case "DELIVERY_AGENT":
                return "redirect:/delivery/dashboard";
            default:
                redirectAttributes.addFlashAttribute("error", "Invalid user role");
                return "redirect:/auth";
        }
    }
    
    @GetMapping("/auth/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}