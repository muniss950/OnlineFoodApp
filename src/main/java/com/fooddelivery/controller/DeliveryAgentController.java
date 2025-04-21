package com.fooddelivery.controller;

import org.springframework.stereotype.Controller;

import com.fooddelivery.model.DeliveryAgent;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.User;
import com.fooddelivery.service.DeliveryAgentService;
import com.fooddelivery.service.NotificationService;
import com.fooddelivery.service.OrderService;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;
import com.fooddelivery.model.Notification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/delivery_agent")
public class DeliveryAgentController {
    private final OrderService orderService;
    private final DeliveryAgentService deliveryAgentService;
    private final NotificationService notificationService;
    public DeliveryAgentController(OrderService orderService , DeliveryAgentService deliveryAgentService,NotificationService notificationService){
        this.deliveryAgentService = deliveryAgentService;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session , Model model) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if(agent == null){
            return "redirect:/auth/login";
        }
        List<Order> assignedOrders = orderService.getOrdersByDeliveryAgentAndStatus(agent.getId(), "ASSIGNED");
        List<Order> acceptedOrders = orderService.getOrdersByDeliveryAgentAndStatus(agent.getId(), "ON_THE_WAY");
        model.addAttribute("assignedOrders", assignedOrders);
        model.addAttribute("acceptedOrders", acceptedOrders);
        return "delivery_agent/dashboard";


        
    }
    @PostMapping("/accept-order")
    public String acceptOrder(@RequestParam Long orderId,HttpSession session) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if(agent == null){
            return "redirect:/auth/login";
        }
        orderService.updateOrderStatus(orderId, "ON_THE_WAY");
        return "redirect:/delivery_agent/dashboard";
       
    }
    @PostMapping("/mark-delivered")
    public String markDelivered(@RequestParam Long orderId, HttpSession session) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if(agent == null){
            return "redirect:/auth/login";
        }
        orderService.updateOrderStatus(orderId, "DELIVERED");
        return "redirect:/delivery_agent/dashboard";
    }
    @PostMapping("/reject-order")
    public String rejectOrder(@RequestParam Long orderId, HttpSession session) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if(agent == null){
            return "redirect:/auth/login";
        }
        orderService.updateOrderStatus(orderId, "PREPARING");
        return "redirect:/delivery_agent/dashboard";
    }
    @GetMapping("/notifications")
    public String viewNotifications(HttpSession session, Model model) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if (agent == null) return "redirect:/auth/login";
        
        List<Notification> notifications = notificationService.getAgentNotifications(agent);
        int unreadCount = notificationService.getUnreadCountForAgent(agent);
        
        model.addAttribute("agent", agent);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "delivery_agent/notifications";
    }

    @PostMapping("/notifications/markAsRead/{id}")
    public String markAsReadForAgent(@PathVariable Long id, HttpSession session) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if (agent == null) return "redirect:/auth/login";
        
        notificationService.markAsReadForAgent(id);
        return "redirect:/delivery_agent/notifications";
    }

    @PostMapping("/notifications/markAllAsRead")
    public String markAllAsRead(HttpSession session) {
        DeliveryAgent agent = (DeliveryAgent) session.getAttribute("deliveryAgent");
        if (agent == null) return "redirect:/auth/login";
        
        notificationService.markAllAsReadForAgent(agent);
        return "redirect:/delivery_agent/notifications";
    }

    
    

}
