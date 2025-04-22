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
import com.fooddelivery.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    
    private final MenuService menuService;
    private final OrderService orderService;
    private final UserService userService;
    private final RecommendationService recommendationService;
    private final NotificationService notificationService;
    private final RestaurantService restaurantService;
    private final PaymentService paymentService;
    
    public CustomerController(MenuService menuService, 
                            OrderService orderService,
                            UserService userService,
                            RecommendationService recommendationService,
                            NotificationService notificationService,
                            RestaurantService restaurantService,
                            PaymentService paymentService) {
        this.menuService = menuService;
        this.orderService = orderService;
        this.userService = userService;
        this.recommendationService = recommendationService;
        this.notificationService = notificationService;
        this.restaurantService = restaurantService;
        this.paymentService = paymentService;
    }
    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth";
        
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
        if (user == null) return "redirect:/auth";
        
        notificationService.markAsRead(id);
        return "redirect:/customer/notifications";
    }

    @PostMapping("/notifications/markAllAsRead")
    public String markAllAsRead(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth";
        
        notificationService.markAllAsRead(user);
        return "redirect:/customer/notifications";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model,
                            @RequestParam(required = false) String basicFilter,
                            @RequestParam(required = false) String userFilter) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.warn("User session is null in dashboard, redirecting to login");
            return "redirect:/auth";
        }
        
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
        if (user == null) return "redirect:/auth";
        
        model.addAttribute("orders", orderService.getUserOrders(user));
        model.addAttribute("user", user);
        return "customer/orders";
    }
    
    @PostMapping("/place-order")
    public String placeOrder(@RequestParam(required = false) List<Long> itemIds,
                           @RequestParam String paymentMethodId,
                           HttpSession session,
                           Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            logger.error("User session is null in placeOrder method");
            return "redirect:/auth/login";
        }
        
        try {
            // Create order items from menu items
            List<OrderItem> orderItems = itemIds.stream()
                .map(id -> menuService.getMenuItemById(id)
                    .map(item -> {
                        OrderItem orderItem = new OrderItem();
                        orderItem.setMenuItem(item);
                        orderItem.setQuantity(1);
                        return orderItem;
                    })
                    .orElse(null))
                .filter(item -> item != null)
                .collect(Collectors.toList());

            // Get restaurant from the first menu item
            Restaurant restaurant = orderItems.isEmpty() ? null : 
                orderItems.get(0).getMenuItem().getRestaurant();

            if (restaurant == null) {
                model.addAttribute("error", "No valid items selected");
                return "customer/cart";
            }

            // Create the order
            Order order = orderService.createOrder(user, restaurant, orderItems);
            
            // Process the payment
            Payment payment = paymentService.processPayment(order, paymentMethodId);
            
            // Update order payment status
            order.setPaymentStatus(payment.getStatus());
            orderService.updateOrderStatus(order.getId(), payment.getStatus());
            
            model.addAttribute("order", order);
            model.addAttribute("payment", payment);
            
            return "customer/order-confirmation";
        } catch (Exception e) {
            logger.error("Error placing order", e);
            model.addAttribute("error", "Failed to place order: " + e.getMessage());
            return "customer/cart";
        }
    }

    @PostMapping("/orders")
    public String createOrder(@RequestParam Long restaurantId, 
                            @RequestParam List<Long> menuItemIds,
                            HttpSession session,
                            Model model) {
        User customer = (User) session.getAttribute("user");
        if (customer == null) {
            return "redirect:/auth/login";
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
            .orElse(null);
        if (restaurant == null) {
            model.addAttribute("error", "Restaurant not found");
            return "customer/error";
        }

        List<OrderItem> orderItems = menuItemIds.stream()
            .map(id -> menuService.getMenuItemById(id)
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItem(item);
                    orderItem.setQuantity(1); // Default quantity
                    return orderItem;
                })
                .orElse(null))
            .filter(item -> item != null)
            .collect(Collectors.toList());

        if (orderItems.isEmpty()) {
            model.addAttribute("error", "No valid menu items found");
            return "customer/error";
        }

        Order order = orderService.createOrder(customer, restaurant, orderItems);
        return "redirect:/customer/orders/" + order.getId();
    }

    @PostMapping("/orders/{orderId}/update")
    public String updateOrderStatus(@PathVariable Long orderId, 
                                  @RequestParam String status,
                                  HttpSession session,
                                  Model model) {
        User customer = (User) session.getAttribute("user");
        if (customer == null) {
            return "redirect:/auth/login";
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getCustomer().getId().equals(customer.getId())) {
            model.addAttribute("error", "Order not found");
            return "customer/error";
        }

        orderService.updateOrderStatus(orderId, status);
        return "redirect:/customer/orders/" + orderId;
    }

    @PostMapping("/order")
    public String createOrderFromView(@RequestParam Long restaurantId, 
                                    @RequestParam List<Long> menuItemIds,
                                    HttpSession session,
                                    Model model) {
        User customer = (User) session.getAttribute("user");
        if (customer == null) {
            return "redirect:/auth/login";
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId)
            .orElse(null);
        if (restaurant == null) {
            model.addAttribute("error", "Restaurant not found");
            return "customer/error";
        }

        List<OrderItem> orderItems = menuItemIds.stream()
            .map(id -> menuService.getMenuItemById(id)
                .map(item -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setMenuItem(item);
                    orderItem.setQuantity(1); // Default quantity
                    return orderItem;
                })
                .orElse(null))
            .filter(item -> item != null)
            .collect(Collectors.toList());

        if (orderItems.isEmpty()) {
            model.addAttribute("error", "No valid menu items found");
            return "customer/error";
        }

        Order order = orderService.createOrder(customer, restaurant, orderItems);
        return "redirect:/customer/orders/" + order.getId();
    }

    @GetMapping("/orders/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth";
        
        Order order = orderService.getOrderById(orderId);
        if (order == null || !order.getCustomer().getId().equals(user.getId())) {
            model.addAttribute("error", "Order not found");
            return "customer/error";
        }
        
        model.addAttribute("order", order);
        model.addAttribute("user", user);
        return "customer/order-details";
    }
}