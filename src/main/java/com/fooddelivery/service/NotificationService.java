package com.fooddelivery.service;

import com.fooddelivery.model.*;
import java.util.List;

public interface NotificationService {
    void notifyOrderPlaced(Order order);
    void notifyOrderAssigned(Order order);
    void notifyOrderAccepted(Order order);
    void notifyOrderRejected(Order order);
    void notifyOrderDelivered(Order order);
    
    // Specific notification methods
    void notifyUser(User user, String message);
    void notifyRestaurant(Restaurant restaurant, String message);
    void notifyDeliveryAgent(DeliveryAgent agent, String message);
    void notifyAdmin(String message);
    
    // Notification management methods
    List<Notification> getUserNotifications(User user);
    List<Notification> getAdminNotifications(User user);
    void markAsRead(Long notificationId);
    void markAsReadAdmin(Long notificationId);
    void markAllAsRead(User user);
    void markAllAsReadAdmin(User user);
    int getUnreadCount(User user);
    int getUnreadCountAdmin(User user);
    
    // Delivery agent notification methods
    List<Notification> getAgentNotifications(DeliveryAgent agent);
    void markAsReadForAgent(Long notificationId);
    void markAllAsReadForAgent(DeliveryAgent agent);
    int getUnreadCountForAgent(DeliveryAgent agent);
}
