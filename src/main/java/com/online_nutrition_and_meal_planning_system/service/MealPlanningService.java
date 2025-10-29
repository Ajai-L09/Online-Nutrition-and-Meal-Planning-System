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
public class MealPlanningService {
    @Autowired
    private LogRepo logRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FoodItemRepo foodItemRepo;
    public Log createPlan(Long userId, Long foodId, LocalDate date, String mealType) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        FoodItem foodItem = foodItemRepo.findById(foodId).orElseThrow(()->new RuntimeException("FoodItem not found"));
        Log newLog = new Log();
        newLog.setUser(user);
        newLog.setFoodItem(foodItem);
        newLog.setDate(date);
        newLog.setMealType(mealType);
        newLog.setStatus("Planned");
        return logRepo.save(newLog);
    }

    public List<Log> getPlanForUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        return logRepo.findByUser(user).stream().filter(log -> "Planned".equals(log.getStatus())).collect(Collectors.toList());
    }
}