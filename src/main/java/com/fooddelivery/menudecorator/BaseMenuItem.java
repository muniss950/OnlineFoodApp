package com.fooddelivery.menudecorator;
import com.fooddelivery.model.MenuItem;
public class BaseMenuItem implements MenuComponent {
    private final MenuItem menuItem;

    public BaseMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public String getName() {
        return menuItem.getName();
    }

    @Override
    public String getDescription() {
        return menuItem.getDescription();
    }

    @Override
    public double getPrice() {
        return menuItem.getPrice();
    }
}

