package com.fooddelivery.menudecorator;

public class LargeSizeDecorator extends MenuDecorator {
    
    public LargeSizeDecorator(MenuComponent decoratedMenuItem) {
        super(decoratedMenuItem);
    }
    
    @Override
    public String getName() {
        return "Large " + decoratedMenuItem.getName();
    }
    
    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription() + " (Large Size)";
    }
    
    @Override
    public double getPrice() {
        // Add 30% to the price for large size
        return decoratedMenuItem.getPrice() * 1.3;
    }
}

