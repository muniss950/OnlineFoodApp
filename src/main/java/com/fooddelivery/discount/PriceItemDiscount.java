package com.fooddelivery.discount;

public class PriceItemDiscount implements Discount {
    private double percentage;
    public PriceItemDiscount(double percentage) {
        this.percentage = percentage;
    }

    public double applyDiscount(double price) {
        double extraDiscount = 0;
        if (price >= 100 && price < 300) {
            extraDiscount = 6;
        } else if (price >= 300 && price < 1000) {
            extraDiscount = 15;
        } else if (price >= 1000) {
            extraDiscount = 20;
        }

        double totalDiscount = this.percentage + extraDiscount;
        if (totalDiscount >= 100) return 0.0;
        return price * (1 - totalDiscount * (1.0 / 100));
    }
}
