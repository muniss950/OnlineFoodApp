package com.fooddelivery.repository;

import com.fooddelivery.model.MenuItem;
import com.fooddelivery.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    
    @Query("SELECT oi.menuItem FROM OrderItem oi GROUP BY oi.menuItem ORDER BY COUNT(oi) DESC")
    List<MenuItem> findTopMostOrderedMenuItems();
}