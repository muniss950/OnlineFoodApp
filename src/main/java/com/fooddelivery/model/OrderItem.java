package com.fooddelivery.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(nullable = false)
    private Double discountedPrice;
    
    @Column
    private String name;
    
    @Column
    private String description;
    
    @Column
    private Boolean largeSize;
    
    @Column
    private Boolean premium;
    
    // Constructors, Getters, and Setters
    public OrderItem() {
        this.largeSize = false;
        this.premium = false;
        this.price = 0.0;
        this.discountedPrice = 0.0;
        this.quantity = 0;
    }
    
    public OrderItem(MenuItem menuItem, Integer quantity) {
        this();
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.price = menuItem.getPrice();
        this.discountedPrice = this.price;
        this.name = menuItem.getName();
        this.description = menuItem.getDescription();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public MenuItem getMenuItem() {
        return menuItem;
    }
    
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Double getDiscountedPrice() {
        return discountedPrice;
    }
    
    public void setDiscountedPrice(Double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean isLargeSize() {
        return largeSize != null ? largeSize : false;
    }
    
    public void setLargeSize(Boolean largeSize) {
        this.largeSize = largeSize;
    }
    
    public Boolean isPremium() {
        return premium != null ? premium : false;
    }
    
    public void setPremium(Boolean premium) {
        this.premium = premium;
    }
}