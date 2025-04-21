package com.fooddelivery.repository;

import com.fooddelivery.model.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.fooddelivery.model.MenuItem;
import java.util.List;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // You can add custom query methods here if needed
    @Query("SELECT oi.menuItem FROM OrderItem oi GROUP BY oi.menuItem ORDER BY COUNT(oi) DESC")
    List<MenuItem> findTopMostOrderedMenuItems();
}