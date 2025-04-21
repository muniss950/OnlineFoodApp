package com.fooddelivery.discount;
import org.springframework.stereotype.Component;

@Component
public class ItemDiscountFactory implements DiscountFactory {
    @Override
    public Discount createDayDiscount(double percentage, int day){
        return new DayDiscountItem(percentage,day);

    }
    @Override
    public Discount createPriceDiscount(double percentage) {
        return new PriceItemDiscount(percentage);
    }
}
