package com.fooddelivery.factory;

import com.fooddelivery.model.User;

/**
 * Factory pattern - Abstract Factory
 * This interface defines the contract for creating different types of users
 */
public interface UserFactory {
    
    /**
     * Create a new user with the specified role
     * @param username The username for the new user
     * @param password The password for the new user
     * @param email The email for the new user
     * @return A new user with the appropriate role and default settings
     */
    User createUser(String username, String password, String email);
    
    /**
     * Create a new user with the specified role and additional properties
     * @param username The username for the new user
     * @param password The password for the new user
     * @param email The email for the new user
     * @param phone The phone number for the new user
     * @param address The address for the new user
     * @return A new user with the appropriate role and specified properties
     */
    User createUser(String username, String password, String email, String phone, String address);
} 