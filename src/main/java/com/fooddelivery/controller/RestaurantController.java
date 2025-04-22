package com.fooddelivery.controller;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.RestaurantService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.fooddelivery.model.User;
import java.util.List;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;
    
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }
    
    @GetMapping
    public String listRestaurants(Model model) {
        model.addAttribute("restaurants", restaurantService.getActiveRestaurants());
        return "restaurants/list";
    }
    
    @GetMapping("/{id}")
    public String viewRestaurant(@PathVariable Long id, Model model) {
        Restaurant restaurant = restaurantService.getRestaurantById(id)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        model.addAttribute("restaurant", restaurant);
        return "restaurants/view";
    }
    
    @GetMapping("/search")
    public String searchRestaurants(@RequestParam String query, Model model) {
        model.addAttribute("restaurants", restaurantService.searchRestaurants(query));
        return "restaurants/list";
    }
    
    @GetMapping("/cuisine/{type}")
    public String getRestaurantsByCuisine(@PathVariable String type, Model model) {
        model.addAttribute("restaurants", restaurantService.getRestaurantsByCuisine(type));
        return "restaurants/list";
    }
    
    @GetMapping("/admin")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth";
        }
        
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "admin/restaurants";
    }
    
    @PostMapping("/admin/toggle/{id}")
    public String toggleRestaurantStatus(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth";
        }
        
        restaurantService.toggleRestaurantStatus(id);
        return "redirect:/restaurants/admin";
    }
    
    @PostMapping("/admin/add")
    public String addRestaurant(@ModelAttribute Restaurant restaurant, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth";
        }
        
        restaurantService.addRestaurant(restaurant);
        return "redirect:/restaurants/admin";
    }
    
    @PostMapping("/admin/update/{id}")
    public String updateRestaurant(@PathVariable Long id, @ModelAttribute Restaurant restaurant, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth";
        }
        
        restaurantService.updateRestaurant(id, restaurant);
        return "redirect:/restaurants/admin";
    }
    
    @PostMapping("/admin/delete/{id}")
    public String deleteRestaurant(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth";
        }
        
        restaurantService.deleteRestaurant(id);
        return "redirect:/restaurants/admin";
    }
} 