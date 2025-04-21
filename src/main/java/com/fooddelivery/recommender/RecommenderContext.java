package com.fooddelivery.recommender;
import java.util.List;
import com.fooddelivery.model.User;
import com.fooddelivery.model.MenuItem;
public class RecommenderContext {
    private RecommendationStrategy basicstrategy;
    private RecommendationStrategyUserBased userBasedStrategy;
    public void setBasicStrategy(RecommendationStrategy basicstrategy ) {
        this.basicstrategy =basicstrategy ; 
    }
    public void setUserBasedStrategy(RecommendationStrategyUserBased userBasedStrategy){
        this.userBasedStrategy = userBasedStrategy;
    }


    public List<MenuItem> getBasicRecommendations(){
        return basicstrategy.recommend();
    }
    public List<MenuItem> getUserBasedRecommendations(User user){
        return userBasedStrategy.recommend(user);
    }


}
