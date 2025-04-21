package com.fooddelivery.recommender;

import com.fooddelivery.repository.MenuItemRepository;
import java.util.List;
import com.fooddelivery.model.MenuItem;
public class HighDiscountStrategy implements RecommendationStrategy {
    private final MenuItemRepository menuItemRepository;
    public HighDiscountStrategy(MenuItemRepository menuItemRepository){
        this.menuItemRepository = menuItemRepository;
    }
    @Override
    public List<MenuItem> recommend() {
        return menuItemRepository.findTop5ByOrderByDiscountPercentageDesc();
    }
}
