package com.fooddelivery.service;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.NotificationRepository;

import org.springframework.transaction.annotation.Transactional;

import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import java.util.List;
import com.fooddelivery.notifications.EventType;
import com.fooddelivery.notifications.ObserverType;
@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

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
        agent.setMessage("You’ve been assigned order " + order.getId());
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }

    public void notifyOrderRejected(Order order) {
        User user = order.getUser();

        Notification customer = new Notification(EventType.ORDER_REJECTED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order Rejected");
        customer.setMessage("Delivery agent " +order.getDeliveryAgent() + " rejected your order We'll reassign it soon.");
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

    public void notifyOrderDelivered(Order order) {
        User user = order.getUser();

        Notification customer = new Notification(EventType.ORDER_DELIVERED, user, order);
        customer.setDeliveryAgent(order.getDeliveryAgent());
        customer.setTitle("Order Delivered");
        customer.setMessage("Your order was delivered by " + order.getDeliveryAgent().getName());
        customer.setObserverType(ObserverType.CUSTOMER);
        Notification admin = new Notification(EventType.ORDER_DELIVERED, user, order);
        admin.setDeliveryAgent(order.getDeliveryAgent());
        admin.setTitle("Order Delivered");
        admin.setMessage("Order " + order.getId() + " delivered by " + order.getDeliveryAgent().getName());
        admin.setObserverType(ObserverType.ADMIN);
        Notification agent = new Notification(EventType.ORDER_DELIVERED, user, order);
        agent.setDeliveryAgent(order.getDeliveryAgent());
        agent.setTitle("Order Delivered");
        agent.setMessage("You delivered order " + order.getId() + ". Great job!");
        agent.setObserverType(ObserverType.DELIVERY_AGENT);
        notificationRepository.saveAll(List.of(customer, admin, agent));
    }
    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }
    public List<Notification> getAdminNotifications(User user) {
        return notificationRepository.findByAdminOrderByCreatedAtDesc(user);
    }
    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }
    public void markAsReadAdmin(Long notificationId) {
        notificationRepository.markAsReadAdmin(notificationId);
    }
    public void markAllAsRead(User user) {
        notificationRepository.markAllAsRead(user);
    }
    public void markAllAsReadAdmin(User user) {
        notificationRepository.markAllAsReadAdmin(user);
    }
    public int getUnreadCount(User user) {
        return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user).size();
    }
    public int getUnreadCountAdmin(User user) {
        return notificationRepository.findByAdminAndReadFalseOrderByCreatedAtDesc(user).size();
    }
    public List<Notification> getAgentNotifications(DeliveryAgent agent){
        return notificationRepository.findByDeliveryAgentOrderByCreatedAtDesc(agent);
    }
    public void markAsReadForAgent(Long notificationId) {
        notificationRepository.markAsReadForAgent(notificationId);
    }
    public void markAllAsReadForAgent(DeliveryAgent agent) {
        notificationRepository.markAllAsReadForAgent(agent);
    }
    public int getUnreadCountForAgent(DeliveryAgent agent) {
        return notificationRepository.findByDeliveryAgentAndReadFalseOrderByCreatedAtDesc(agent).size();
    }
    
}
