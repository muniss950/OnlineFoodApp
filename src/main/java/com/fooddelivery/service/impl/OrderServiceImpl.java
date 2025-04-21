package com.fooddelivery.service.impl;

import com.fooddelivery.model.*;
import com.fooddelivery.repository.*;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.NotificationService;
import com.fooddelivery.discount.*;
import com.fooddelivery.notifications.EventType;
import com.fooddelivery.notifications.OrderEvent;
import com.fooddelivery.menudecorator.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryAgentRepository deliveryAgentRepository;
    private final DiscountFactory orderDiscountFactory;
    private final DiscountFactory itemDiscountFactory;
    private final NotificationService notificationService;

    public OrderServiceImpl(OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository,
                          DeliveryAgentRepository deliveryAgentRepository,
                          OrderDiscountFactory orderDiscountFactory,
                          ItemDiscountFactory itemDiscountFactory,
                          NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.deliveryAgentRepository = deliveryAgentRepository;
        this.orderDiscountFactory = orderDiscountFactory;
        this.itemDiscountFactory = itemDiscountFactory;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public Order createOrder(User user, Restaurant restaurant, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setStatus("PENDING");
        order.setOrderDate(LocalDateTime.now());

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        double discountedTotal = 0.0;
        double total = 0.0;

        for (OrderItem item : items) {
            MenuComponent menuComponent = new BaseMenuItem(item.getMenuItem());
            if (item.isLargeSize()) {
                menuComponent = new LargeSizeDecorator(menuComponent);
            }
            if (item.isPremium()) {
                menuComponent = new PremiumServiceDecorator(menuComponent);
            }

            Discount itemDiscount = itemDiscountFactory.createPriceDiscount(item.getMenuItem().getDiscountPercentage());
            double basePrice = menuComponent.getPrice();
            double discountedPrice = itemDiscount.applyDiscount(basePrice);
            
            item.setDiscountedPrice(discountedPrice);
            item.setName(menuComponent.getName());
            item.setDescription(menuComponent.getDescription());
            discountedTotal += discountedPrice * item.getQuantity();
            total += basePrice * item.getQuantity();
        }

        Discount orderDiscount = orderDiscountFactory.createDayDiscount(5, dayOfWeek);
        double finalTotal = orderDiscount.applyDiscount(discountedTotal);

        order.setTotalPrice(total);
        order.setTotalDiscountedPrice(finalTotal);

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
        orderEvent.notifyObservers(EventType.ORDER_PLACED);
        return savedOrder;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public List<Order> getOrdersByDeliveryAgentAndStatus(Long agentId, String status) {
        return orderRepository.findByDeliveryAgentIdAndStatus(agentId, status);
    }

    @Override
    @Transactional
    public Order assignDeliveryAgent(Long orderId, Long agentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Delivery agent not found"));

        if (!agent.isAvailable()) {
            throw new RuntimeException("Delivery agent is not available");
        }

        order.setDeliveryAgent(agent);
        order.setStatus("ASSIGNED");

        Order savedOrder = orderRepository.save(order);
        OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
        orderEvent.notifyObservers(EventType.ORDER_ASSIGNED);
        return savedOrder;
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        
        String oldStatus = order.getStatus();
        order.setStatus(status);
        
        Order savedOrder = orderRepository.save(order);
        
        if (oldStatus.equals("ASSIGNED") && status.equals("PREPARING")) {
            OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
            orderEvent.notifyObservers(EventType.ORDER_REJECTED);
        } else if (status.equals("ON_THE_WAY")) {
            OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
            orderEvent.notifyObservers(EventType.ORDER_ACCEPTED);
        } else if (status.equals("DELIVERED")) {
            OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
            orderEvent.notifyObservers(EventType.ORDER_DELIVERED);
        }
        
        return savedOrder;
    }

    @Override
    public List<Order> getRestaurantOrdersByDate(Long restaurantId, LocalDate date) {
        return orderRepository.findByRestaurantIdAndOrderDateBetween(restaurantId,
                date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    @Override
    public List<Order> getRestaurantOrdersByStatus(Long restaurantId, String status) {
        return orderRepository.findByRestaurantIdAndStatus(restaurantId, status);
    }

    @Override
    public List<Order> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
} 