package com.fooddelivery.facade;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * Facade pattern implementation to simplify the complex interactions
 * between order processing, restaurant notification, and delivery assignment.
 */
@Component
public class DeliveryFacade {
    
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final DeliveryAgentService deliveryAgentService;
    private final RestaurantService restaurantService;
    
    @Autowired
    public DeliveryFacade(OrderService orderService, 
                          NotificationService notificationService,
                          DeliveryAgentService deliveryAgentService,
                          RestaurantService restaurantService) {
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.deliveryAgentService = deliveryAgentService;
        this.restaurantService = restaurantService;
    }
    
    /**
     * Process a new order with a simplified interface
     * This method handles the entire order lifecycle from creation to delivery assignment
     */
    public Order processNewOrder(User customer, Restaurant restaurant, List<OrderItem> items) {
        // 1. Create the order
        Order order = orderService.createOrder(customer, restaurant, items);
        
        // 2. Notify the restaurant
        notificationService.notifyRestaurant(restaurant, "New order received: #" + order.getId());
        
        // 3. Find available delivery agent
        DeliveryAgent agent = deliveryAgentService.findAvailableAgent();
        
        // 4. Assign delivery agent to order
        if (agent != null) {
            orderService.assignDeliveryAgent(order.getId(), agent.getId());
            notificationService.notifyDeliveryAgent(agent, "New delivery assignment: Order #" + order.getId());
        } else {
            notificationService.notifyAdmin("No available delivery agents for order #" + order.getId());
        }
        
        return order;
    }
    
    /**
     * Update order status with notifications
     */
    public void updateOrderStatus(Long orderId, String status) {
        // 1. Update order status
        orderService.updateOrderStatus(orderId, status);
        
        // 2. Get order details
        Order order = orderService.getOrderById(orderId);
        
        // 3. Notify relevant parties based on status
        switch (status) {
            case "PROCESSING":
                notificationService.notifyUser(order.getUser(), 
                    "Your order #" + orderId + " is being prepared");
                break;
            case "READY_FOR_DELIVERY":
                notificationService.notifyDeliveryAgent(order.getDeliveryAgent(), 
                    "Order #" + orderId + " is ready for pickup");
                break;
            case "OUT_FOR_DELIVERY":
                notificationService.notifyUser(order.getUser(), 
                    "Your order #" + orderId + " is out for delivery");
                break;
            case "DELIVERED":
                notificationService.notifyUser(order.getUser(), 
                    "Your order #" + orderId + " has been delivered");
                notificationService.notifyRestaurant(order.getRestaurant(), 
                    "Order #" + orderId + " has been delivered");
                break;
            case "CANCELLED":
                notificationService.notifyUser(order.getUser(), 
                    "Your order #" + orderId + " has been cancelled");
                notificationService.notifyRestaurant(order.getRestaurant(), 
                    "Order #" + orderId + " has been cancelled");
                if (order.getDeliveryAgent() != null) {
                    notificationService.notifyDeliveryAgent(order.getDeliveryAgent(), 
                        "Order #" + orderId + " has been cancelled");
                }
                break;
        }
    }
    
    /**
     * Get order details with all related information
     */
    public OrderDetails getOrderDetails(Long orderId) {
        Order order = orderService.getOrderById(orderId);
        
        return new OrderDetails(
            order,
            order.getRestaurant(),
            order.getUser(),
            order.getDeliveryAgent(),
            orderService.getOrderItems(orderId)
        );
    }
} 