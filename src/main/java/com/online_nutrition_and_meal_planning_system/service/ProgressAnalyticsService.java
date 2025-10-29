package com.online_nutrition_and_meal_planning_system.service;

import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.model.User;
import com.online_nutrition_and_meal_planning_system.repository.LogRepo;
import com.online_nutrition_and_meal_planning_system.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProgressAnalyticsService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private LogRepo logRepo;
    public Map<String, Integer> getNutritionReport(Long userId, LocalDate startDate, LocalDate endDate) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        List<Log> logs = logRepo.findByUserAndStatusAndDateBetween(user, "Eaten", startDate, endDate);
        int totCalories = 0;
        int totProtein = 0;
        int totCarbs = 0;
        int totFat = 0;
        for (Log log : logs) {
            if (log.getFoodItem() != null) {
                totCalories += log.getFoodItem().getCalories();
                totProtein += log.getFoodItem().getProtein();
                totCarbs += log.getFoodItem().getCarbs();
                totFat += log.getFoodItem().getFats();
            }
        }
        Map<String, Integer> report = new HashMap<>();
        report.put("totCalories",totCalories);
        report.put("totProtein", totProtein);
        report.put("totCarbs", totCarbs);
        report.put("totFat", totFat);
        return report;
    }
}