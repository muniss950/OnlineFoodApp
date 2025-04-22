package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurant")
public class RestaurantPortalController {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantPortalController.class);
    
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private Restaurant getRestaurantFromSession(HttpSession session) {
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        if (restaurantId == null) {
            return null;
        }
        return restaurantService.getRestaurantById(restaurantId)
                .orElse(null);
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        if (restaurantId == null) {
            return "redirect:/auth";
        }

        try {
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            
            List<Order> pendingOrders = orderService.getRestaurantOrdersByStatus(restaurantId, "PENDING");
            List<Order> processingOrders = orderService.getRestaurantOrdersByStatus(restaurantId, "PROCESSING");
            List<MenuItem> menuItems = menuService.getRestaurantMenuItems(restaurantId);
            
            model.addAttribute("restaurant", restaurant);
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("processingOrders", processingOrders);
            model.addAttribute("menuItems", menuItems);
            
            return "restaurant/dashboard";
        } catch (Exception e) {
            logger.error("Error loading restaurant dashboard: {}", e.getMessage(), e);
            model.addAttribute("error", "Failed to load dashboard: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/menu")
    public String menu(Model model, HttpSession session) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        List<MenuItem> menuItems = menuService.getRestaurantMenuItems(restaurant.getId());
        model.addAttribute("menuItems", menuItems);
        return "restaurant/menu";
    }

    @PostMapping("/menu/save")
    public String saveMenuItem(@ModelAttribute MenuItem menuItem, HttpSession session, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        menuItem.setRestaurant(restaurant);
        menuService.saveMenuItem(menuItem);
        redirectAttributes.addFlashAttribute("message", "Menu item saved successfully!");
        return "redirect:/restaurant/menu";
    }

    @PostMapping("/menu/toggle/{id}")
    public String toggleMenuItem(@PathVariable Long id, HttpSession session) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        menuService.getMenuItemById(id).ifPresent(menuItem -> {
            if (menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                menuItem.setAvailable(!menuItem.isAvailable());
                menuService.saveMenuItem(menuItem);
            }
        });
        return "redirect:/restaurant/menu";
    }

    @PostMapping("/menu/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        menuService.getMenuItemById(id).ifPresent(menuItem -> {
            if (menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                menuService.deleteMenuItem(id);
                redirectAttributes.addFlashAttribute("message", "Menu item deleted successfully!");
            }
        });
        return "redirect:/restaurant/menu";
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(required = false) String status,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                        Model model, HttpSession session) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        List<Order> orders;
        if (status != null && !status.equals("ALL")) {
            orders = orderService.getRestaurantOrdersByStatus(restaurant.getId(), status);
        } else if (date != null) {
            orders = orderService.getRestaurantOrdersByDate(restaurant.getId(), date);
        } else {
            orders = orderService.getRestaurantOrders(restaurant.getId());
        }

        model.addAttribute("orders", orders);
        return "restaurant/orders";
    }

    @PostMapping("/orders/update/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status,
                                  HttpSession session, RedirectAttributes redirectAttributes) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        Order order = orderService.getOrderById(id);
        if (order != null && order.getRestaurant().getId().equals(restaurant.getId())) {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("message", "Order status updated successfully!");
        }
        return "redirect:/restaurant/orders";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        if (restaurantId == null) {
            return "redirect:/auth";
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        model.addAttribute("restaurant", restaurant);
        return "restaurant/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Restaurant updatedRestaurant,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Long restaurantId = (Long) session.getAttribute("restaurantId");
        if (restaurantId == null) {
            return "redirect:/auth";
        }

        try {
            Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
            
            // Update only the allowed fields
            restaurant.setName(updatedRestaurant.getName());
            restaurant.setAddress(updatedRestaurant.getAddress());
            restaurant.setPhone(updatedRestaurant.getPhone());
            restaurant.setEmail(updatedRestaurant.getEmail());
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
            restaurant.setOperatingHours(updatedRestaurant.getOperatingHours());
            
            restaurantService.updateRestaurant(restaurantId, restaurant);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }
        
        return "redirect:/restaurant/profile";
    }

    @PostMapping("/profile/password")
    public String updatePassword(@RequestParam String currentPassword,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Restaurant restaurant = getRestaurantFromSession(session);
        if (restaurant == null) {
            return "redirect:/auth";
        }

        User user = userService.getUserByRestaurant(restaurant);
        if (user != null && userService.updatePassword(user, currentPassword, newPassword, confirmPassword)) {
            redirectAttributes.addFlashAttribute("message", "Password updated successfully!");
            redirectAttributes.addFlashAttribute("success", true);
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to update password. Please check your current password.");
            redirectAttributes.addFlashAttribute("success", false);
        }
        return "redirect:/restaurant/profile";
    }
} 