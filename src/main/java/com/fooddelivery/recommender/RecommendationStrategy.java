package com.fooddelivery.recommender;

import com.fooddelivery.model.MenuItem;

import java.util.List;


public interface RecommendationStrategy {
    List<MenuItem> recommend();
    
}