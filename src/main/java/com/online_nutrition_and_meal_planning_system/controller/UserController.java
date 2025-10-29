package com.online_nutrition_and_meal_planning_system.controller;

import com.online_nutrition_and_meal_planning_system.model.User;
import com.online_nutrition_and_meal_planning_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String role, @RequestParam Integer dailyCalorieGoal) {
        User user = userService.register(username, password,role,dailyCalorieGoal);
        return ResponseEntity.ok(user);
    }
    @PutMapping("/profile/{userId}")
    public ResponseEntity<User> updateProfile(@PathVariable Long userId, @RequestParam Integer dailyCalorieGoal) {
        User updatedUser = userService.updateProfile(userId,dailyCalorieGoal);
        return ResponseEntity.ok(updatedUser);
    }
}