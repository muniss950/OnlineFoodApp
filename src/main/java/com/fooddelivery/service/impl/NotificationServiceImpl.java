package com.fooddelivery.service.impl;

import com.fooddelivery.model.*;
import com.fooddelivery.notifications.EventType;
import com.fooddelivery.notifications.ObserverType;
import com.fooddelivery.repository.NotificationRepository;
import com.fooddelivery.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void notifyOrderPlaced(Order order) {
        User user = order.getUser();
        Notification customer = new Notification(EventType.ORDER_PLACED, user, order);
        customer.setTitle("Congratulations, your order is placed!");
        customer.setMessage("Your order with ID " + order.getId() + " has been placed.");
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_PLACED, user, order);
        admin.setTitle("New Order Arrived");
        admin.setMessage("Order ID " + order.getId() + " placed by " + user.getUsername() + ". Assign a delivery agent soon.");
        admin.setObserverType(ObserverType.ADMIN);
        notificationRepository.saveAll(List.of(customer, admin));
    }

    @Override
    public void notifyOrderAssigned(Order order) {
        User user = order.getUser();

        Notification customer = new Notification(EventType.ORDER_ASSIGNED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order " + order.getId() + " Assigned!");
        customer.setMessage("Your order is assigned to " + order.getDeliveryAgent().getName() + ".");
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_ASSIGNED, user, order);
        admin.setDeliveryAgent(order.getDeliveryAgent());
        admin.setTitle("Order Assigned");
        admin.setMessage("Order " + order.getId() + " assigned to " + order.getDeliveryAgent().getName());
        admin.setObserverType(ObserverType.ADMIN);
        Notification agent = new Notification(EventType.ORDER_ASSIGNED, user, order);
        agent.setDeliveryAgent(order.getDeliveryAgent());
        agent.setTitle("New Order Assigned");
        agent.setMessage("You've been assigned order " + order.getId());
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }

    @Override
    public void notifyOrderRejected(Order order) {
        User user = order.getUser();

        Notification customer = new Notification(EventType.ORDER_REJECTED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order Rejected");
        customer.setMessage("Delivery agent " + order.getDeliveryAgent() + " rejected your order We'll reassign it soon.");
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_REJECTED, user, order);
        admin.setDeliveryAgent(order.getDeliveryAgent());
        admin.setTitle("Order Rejected");
        admin.setMessage("Order " + order.getId() + " was rejected by the agent" + order.getDeliveryAgent() + " . Please reassign.");
        admin.setObserverType(ObserverType.ADMIN);
        Notification agent = new Notification(EventType.ORDER_REJECTED, user, order);
        agent.setDeliveryAgent(order.getDeliveryAgent());
        agent.setTitle("Order Rejected");
        agent.setMessage("You rejected order " + order.getId());
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }

    @Override
    public void notifyOrderAccepted(Order order) {
        User user = order.getUser();
        Notification customer = new Notification(EventType.ORDER_ACCEPTED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order Accepted");
        customer.setMessage("Delivery agent " + order.getDeliveryAgent().getName() + " accepted your order. Get ready!");
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_ACCEPTED, user, order);
        admin.setDeliveryAgent(order.getDeliveryAgent());
        admin.setTitle("Order Accepted");
        admin.setMessage("Order " + order.getId() + " accepted by " + order.getDeliveryAgent().getName());
        admin.setObserverType(ObserverType.ADMIN);
        Notification agent = new Notification(EventType.ORDER_ACCEPTED, user, order);
        agent.setDeliveryAgent(order.getDeliveryAgent());
        agent.setTitle("You Accepted the Order");
        agent.setMessage("You accepted order " + order.getId() + ". Deliver it on time!");
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }

    @Override
    public void notifyOrderDelivered(Order order) {
        User user = order.getUser();
        Notification customer = new Notification(EventType.ORDER_DELIVERED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order Delivered");
        customer.setMessage("Your order " + order.getId() + " has been delivered by " + order.getDeliveryAgent().getName());
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_DELIVERED, user, order);
        admin.setDeliveryAgent(order.getDeliveryAgent());
        admin.setTitle("Order Delivered");
        admin.setMessage("Order " + order.getId() + " has been delivered by " + order.getDeliveryAgent().getName());
        admin.setObserverType(ObserverType.ADMIN);
        Notification agent = new Notification(EventType.ORDER_DELIVERED, user, order);
        agent.setDeliveryAgent(order.getDeliveryAgent());
        agent.setTitle("Order Delivered");
        agent.setMessage("You have successfully delivered order " + order.getId());
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }

    @Override
    public void notifyUser(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle("Notification");
        notification.setMessage(message);
        notification.setObserverType(ObserverType.CUSTOMER);
        notification.setEventType(EventType.ORDER_PLACED); // Default event type
        
        notificationRepository.save(notification);
    }

    @Override
    public void notifyRestaurant(Restaurant restaurant, String message) {
        Notification notification = new Notification();
        notification.setRestaurant(restaurant);
        notification.setTitle("Restaurant Notification");
        notification.setMessage(message);
        notification.setObserverType(ObserverType.RESTAURANT);
        notification.setEventType(EventType.ORDER_PLACED); // Default event type
        
        notificationRepository.save(notification);
    }

    @Override
    public void notifyDeliveryAgent(DeliveryAgent agent, String message) {
        Notification notification = new Notification();
        notification.setDeliveryAgent(agent);
        notification.setTitle("Delivery Agent Notification");
        notification.setMessage(message);
        notification.setObserverType(ObserverType.DELIVERY_AGENT);
        notification.setEventType(EventType.ORDER_ASSIGNED); // Default event type
        
        notificationRepository.save(notification);
    }

    @Override
    public void notifyAdmin(String message) {
        Notification notification = new Notification();
        notification.setTitle("Admin Notification");
        notification.setMessage(message);
        notification.setObserverType(ObserverType.ADMIN);
        notification.setEventType(EventType.ORDER_PLACED); // Default event type
        
        notificationRepository.save(notification);
    }
    
    @Override
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserAndObserverType(user, ObserverType.CUSTOMER);
    }

    @Override
    public List<Notification> getAdminNotifications(User user) {
        return notificationRepository.findByObserverType(ObserverType.ADMIN);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAsReadAdmin(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead(User user) {
        List<Notification> notifications = getUserNotifications(user);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public void markAllAsReadAdmin(User user) {
        List<Notification> notifications = getAdminNotifications(user);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public int getUnreadCount(User user) {
        return notificationRepository.countByUserAndObserverTypeAndReadFalse(user, ObserverType.CUSTOMER);
    }

    @Override
    public int getUnreadCountAdmin(User user) {
        return notificationRepository.countByObserverTypeAndReadFalse(ObserverType.ADMIN);
    }

    @Override
    public List<Notification> getAgentNotifications(DeliveryAgent agent) {
        return notificationRepository.findByDeliveryAgentAndObserverType(agent, ObserverType.DELIVERY_AGENT);
    }

    @Override
    public void markAsReadForAgent(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsReadForAgent(DeliveryAgent agent) {
        List<Notification> notifications = getAgentNotifications(agent);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

    @Override
    public int getUnreadCountForAgent(DeliveryAgent agent) {
        return notificationRepository.countByDeliveryAgentAndObserverTypeAndReadFalse(agent, ObserverType.DELIVERY_AGENT);
    }
} 