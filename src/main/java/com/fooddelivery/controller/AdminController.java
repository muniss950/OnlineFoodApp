package com.fooddelivery.controller;

import com.fooddelivery.model.*;
import com.fooddelivery.notifications.OrderEvent;
import com.fooddelivery.service.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final MenuService menuService;
    private final OrderService orderService;
    private final DeliveryAgentService deliveryAgentService;
    private final NotificationService notificationService ;
    private final RestaurantService restaurantService;
    
    public AdminController(MenuService menuService,
                         OrderService orderService,
                         DeliveryAgentService deliveryAgentService,
                         NotificationService notificationService,
                         RestaurantService restaurantService) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.deliveryAgentService = deliveryAgentService;
        this.notificationService = notificationService;
        this.restaurantService = restaurantService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("pendingOrders", orderService.getOrdersByStatus("PENDING"));
        model.addAttribute("restaurants", restaurantService.getAllRestaurants());
        return "admin/dashboard";
    }
    
    @GetMapping("/menu")
    public String manageMenu(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }

        
        
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "admin/menu";
    }
    
    @PostMapping("/menu/add")
    public String addMenuItem(@RequestParam String name,
                            @RequestParam String description,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "false") boolean available) {
        MenuItem item = new MenuItem(name, description, price, available);
        menuService.addMenuItem(item);
        return "redirect:/admin/menu";
    }
    
    @PostMapping("/menu/update/{id}")
    public String updateMenuItem(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam String description,
                               @RequestParam double price,
                               @RequestParam boolean available,
                               @RequestParam double discountPercentage
                               ) {
                    
        Optional<MenuItem> existingItem = menuService.getMenuItemById(id);
        MenuItem item = new MenuItem(name, description, price, available,discountPercentage);
        item.setId(id);
        if(existingItem.isPresent()){
            item.setImageUrl(existingItem.get().getImageUrl());
        }
        item.setId(id);
        menuService.updateMenuItem(id, item);
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/menu/delete/{id}")
    public String deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return "redirect:/admin/menu";
    }
    
    @GetMapping("/orders")
    public String manageOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("orders", orderService.getOrdersByStatus("PENDING"));
        model.addAttribute("agents", deliveryAgentService.getAvailableAgents());
        return "admin/orders";
    }
    
    @PostMapping("/orders/assign")
    public String assignOrder(@RequestParam Long orderId,
                            @RequestParam Long agentId) {
        orderService.assignDeliveryAgent(orderId, agentId);
        
        return "redirect:/admin/orders";
    }
    
    @GetMapping("/agents")
    public String manageAgents(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("agents", deliveryAgentService.getAllAgents());
        return "admin/agents";
    }
    
    @PostMapping("/agents/add")
    public String addAgent(@RequestParam String name,
                            @RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String phone
                          ) {
        DeliveryAgent agent = new DeliveryAgent(name,username,password,phone, true);
        deliveryAgentService.addAgent(agent);
        return "redirect:/admin/agents";
    }
    
    @PostMapping("/agents/update/{id}")
    public String updateAgentAvailability(@PathVariable Long id,
    @RequestParam(name = "available", defaultValue = "false") boolean available) {
        deliveryAgentService.updateAvailability(id, available);
        return "redirect:/admin/agents";
    }
    @GetMapping("/notifications")
    public String viewNotificationsAdmin(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        List<Notification> notifications = notificationService.getAdminNotifications(user);
        int unreadCount = notificationService.getUnreadCountAdmin(user);
        model.addAttribute("user", user);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "admin/notifications";
    }
    @PostMapping("/notifications/markAsRead/{id}")
    public String markAsReadAdmin(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        notificationService.markAsReadAdmin(id);
        return "redirect:/admin/notifications";
    }

    @PostMapping("/notifications/markAllAsRead")
    public String markAllAsReadAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        notificationService.markAllAsReadAdmin(user);
        return "redirect:/admin/notifications";
    }
}