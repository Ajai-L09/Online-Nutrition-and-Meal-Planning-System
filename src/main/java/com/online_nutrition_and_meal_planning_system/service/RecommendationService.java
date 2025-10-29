package com.online_nutrition_and_meal_planning_system.service;

import com.online_nutrition_and_meal_planning_system.model.FoodItem;
import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.model.User;
import com.online_nutrition_and_meal_planning_system.repository.FoodItemRepo;
import com.online_nutrition_and_meal_planning_system.repository.LogRepo;
import com.online_nutrition_and_meal_planning_system.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LogRepo logRepo;
    @Autowired
    private FoodItemRepo foodItemRepo;
    public List<FoodItem> getRecommendations(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        LocalDate today = LocalDate.now();
        List<Log> todayLogs = logRepo.findByUserAndStatusAndDateBetween(user, "Eaten", today, today);
        int caloriesEaten = todayLogs.stream().mapToInt(log->log.getFoodItem().getCalories()).sum();
        Integer dailyGoal = user.getDailyCalorieGoal();
        if (dailyGoal == null) {
            dailyGoal = 2000;
        }
        int remainingCalories = dailyGoal -caloriesEaten;
        List<FoodItem> allFoods = foodItemRepo.findAll();
        return allFoods.stream().filter(food->food.getCalories()<= remainingCalories && food.getCalories()> 0).limit(10).collect(Collectors.toList());
    }
}