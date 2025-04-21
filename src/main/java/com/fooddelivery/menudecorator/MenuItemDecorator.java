package com.fooddelivery.menudecorator;

public abstract class MenuItemDecorator implements MenuComponent {
    protected MenuComponent menuComponent;

    public MenuItemDecorator(MenuComponent menuComponent) {
        this.menuComponent = menuComponent;
    }

    @Override
    public String getName() {
        return menuComponent.getName();
    }

    @Override
    public String getDescription() {
        return menuComponent.getDescription();
    }

    @Override
    public double getPrice() {
        return menuComponent.getPrice();
    }
}
