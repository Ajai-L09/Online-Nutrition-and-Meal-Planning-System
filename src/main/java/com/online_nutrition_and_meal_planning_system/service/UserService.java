package com.online_nutrition_and_meal_planning_system.service;

import com.online_nutrition_and_meal_planning_system.model.User;
import com.online_nutrition_and_meal_planning_system.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User register(String username, String password, String role, Integer dailyCalorieGoal) {
        if (userRepo.findByUsername(username).isPresent()) {


            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRole(role != null?role : "USER");
        user.setDailyCalorieGoal(dailyCalorieGoal);
        return userRepo.save(user);
    }
    public User updateProfile(Long userId, Integer dailyCalorieGoal) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        user.setDailyCalorieGoal(dailyCalorieGoal);
        return userRepo.save(user);
    }
    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(()->new RuntimeException("User not found"));
    }
}