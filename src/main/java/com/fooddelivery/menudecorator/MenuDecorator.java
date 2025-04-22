package com.fooddelivery.menudecorator;

public abstract class MenuDecorator implements MenuComponent {
    protected MenuComponent decoratedMenuItem;
    
    public MenuDecorator(MenuComponent decoratedMenuItem) {
        this.decoratedMenuItem = decoratedMenuItem;
    }
    
    @Override
    public String getName() {
        return decoratedMenuItem.getName();
    }
    
    @Override
    public String getDescription() {
        return decoratedMenuItem.getDescription();
    }
    
    @Override
    public double getPrice() {
        return decoratedMenuItem.getPrice();
    }
} 