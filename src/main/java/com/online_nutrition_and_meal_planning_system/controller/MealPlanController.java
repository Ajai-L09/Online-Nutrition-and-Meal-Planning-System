package com.online_nutrition_and_meal_planning_system.controller;

import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.service.MealPlanningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
public class MealPlanController {
    @Autowired
    private MealPlanningService mealPlanningService;
    @PostMapping
    public ResponseEntity<Log> createPlanEntry(@RequestParam Long userId, @RequestParam Long foodId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam String mealType) {
        Log log = mealPlanningService.createPlan(userId,foodId, date, mealType);
        return ResponseEntity.ok(log);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<Log>> getPlanForUser(@PathVariable Long userId) {
        List<Log> plan = mealPlanningService.getPlanForUser(userId);
        return ResponseEntity.ok(plan);
    }
}