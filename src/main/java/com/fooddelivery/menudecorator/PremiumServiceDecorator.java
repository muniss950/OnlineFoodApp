package com.fooddelivery.menudecorator;

public class PremiumServiceDecorator extends MenuDecorator {
    
    public PremiumServiceDecorator(MenuComponent decoratedMenuItem) {
        super(decoratedMenuItem);
    }
    
    @Override
    public String getName() {
        return "Premium " + decoratedMenuItem.getName();
    }
    
    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription() + " (Premium Service)";
    }
    
    @Override
    public double getPrice() {
        // Add 20% premium service charge
        return decoratedMenuItem.getPrice() * 1.2;
    }
}
