package com.fooddelivery.service;

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
    
    public UserService(UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }
    
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
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
    
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
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