package com.fooddelivery.discount;

public class DayDiscountItem implements Discount {
    private  double percentage;
    private int day;
    DayDiscountItem(double percentage, int day) {
        this.percentage = percentage;
        this.day = day;
    }
    public double applyDiscount(double price) {
        double extraDiscount = 0;
        if(this.day == 6){
            extraDiscount = 5;   
        }
        else if(this.day == 5){
            extraDiscount = 3;
        }
        double totalDiscount = this.percentage + extraDiscount;
        if(totalDiscount >= 100) return 0.0;
        return price* (1-totalDiscount*(1.0/100));
        
    }
    

}
