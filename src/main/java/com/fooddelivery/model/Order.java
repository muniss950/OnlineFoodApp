package com.fooddelivery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
    
    @Column(nullable = false)
    private Double totalAmount;
    
    @Column(nullable = false)
    private String status; // PENDING, CONFIRMED, PREPARING, READY, DELIVERED, CANCELLED
    
    @ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private DeliveryAgent deliveryAgent;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderTime;
    
    @Column
    private LocalDateTime deliveryTime;
    
    @Column
    private String deliveryAddress;
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;
    
    @Column(name = "payment_status")
    private String paymentStatus = "PENDING";
    
    @Column
    private String paymentMethod;
    
    @Column
    private Double discount;
    
    @Column
    private String specialInstructions;
    
    @Column
    private Double deliveryFee;
    
    @Column
    private Double taxRate;
    
    @Column
    private Boolean premiumDelivery;
    
    @Column
    private Double subtotal;
    
    @Column
    private Double tax;
    
    @Column
    private Double total;
    
    // Constructors, Getters, and Setters
    public Order() {
        this.orderTime = LocalDateTime.now();
        this.status = "PENDING";
        this.paymentStatus = "PENDING";
        this.deliveryFee = 0.0;
        this.taxRate = 0.0;
        this.discount = 0.0;
        this.subtotal = 0.0;
        this.tax = 0.0;
        this.total = 0.0;
        this.totalAmount = 0.0;
        this.premiumDelivery = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    // Compatibility methods
    public User getCustomer() {
        return user;
    }
    
    public void setCustomer(User user) {
        this.user = user;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    
    // Compatibility methods
    public List<OrderItem> getOrderItems() {
        return items;
    }
    
    public void setOrderItems(List<OrderItem> items) {
        this.items = items;
    }
    
    public Double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // Compatibility methods
    public Double getTotalPrice() {
        return totalAmount;
    }
    
    public void setTotalPrice(Double totalPrice) {
        this.totalAmount = totalPrice;
    }
    
    public Double getTotalDiscountedPrice() {
        return totalAmount - (discount != null ? discount : 0.0);
    }
    
    public void setTotalDiscountedPrice(Double totalDiscountedPrice) {
        this.discount = totalAmount - totalDiscountedPrice;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public DeliveryAgent getDeliveryAgent() {
        return deliveryAgent;
    }
    
    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    // Explicitly named methods to match database column
    public LocalDateTime getOrderDate() {
        return orderTime;
    }
    
    public void setOrderDate(LocalDateTime orderDate) {
        this.orderTime = orderDate;
    }
    
    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    
    public Payment getPayment() {
        return payment;
    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Double getDiscount() {
        return discount;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
    
    public String getSpecialInstructions() {
        return specialInstructions;
    }
    
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
    
    public Double getDeliveryFee() {
        return deliveryFee;
    }
    
    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
    
    public Double getTaxRate() {
        return taxRate;
    }
    
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }
    
    public Boolean isPremiumDelivery() {
        return premiumDelivery;
    }
    
    public void setPremiumDelivery(Boolean premiumDelivery) {
        this.premiumDelivery = premiumDelivery;
    }
    
    public Double getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
    
    public Double getTax() {
        return tax;
    }
    
    public void setTax(Double tax) {
        this.tax = tax;
    }
    
    public Double getTotal() {
        return total;
    }
    
    public void setTotal(Double total) {
        this.total = total;
    }
}