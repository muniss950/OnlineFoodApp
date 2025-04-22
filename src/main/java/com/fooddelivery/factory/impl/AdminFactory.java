package com.fooddelivery.factory.impl;

import com.fooddelivery.factory.UserFactory;
import com.fooddelivery.model.User;
import org.springframework.stereotype.Component;

/**
 * Factory pattern - Concrete Factory
 * This class creates Admin users with appropriate default settings
 */
@Component
public class AdminFactory implements UserFactory {
    
    @Override
    public User createUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole("ADMIN");
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