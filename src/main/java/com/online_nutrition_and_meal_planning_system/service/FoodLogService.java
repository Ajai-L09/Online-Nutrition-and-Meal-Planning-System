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

@Service
public class FoodLogService {
    @Autowired
    private LogRepo logRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private FoodItemRepo foodItemRepo;

    public Log logMeal(Long userId, Long foodId, String mealType, LocalDate date) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found for ID: " + userId));
        FoodItem foodItem = foodItemRepo.findById(foodId).orElseThrow(()-> new RuntimeException("FoodItem not found for ID: " + foodId));
        Log log = new Log();
        log.setUser(user);
        log.setFoodItem(foodItem);
        log.setDate(date);
        log.setMealType(mealType);
        log.setStatus("Eaten");
        return logRepo.save(log);
    }
    public Log updateLogStatusToEaten(Long logId) {
        Log log = logRepo.findById(logId).orElseThrow(()->new RuntimeException("Log not found for ID: " + logId));
        log.setStatus("Eaten");
        return logRepo.save(log);
    }
    public List<Log> getLogsForUser(Long userId) {
        return logRepo.findByUser_UserId(userId);
    }
}
