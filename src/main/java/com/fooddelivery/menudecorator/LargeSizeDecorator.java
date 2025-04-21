package com.fooddelivery.menudecorator;

public class LargeSizeDecorator extends MenuItemDecorator {
    public LargeSizeDecorator(MenuComponent menuComponent) {
        super(menuComponent);
    }

    @Override
    public String getName() {
        return menuComponent.getName() + " (Large)";
    }

    @Override
    public double getPrice() {
        return menuComponent.getPrice() + menuComponent.getPrice()/4;

    }

    @Override
    public String getDescription() {
        return menuComponent.getDescription() + " .Larger size ";
    }


}

