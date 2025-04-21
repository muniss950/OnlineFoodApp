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

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private EventType type;

    @Column(name = "`read`")
    private boolean read;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private ObserverType observerType;

    @ManyToOne
    @JoinColumn(name = "delivery_agent_id")
    private DeliveryAgent deliveryAgent;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private  Order order;

    public Notification() {
        this.createdAt = LocalDateTime.now();
        this.read = false;
    }
    public Notification(EventType type , User user, Order order){
        this.type = type;
        this.user = user;
        this.order = order;
        this.createdAt = LocalDateTime.now();
    }
    public Long getId() { return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getMessage() {return message; }
    public void setMessage(String message) {this.message = message ;}
    public EventType getType() {return type;}
    public void setType(EventType type){
        this.type = type;
    }
    public void setRead(boolean read) {
        this.read = read;
    }
    public boolean isRead() {
        return read;
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
    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        this.deliveryAgent = deliveryAgent;
    }
    public DeliveryAgent getDeliveryAgent(){
        return deliveryAgent;
    } 








}
