package com.fooddelivery.discount;
import org.springframework.stereotype.Component;

@Component
public class OrderDiscountFactory implements DiscountFactory {
    @Override
    public Discount createDayDiscount(double percentage, int day){
        return new DayDiscountOrder(percentage,day);

    }
    @Override
    public Discount createPriceDiscount(double percentage) {
        return new PriceOrderDiscount(percentage);
    }
}
