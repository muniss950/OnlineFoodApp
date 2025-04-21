package com.fooddelivery.service;
import com.fooddelivery.repository.OrderRepository;
import org.springframework.stereotype.Service;

import com.fooddelivery.model.Order;
import com.fooddelivery.recommender.FrequentlyBoughtStrategy;
import com.fooddelivery.recommender.HighDiscountStrategy;
import com.fooddelivery.recommender.PopularItemsStrategy;
import com.fooddelivery.recommender.RecommenderContext;
import com.fooddelivery.repository.MenuItemRepository;
import com.fooddelivery.repository.OrderItemRepository;
import java.util.List;
import com.fooddelivery.model.MenuItem;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.model.User;
@Service
public class RecommendationService {

    private final MenuItemRepository menuItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    private RecommendationService(MenuItemRepository menuItemRepository, OrderItemRepository orderItemRepository,OrderRepository orderRepository) {
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }


    public List<MenuItem> recommendBasicItems(String strategyType) {
        RecommenderContext context = new RecommenderContext();
       
        switch(strategyType.toLowerCase()) {
            case "popular":
                context.setBasicStrategy(new PopularItemsStrategy(orderItemRepository));
                break;
            case "discount":
                context.setBasicStrategy(new HighDiscountStrategy(menuItemRepository));
                break;
            default:
                context.setBasicStrategy(new PopularItemsStrategy(orderItemRepository));
                

        }
        return context.getBasicRecommendations();
    }
    public List<MenuItem> recommendUserBasedItems(String strategyType, User user) {
        RecommenderContext context = new RecommenderContext();
       
        switch(strategyType.toLowerCase()) {
            case "frequent":
                context.setUserBasedStrategy(new FrequentlyBoughtStrategy(orderRepository));
                break;
            default:
                context.setUserBasedStrategy(new FrequentlyBoughtStrategy(orderRepository));
                

        }
        return context.getUserBasedRecommendations(user);
    }
}
