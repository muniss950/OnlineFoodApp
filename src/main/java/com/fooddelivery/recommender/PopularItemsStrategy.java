package com.fooddelivery.recommender;

import com.fooddelivery.model.MenuItem;

import com.fooddelivery.repository.OrderItemRepository;
import java.util.List;
public class PopularItemsStrategy implements RecommendationStrategy {
    private final OrderItemRepository orderItemRepository;
    public PopularItemsStrategy(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override 
    public List<MenuItem> recommend(){
        return orderItemRepository.findTopMostOrderedMenuItems();
    }
}
