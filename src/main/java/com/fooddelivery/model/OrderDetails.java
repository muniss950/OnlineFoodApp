package com.fooddelivery.model;

import java.util.List;

/**
 * DTO (Data Transfer Object) to encapsulate comprehensive order details
 * including order, restaurant, customer, delivery agent, and order items.
 */
public class OrderDetails {
    private final Order order;
    private final Restaurant restaurant;
    private final User customer;
    private final DeliveryAgent deliveryAgent;
    private final List<OrderItem> orderItems;
    
    public OrderDetails(Order order, Restaurant restaurant, User customer, DeliveryAgent deliveryAgent, List<OrderItem> orderItems) {
        this.order = order;
        this.restaurant = restaurant;
        this.customer = customer;
        this.deliveryAgent = deliveryAgent;
        this.orderItems = orderItems;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public User getCustomer() {
        return customer;
    }
    
    public DeliveryAgent getDeliveryAgent() {
        return deliveryAgent;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public double getTotalAmount() {
        return order.getTotalAmount();
    }
    
    public double getDiscountedAmount() {
        return order.getTotalDiscountedPrice();
    }
    
    public String getStatus() {
        return order.getStatus();
    }
} 