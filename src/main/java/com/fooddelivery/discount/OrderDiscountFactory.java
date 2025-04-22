package com.fooddelivery.discount;

import org.springframework.stereotype.Component;
import java.util.Calendar;

@Component
public class OrderDiscountFactory implements DiscountFactory {
    
    @Override
    public Discount createPriceDiscount(double percentage) {
        return new PriceDiscount(percentage);
    }
    
    @Override
    public Discount createDayDiscount(double percentage, int dayOfWeek) {
        // Apply special discount on weekends (Saturday and Sunday)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return new WeekendDiscount(percentage);
        } else {
            return new WeekdayDiscount(percentage / 2); // Half discount on weekdays
        }
    }
    
    // Inner discount implementations
    private static class PriceDiscount implements Discount {
        private final double percentage;
        
        public PriceDiscount(double percentage) {
            this.percentage = percentage;
        }
        
        @Override
        public double applyDiscount(double price) {
            return price * (1 - (percentage / 100));
        }
    }
    
    private static class WeekendDiscount implements Discount {
        private final double percentage;
        
        public WeekendDiscount(double percentage) {
            this.percentage = percentage;
        }
        
        @Override
        public double applyDiscount(double price) {
            return price * (1 - (percentage / 100));
        }
    }
    
    private static class WeekdayDiscount implements Discount {
        private final double percentage;
        
        public WeekdayDiscount(double percentage) {
            this.percentage = percentage;
        }
        
        @Override
        public double applyDiscount(double price) {
            return price * (1 - (percentage / 100));
        }
    }
}
