package com.fooddelivery.factory;

import com.fooddelivery.factory.impl.AdminFactory;
import com.fooddelivery.factory.impl.CustomerFactory;
import com.fooddelivery.factory.impl.RestaurantFactory;
import com.fooddelivery.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory pattern - Factory Manager
 * This class selects the appropriate factory based on the user role
 */
@Component
public class UserFactoryManager {
    
    private final CustomerFactory customerFactory;
    private final RestaurantFactory restaurantFactory;
    private final AdminFactory adminFactory;
    
    @Autowired
    public UserFactoryManager(CustomerFactory customerFactory, 
                             RestaurantFactory restaurantFactory,
                             AdminFactory adminFactory) {
        this.customerFactory = customerFactory;
        this.restaurantFactory = restaurantFactory;
        this.adminFactory = adminFactory;
    }
    
    /**
     * Get the appropriate factory for the specified role
     * @param role The user role (CUSTOMER, RESTAURANT, ADMIN)
     * @return The appropriate UserFactory implementation
     */
    public UserFactory getFactory(String role) {
        switch (role.toUpperCase()) {
            case "CUSTOMER":
                return customerFactory;
            case "RESTAURANT":
                return restaurantFactory;
            case "ADMIN":
                return adminFactory;
            default:
                throw new IllegalArgumentException("Unknown user role: " + role);
        }
    }
    
    /**
     * Create a new user with the specified role
     * @param role The user role (CUSTOMER, RESTAURANT, ADMIN)
     * @param username The username for the new user
     * @param password The password for the new user
     * @param email The email for the new user
     * @return A new user with the appropriate role and default settings
     */
    public User createUser(String role, String username, String password, String email) {
        return getFactory(role).createUser(username, password, email);
    }
    
    /**
     * Create a new user with the specified role and additional properties
     * @param role The user role (CUSTOMER, RESTAURANT, ADMIN)
     * @param username The username for the new user
     * @param password The password for the new user
     * @param email The email for the new user
     * @param phone The phone number for the new user
     * @param address The address for the new user
     * @return A new user with the appropriate role and specified properties
     */
    public User createUser(String role, String username, String password, String email, 
                          String phone, String address) {
        return getFactory(role).createUser(username, password, email, phone, address);
    }
} 