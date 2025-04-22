package com.fooddelivery.repository;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);
    List<Order> findByRestaurantId(Long restaurantId);
    List<Order> findByRestaurantIdAndStatus(Long restaurantId, String status);
    
    // Using query to explicitly specify the column name
    @Query("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.orderTime BETWEEN :start AND :end")
    List<Order> findByRestaurantIdAndOrderTimeBetween(
        @Param("restaurantId") Long restaurantId, 
        @Param("start") LocalDateTime start, 
        @Param("end") LocalDateTime end
    );
    
    List<Order> findByDeliveryAgentIdAndStatus(Long agentId, String status);
}