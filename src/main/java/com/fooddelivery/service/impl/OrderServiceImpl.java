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
    public Order createOrder(User customer, Restaurant restaurant, List<OrderItem> items) {
        Order order = new Order();
        order.setUser(customer);
        order.setRestaurant(restaurant);
        order.setStatus("PENDING");
        order.setOrderTime(LocalDateTime.now());
        
        // Initialize numeric fields to avoid null issues
        order.setDeliveryFee(0.0);
        order.setTaxRate(0.0);
        order.setDiscount(0.0);
        order.setSubtotal(0.0);
        order.setTax(0.0);
        order.setTotal(0.0);
        order.setPremiumDelivery(false);

        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        Double discountedTotal = 0.0;
        Double total = 0.0;

        for (OrderItem item : items) {
            MenuComponent menuComponent = new BaseMenuItem(item.getMenuItem());
            if (item.isLargeSize()) {
                menuComponent = new LargeSizeDecorator(menuComponent);
            }
            if (item.isPremium()) {
                menuComponent = new PremiumServiceDecorator(menuComponent);
            }

            Discount itemDiscount = itemDiscountFactory.createPriceDiscount(item.getMenuItem().getDiscountPercentage());
            Double basePrice = menuComponent.getPrice();
            Double discountedPrice = itemDiscount.applyDiscount(basePrice);
            
            item.setDiscountedPrice(discountedPrice);
            item.setName(menuComponent.getName());
            item.setDescription(menuComponent.getDescription());
            discountedTotal += discountedPrice * item.getQuantity();
            total += basePrice * item.getQuantity();
        }

        Discount orderDiscount = orderDiscountFactory.createDayDiscount(5, dayOfWeek);
        Double finalTotal = orderDiscount.applyDiscount(discountedTotal);

        order.setTotalAmount(total);
        order.setDiscount(total - finalTotal);
        order.setTotal(finalTotal); // Set the final total after discount
        order.setSubtotal(total);   // Set the subtotal before discounts

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        order.setItems(items);
        
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
        
        order.setDeliveryAgent(agent);
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
        
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        
        OrderEvent orderEvent = new OrderEvent(savedOrder, notificationService);
        orderEvent.notifyObservers(EventType.valueOf("ORDER_" + status.toUpperCase()));
        
        return savedOrder;
    }

    @Override
    public List<Order> getRestaurantOrdersByDate(Long restaurantId, LocalDate date) {
        return orderRepository.findByRestaurantIdAndOrderTimeBetween(
            restaurantId, 
            date.atStartOfDay(), 
            date.plusDays(1).atStartOfDay()
        );
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
        return orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
} 