package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, String status);
    List<Order> findByRestaurantIdAndOrderDate(Long restaurantId, LocalDate date);
    List<Order> findByUserId(Long userId);
    List<Order> findByUserIdAndStatus(Long userId, String status);
    List<Order> findByUser(User user);
    List<Order> findByStatus(String status);
    List<Order> findByDeliveryAgentIdAndStatus(Long agentId, String status);
    List<Order> findByRestaurantIdAndOrderDateBetween(Long restaurantId, LocalDateTime start, LocalDateTime end);
}