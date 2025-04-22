package com.fooddelivery.builder;

import com.fooddelivery.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder pattern implementation for creating Order objects
 */
public class OrderBuilder {
    private User customer;
    private Restaurant restaurant;
    private DeliveryAgent deliveryAgent;
    private List<OrderItem> items = new ArrayList<>();
    private String status = "PENDING";
    private LocalDateTime orderTime = LocalDateTime.now();
    private String specialInstructions;
    private double deliveryFee = 5.0;
    private double taxRate = 0.1; // 10% tax
    private boolean isPremiumDelivery = false;
    
    public OrderBuilder setCustomer(User customer) {
        this.customer = customer;
        return this;
    }
    
    public OrderBuilder setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
        return this;
    }
    
    public OrderBuilder setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
        return this;
    }
    
    public OrderBuilder addItem(MenuItem menuItem, int quantity) {
        OrderItem orderItem = new OrderItem();
        orderItem.setMenuItem(menuItem);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(menuItem.getPrice());
        items.add(orderItem);
        return this;
    }
    
    public OrderBuilder setStatus(String status) {
        this.status = status;
        return this;
    }
    
    public OrderBuilder setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
        return this;
    }
    
    public OrderBuilder setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
        return this;
    }
    
    public OrderBuilder setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
        return this;
    }
    
    public OrderBuilder setTaxRate(double taxRate) {
        this.taxRate = taxRate;
        return this;
    }
    
    public OrderBuilder setPremiumDelivery(boolean isPremiumDelivery) {
        this.isPremiumDelivery = isPremiumDelivery;
        if (isPremiumDelivery) {
            this.deliveryFee = 10.0; // Premium delivery costs more
        }
        return this;
    }
    
    public Order build() {
        if (customer == null) {
            throw new IllegalStateException("Customer is required");
        }
        if (restaurant == null) {
            throw new IllegalStateException("Restaurant is required");
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setRestaurant(restaurant);
        order.setDeliveryAgent(deliveryAgent);
        order.setStatus(status);
        order.setOrderTime(orderTime);
        order.setSpecialInstructions(specialInstructions);
        order.setDeliveryFee(deliveryFee);
        order.setTaxRate(taxRate);
        order.setPremiumDelivery(isPremiumDelivery);
        
        // Calculate total price
        double subtotal = items.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
        
        double tax = subtotal * taxRate;
        double total = subtotal + tax + deliveryFee;
        
        order.setSubtotal(subtotal);
        order.setTax(tax);
        order.setTotal(total);
        
        // Set order items
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(items);
        
        return order;
    }
} 