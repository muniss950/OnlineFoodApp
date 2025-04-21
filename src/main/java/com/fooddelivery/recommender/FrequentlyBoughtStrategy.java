package com.fooddelivery.recommender;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.repository.OrderRepository;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import com.fooddelivery.model.User;
import com.fooddelivery.model.MenuItem;
import java.util.Set;
public class FrequentlyBoughtStrategy implements RecommendationStrategyUserBased {
    private final OrderRepository orderRepository;

    public FrequentlyBoughtStrategy(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<MenuItem> recommend(User user) {
        List<Order> orders = orderRepository.findByUser(user);
        Set<MenuItem> menuItems = new LinkedHashSet<>();
        for(Order order : orders) {
            for(OrderItem orderItem : order.getOrderItems()) {
                menuItems.add(orderItem.getMenuItem());
            }
        }
        return new ArrayList<>(menuItems);
    }

}
