package com.fooddelivery.recommender;
import com.fooddelivery.model.User;
import java.util.List;
import com.fooddelivery.model.MenuItem;
public interface RecommendationStrategyUserBased {
    List<MenuItem> recommend(User user);
}
