package com.fooddelivery.service;

import com.fooddelivery.factory.UserFactoryManager;
import com.fooddelivery.model.User;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.repository.UserRepository;
import com.fooddelivery.repository.RestaurantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserFactoryManager userFactoryManager;
    
    public UserService(UserRepository userRepository, RestaurantRepository restaurantRepository, UserFactoryManager userFactoryManager) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.userFactoryManager = userFactoryManager;
    }
    
    @Transactional
    public User registerUser(String username, String password, String email, String role) {
        // Use the UserFactoryManager to create a user with the appropriate role
        User user = userFactoryManager.createUser(role, username, password, email);
        return userRepository.save(user);
    }
    
    @Transactional
    public User registerUser(String username, String password, String email, String role, 
                            String phone, String address) {
        // Use the UserFactoryManager to create a user with the appropriate role and additional properties
        User user = userFactoryManager.createUser(role, username, password, email, phone, address);
        return userRepository.save(user);
    }
    
    @Transactional
    public User registerRestaurantUser(User user, Restaurant restaurant) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setRestaurant(restaurant);
        user.setRole("RESTAURANT");
        return userRepository.save(user);
    }
    
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(user -> password.equals(user.getPassword()));
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User getUserByRestaurant(Restaurant restaurant) {
        return userRepository.findByRestaurant(restaurant);
    }

    @Transactional
    public boolean updatePassword(User user, String currentPassword, String newPassword, String confirmPassword) {
        if (!currentPassword.equals(user.getPassword())) {
            return false;
        }
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}