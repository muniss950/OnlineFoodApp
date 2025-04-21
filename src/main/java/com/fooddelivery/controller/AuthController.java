package com.fooddelivery.controller;

import com.fooddelivery.model.DeliveryAgent;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.DeliveryAgentService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    private final UserService userService;
    private final DeliveryAgentService deliveryAgentService;
    private final RestaurantService restaurantService;

    public AuthController(UserService userService,
                         DeliveryAgentService deliveryAgentService,
                         RestaurantService restaurantService) {
        this.userService = userService;
        this.deliveryAgentService = deliveryAgentService;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public String showLanding() {
        return "auth/landing";
    }

    @GetMapping("/customer/login")
    public String showCustomerLogin() {
        return "auth/customer/login";
    }

    @GetMapping("/restaurant/login")
    public String showRestaurantLogin() {
        return "auth/restaurant/login";
    }

    @GetMapping("/admin/login")
    public String showAdminLogin() {
        return "auth/admin/login";
    }

    @PostMapping("/customer/login")
    public String customerLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        try {
            Optional<User> userOpt = userService.login(username, password);
            if (userOpt.isEmpty() || !"CUSTOMER".equals(userOpt.get().getRole())) {
                model.addAttribute("error", "Invalid customer credentials");
                return "auth/customer/login";
            }
            session.setAttribute("user", userOpt.get());
            return "redirect:/customer/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/customer/login";
        }
    }

    @PostMapping("/restaurant/login")
    public String restaurantLogin(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {
        try {
            logger.info("Attempting restaurant login for user: {}", username);
            Optional<User> userOpt = userService.login(username, password);
            
            if (userOpt.isEmpty() || !"RESTAURANT".equals(userOpt.get().getRole())) {
                logger.warn("Invalid role for restaurant login: {}", userOpt.map(u -> u.getRole()).orElse("null"));
                model.addAttribute("error", "Invalid restaurant credentials");
                return "auth/restaurant/login";
            }
            
            User user = userOpt.get();
            session.setAttribute("user", user);
            if (user.getRestaurant() != null) {
                logger.info("Setting restaurant ID in session: {}", user.getRestaurant().getId());
                session.setAttribute("restaurantId", user.getRestaurant().getId());
            } else {
                logger.warn("No restaurant associated with user: {}", username);
                model.addAttribute("error", "No restaurant found for this account");
                return "auth/restaurant/login";
            }
            
            return "redirect:/restaurant/dashboard";
        } catch (RuntimeException e) {
            logger.error("Restaurant login failed: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            return "auth/restaurant/login";
        }
    }

    @PostMapping("/admin/login")
    public String adminLogin(@RequestParam String username,
                           @RequestParam String password,
                           HttpSession session,
                           Model model) {
        try {
            Optional<User> userOpt = userService.login(username, password);
            if (userOpt.isEmpty() || !"ADMIN".equals(userOpt.get().getRole())) {
                model.addAttribute("error", "Invalid admin credentials");
                return "auth/admin/login";
            }
            session.setAttribute("user", userOpt.get());
            return "redirect:/admin/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "auth/admin/login";
        }
    }
    
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        // Add list of restaurants for the dropdown
        model.addAttribute("restaurants", restaurantService.getActiveRestaurants());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String role,
                         @RequestParam(required = false) Long restaurantId,
                         Model model) {
        try {
            logger.info("Starting registration process for role: {}", role);
            User user = new User(username, password, email, role);
            
            // If registering as a restaurant, link to existing restaurant
            if ("RESTAURANT".equals(role)) {
                if (restaurantId == null) {
                    model.addAttribute("error", "Please select a restaurant");
                    model.addAttribute("restaurants", restaurantService.getActiveRestaurants());
                    return "auth/register";
                }
                
                Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Selected restaurant not found"));
                
                user = userService.registerRestaurantUser(user, restaurant);
                logger.info("User registered for restaurant with ID: {}", restaurantId);
            } else {
                user = userService.registerUser(user);
                logger.info("User registered successfully with role: {}", role);
            }
            
            return "redirect:/auth/" + role.toLowerCase() + "/login";
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage(), e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("restaurants", restaurantService.getActiveRestaurants());
            return "auth/register";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}