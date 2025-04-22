package com.fooddelivery.service;

import com.fooddelivery.model.DeliveryAgent;
import com.fooddelivery.repository.DeliveryAgentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryAgentService {
    private final DeliveryAgentRepository deliveryAgentRepository;
    
    public DeliveryAgentService(DeliveryAgentRepository deliveryAgentRepository) {
        this.deliveryAgentRepository = deliveryAgentRepository;
    }
    
    public DeliveryAgent addAgent(DeliveryAgent agent) {
        return deliveryAgentRepository.save(agent);
    }
    
    public List<DeliveryAgent> getAvailableAgents() {
        return deliveryAgentRepository.findByAvailableTrue();
    }
    
    public DeliveryAgent updateAvailability(Long agentId, boolean available) {
        DeliveryAgent agent = deliveryAgentRepository.findById(agentId)
            .orElseThrow(() -> new RuntimeException("Agent not found"));
        agent.setAvailable(available);
        return deliveryAgentRepository.save(agent);
    }
    
    public List<DeliveryAgent> getAllAgents() {
        return deliveryAgentRepository.findAll();
    }
    
    public DeliveryAgent authenticate(String username, String password) {
        DeliveryAgent agent = deliveryAgentRepository.findByUsername(username);
        if (agent == null || !agent.getPassword().equals(password)) {
            throw new RuntimeException("Invalid delivery agent credentials");
        }
        return agent;
    }
    
    public DeliveryAgent findAvailableAgent() {
        List<DeliveryAgent> availableAgents = getAvailableAgents();
        if (availableAgents.isEmpty()) {
            throw new RuntimeException("No available delivery agents found");
        }
        // For now, just return the first available agent
        // In a real system, you might want to implement more sophisticated selection logic
        return availableAgents.get(0);
    }
}