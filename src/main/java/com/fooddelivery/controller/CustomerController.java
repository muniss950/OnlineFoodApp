package com.fooddelivery.controller;
import com.fooddelivery.notifications.EventType;
import com.fooddelivery.model.*;
import com.fooddelivery.notifications.OrderEvent;
import com.fooddelivery.service.MenuService;
import com.fooddelivery.service.NotificationService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.RecommendationService;
import com.fooddelivery.service.UserService;
import com.fooddelivery.service.RestaurantService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private final MenuService menuService;
    private final OrderService orderService;
    private final UserService userService;
    private final RecommendationService recommendationService;
    private final NotificationService notificationService;
    private final RestaurantService restaurantService;
    
    public CustomerController(MenuService menuService, 
                            OrderService orderService,
                            UserService userService,
                            RecommendationService recommendationService,
                            NotificationService notificationService,
                            RestaurantService restaurantService) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.userService = userService;
        this.recommendationService = recommendationService;
        this.notificationService = notificationService;
        this.restaurantService = restaurantService;
    }
    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        List<Notification> notifications = notificationService.getUserNotifications(user);
        int unreadCount = notificationService.getUnreadCount(user);
        model.addAttribute("user", user);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "customer/notifications";
    }

    @PostMapping("/notifications/markAsRead/{id}")
    public String markAsRead(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        notificationService.markAsRead(id);
        return "redirect:/customer/notifications";
    }

    @PostMapping("/notifications/markAllAsRead")
    public String markAllAsRead(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        notificationService.markAllAsRead(user);
        return "redirect:/customer/notifications";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model,
                            @RequestParam(required = false) String basicFilter,
                            @RequestParam(required = false) String userFilter) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        List<MenuItem> menuItems;
        if (userFilter != null && !userFilter.isEmpty() && basicFilter != null && !basicFilter.isEmpty()) {
            // Both filters are selected - get intersection of both recommendation lists
            List<MenuItem> basicRecommended = recommendationService.recommendBasicItems(basicFilter);
            List<MenuItem> userRecommended = recommendationService.recommendUserBasedItems(userFilter, user);
            
            // Create intersection of both lists
            menuItems = new ArrayList<>();
            for (MenuItem item : basicRecommended) {
                if (userRecommended.contains(item)) {
                    menuItems.add(item);
                }
            }
        }
        else if(userFilter != null && !userFilter.isEmpty()) {
            menuItems = recommendationService.recommendUserBasedItems(userFilter,user);
        } else if(basicFilter != null && !basicFilter.isEmpty()){
            menuItems = recommendationService.recommendBasicItems(basicFilter);
        }
        else{
            menuItems = menuService.getAvailableMenuItems();
        }

        // Get all active restaurants
        List<Restaurant> restaurants = restaurantService.getActiveRestaurants();
        
        model.addAttribute("user", user);
        model.addAttribute("menuItems", menuItems);
        model.addAttribute("restaurants", restaurants);
        model.addAttribute("selectedBasicFilter", basicFilter);
        model.addAttribute("selectedUserFilter", userFilter);
        return "customer/dashboard";
    }

    
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        model.addAttribute("orders", orderService.getUserOrders(user));
        return "customer/orders";
    }
    
    @PostMapping("/order")
    public String placeOrder(@RequestParam List<Long> itemIds,
                           @RequestParam List<Integer> quantities,
                           @RequestParam Long restaurantId,
                           HttpSession session,
                           HttpServletRequest request) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth/login";
        
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            Integer quantity = quantities.get(i);
            
            if (quantity > 0) {
                MenuItem menuItem = menuService.getMenuItemById(itemId)
                    .orElseThrow(() -> new RuntimeException("Menu item not found"));
                if (menuItem.getRestaurant().getId().equals(restaurant.getId())) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItem(menuItem);
                    orderItem.setQuantity(quantity);
                    orderItem.setPrice(menuItem.getPrice());
                    
                    // Check size selection
                    String sizeParam = request.getParameter("size_" + itemId);
                    orderItem.setLargeSize("large".equals(sizeParam));
                    
                    // Check premium service selection
                    String premiumParam = request.getParameter("premium_" + itemId);
                    orderItem.setPremium("true".equals(premiumParam));
                    
                    items.add(orderItem);
                }
            }
        }
        
        if (!items.isEmpty()) {
            orderService.createOrder(user, restaurant, items);
        }
        
        return "redirect:/customer/orders";
    }
}