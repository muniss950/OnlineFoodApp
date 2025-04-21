package com.fooddelivery.discount;

public interface DiscountFactory {
    Discount createDayDiscount(double percentage , int day);
    Discount createPriceDiscount(double percentage);
    
}