package com.fooddelivery.model;

import java.time.LocalDateTime;
import com.fooddelivery.notifications.EventType;
import com.fooddelivery.notifications.ObserverType;

import jakarta.persistence.*;

@Entity
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ObserverType observerType;

    @Enumerated(EnumType.STRING)
    @Column
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private  Order order;

    @ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private DeliveryAgent deliveryAgent;

    public Notification() {}
    public Notification(EventType eventType, User user, Order order) {
        this.eventType = eventType;
        this.user = user;
        this.order = order;
    }
    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getMessage() {return message; }
    public void setMessage(String message) {this.message = message ;}
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt = createdAt;}
    public void setOrder(Order order) {this.order = order;}
    public Order getOrder() {return order;}
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public ObserverType getObserverType(){return observerType;}
    public void setObserverType(ObserverType observerType){this.observerType = observerType;}
    public EventType getEventType() {
        return eventType;
    }
    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
    public Restaurant getRestaurant() {
        return restaurant;
    }
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    public DeliveryAgent getDeliveryAgent(){
        return deliveryAgent;
    }
    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    } 
}
