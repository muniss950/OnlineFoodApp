package com.fooddelivery.service;

import com.fooddelivery.model.MenuItem;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<MenuItem> getAllMenuItems();
    List<MenuItem> getAvailableMenuItems();
    MenuItem addMenuItem(MenuItem menuItem);
    Optional<MenuItem> getMenuItemById(Long id);
    MenuItem updateMenuItem(Long id, MenuItem menuItem);
    void deleteMenuItem(Long id);
    List<MenuItem> getRestaurantMenuItems(Long restaurantId);
    MenuItem saveMenuItem(MenuItem menuItem);
    List<MenuItem> getAvailableMenuItems(Long restaurantId);
    Optional<MenuItem> findById(Long id);
}