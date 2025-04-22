package com.fooddelivery.service;

import com.fooddelivery.model.*;
import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    Order createOrder(User customer, Restaurant restaurant, List<OrderItem> items);
    List<Order> getUserOrders(User user);
    List<Order> getOrdersByStatus(String status);
    List<Order> getOrdersByDeliveryAgentAndStatus(Long agentId, String status);
    Order assignDeliveryAgent(Long orderId, Long agentId);
    Order updateOrderStatus(Long orderId, String status);
    List<Order> getRestaurantOrdersByDate(Long restaurantId, LocalDate date);
    List<Order> getRestaurantOrdersByStatus(Long restaurantId, String status);
    List<Order> getRestaurantOrders(Long restaurantId);
    Order getOrderById(Long id);
    Order saveOrder(Order order);
    List<Order> getUserOrders(Long userId);
    List<OrderItem> getOrderItems(Long orderId);
}

