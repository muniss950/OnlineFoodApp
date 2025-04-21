package com.fooddelivery.menudecorator;

public class PremiumServiceDecorator extends MenuItemDecorator {
    public PremiumServiceDecorator(MenuComponent menuComponent) {
        super(menuComponent);
    }

    @Override
    public String getName() {
        return "Premium " + menuComponent.getName() + " Premium";
    }

    @Override
    public double getPrice() {
        return menuComponent.getPrice() + menuComponent.getPrice()/3; // add-on price
    }

    @Override
    public String getDescription() {
        return menuComponent.getDescription() + " .made by Our best Chefs "; // add-on price
    }



}
