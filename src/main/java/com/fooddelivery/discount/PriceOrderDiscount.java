package com.fooddelivery.discount;

public class PriceOrderDiscount implements Discount {
    private double percentage;
    public PriceOrderDiscount(double percentage) {
        this.percentage = percentage;
    }

    public double applyDiscount(double price) {
        double extraDiscount = 0;
        if (price >= 1000 && price < 2000) {
            extraDiscount = 10;
        } else if (price >= 2000 && price < 4000) {
            extraDiscount = 15;
        } else if (price >= 4000) {
            extraDiscount = 20;
        }

        double totalDiscount = this.percentage + extraDiscount;
        if (totalDiscount >= 100) return 0.0;
        return price * (1 - totalDiscount * (1.0 / 100));
    }
}
