package com.fooddelivery.discount;

public interface DiscountFactory {
    Discount createPriceDiscount(double percentage);
    Discount createDayDiscount(double percentage, int dayOfWeek);
}