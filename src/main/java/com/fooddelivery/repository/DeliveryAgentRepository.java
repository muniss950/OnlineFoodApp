package com.fooddelivery.repository;

import com.fooddelivery.model.DeliveryAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DeliveryAgentRepository extends JpaRepository<DeliveryAgent, Long> {
    List<DeliveryAgent> findByAvailableTrue();
    List<DeliveryAgent> findAll();
    DeliveryAgent findByUsername(String username);
}
