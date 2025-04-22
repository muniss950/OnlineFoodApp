package com.fooddelivery.factory.impl;

import com.fooddelivery.factory.UserFactory;
import com.fooddelivery.model.User;
import org.springframework.stereotype.Component;

/**
 * Factory pattern - Concrete Factory
 * This class creates Customer users with appropriate default settings
 */
@Component
public class CustomerFactory implements UserFactory {
    
    @Override
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("CUSTOMER");
        user.setActive(true);
        return user;
    }
    
    @Override
    public User createUser(String username, String password, String email, String phone, String address) {
        User user = createUser(username, password, email);
        user.setPhone(phone);
        user.setAddress(address);
        return user;
    }
} 