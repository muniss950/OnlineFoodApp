package com.fooddelivery.discount;
import org.springframework.stereotype.Component;

@Component
public class ItemDiscountFactory implements DiscountFactory {
    @Override
    public Discount createDayDiscount(double percentage, int dayOfWeek) {
        // For menu items, we apply the same discount regardless of day
        // This could be enhanced later if needed
        return new ItemPriceDiscount(percentage);
    }
    @Override
    public Discount createPriceDiscount(double percentage) {
        return new ItemPriceDiscount(percentage);
    }
    
    // Inner discount implementation
    private static class ItemPriceDiscount implements Discount {
        private final double percentage;
        
        public ItemPriceDiscount(double percentage) {
            this.percentage = percentage;
        }
        
        @Override
        public double applyDiscount(double price) {
            return price * (1 - (percentage / 100));
        }
    }
}
