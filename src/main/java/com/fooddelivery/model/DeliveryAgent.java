package com.fooddelivery.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "delivery_agents")
public class DeliveryAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String phone;
    
    @Column(nullable = false)
    private boolean available;
    
    @OneToMany(mappedBy = "deliveryAgent")
    private List<Order> orders;
    
    // Constructors, Getters, and Setters
    public DeliveryAgent() {}
    
    public DeliveryAgent(String name, String username, String password, String phone, boolean available) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.available = available;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
}