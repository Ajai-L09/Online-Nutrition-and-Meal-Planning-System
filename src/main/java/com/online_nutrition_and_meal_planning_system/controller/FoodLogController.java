package com.online_nutrition_and_meal_planning_system.controller;

import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.service.FoodLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/log")
public class FoodLogController {
    @Autowired
    private FoodLogService foodLogService;
    @PostMapping
    public ResponseEntity<Log> logMeal(@RequestParam Long userId, @RequestParam Long foodId, @RequestParam String mealType, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Log log = foodLogService.logMeal(userId, foodId, mealType, date);
        return ResponseEntity.ok(log);
    }
    @PutMapping("/eat/{logId}")
    public ResponseEntity<Log> updateStatusToEaten(@PathVariable Long logId) {
        Log log = foodLogService.updateLogStatusToEaten(logId);
        return ResponseEntity.ok(log);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<Log>> getLogsForUser(@PathVariable Long userId) {
        List<Log> logs = foodLogService.getLogsForUser(userId);
        return ResponseEntity.ok(logs);
    }
}