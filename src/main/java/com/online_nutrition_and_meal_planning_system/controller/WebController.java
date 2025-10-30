package com.online_nutrition_and_meal_planning_system.controller;

import com.online_nutrition_and_meal_planning_system.model.FoodItem;
import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.model.User;
import com.online_nutrition_and_meal_planning_system.repository.FoodItemRepo;
import com.online_nutrition_and_meal_planning_system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {
    @Autowired private UserService userService;
    @Autowired private MealPlanningService mealPlanningService;
    @Autowired private FoodLogService foodLogService;
    @Autowired private RecommendationService recommendationService;
    @Autowired private ProgressAnalyticsService progressAnalyticsService;
    @Autowired private FoodItemRepo foodItemRepo;
    private User getLoggedInUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        try {
            return userService.findByUsername(authentication.getName());
        } catch (RuntimeException e) {
            System.err.println("Error finding logged in user: " + e.getMessage());
            return null;
        }
    }
    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";   
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String handleRegistration(User user) {
        try {
            userService.register(
                    user.getUsername(),
                    user.getPasswordHash(),
                    "USER",
                    user.getDailyCalorieGoal()
            );
            return "redirect:/login?registered=true";
        } catch (RuntimeException e) {
            return "redirect:/register?error=" + e.getMessage();
        }
    }
    @GetMapping("/dashboard")
    public String dashboardPage(Authentication authentication, Model model) {
        User user = getLoggedInUser(authentication);
        if (user == null) return "redirect:/login";

        List<FoodItem> allFoods = foodItemRepo.findAll();
        List<Log> mealPlan = mealPlanningService.getPlanForUser(user.getUserId());
        List<FoodItem> recommendations = recommendationService.getRecommendations(user.getUserId());

        List<Log> eatenToday = foodLogService.getLogsForUser(user.getUserId()).stream()
                .filter(log -> "Eaten".equals(log.getStatus()) && log.getDate().isEqual(LocalDate.now()))
                .toList();
        model.addAttribute("currentUser", user);
        model.addAttribute("allFoods", allFoods);
        model.addAttribute("mealPlan", mealPlan);
        model.addAttribute("eatenToday", eatenToday);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("today", LocalDate.now());

        return "dashboard";
    }
    @PostMapping("/log-meal")
    public String handleLogMeal(Authentication authentication, @RequestParam Long foodId, @RequestParam String mealType,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = getLoggedInUser(authentication);
        if (user != null) foodLogService.logMeal(user.getUserId(), foodId, mealType, date);
        return "redirect:/dashboard";
    }

    @PostMapping("/plan-meal")
    public String handlePlanMeal(Authentication authentication, @RequestParam Long foodId, @RequestParam String mealType, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        User user = getLoggedInUser(authentication);
        if (user != null) mealPlanningService.createPlan(user.getUserId(), foodId, date, mealType);
        return "redirect:/dashboard";
    }
    @PostMapping("/eat-log/{logId}")
    public String handleEatPlannedMeal(@PathVariable Long logId, Authentication authentication) {
        User user = getLoggedInUser(authentication);
        if (user != null) foodLogService.updateLogStatusToEaten(logId);
        return "redirect:/dashboard";
    }
    @GetMapping("/report")
    public String reportPage(Authentication authentication,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             Model model) {
        User user = getLoggedInUser(authentication);
        if (user == null) return "redirect:/login";
        model.addAttribute("currentUser", user);
        model.addAttribute("startDate", startDate != null ? startDate : LocalDate.now().minusWeeks(1));
        model.addAttribute("endDate", endDate != null ? endDate : LocalDate.now());

        if (startDate != null && endDate != null) {
            Map<String, Integer> reportData = progressAnalyticsService.getNutritionReport(user.getUserId(), startDate, endDate);
            model.addAttribute("reportData", reportData);
        }
        return "report";
    }
    @PostMapping("/report")
    public String handleGenerateReport(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return "redirect:/report?startDate=" + startDate + "&endDate=" + endDate;
    }
    @GetMapping("/profile")
    public String profilePage(Authentication authentication, Model model) {
        User user = getLoggedInUser(authentication);
        if (user == null) return "redirect:/login";
        model.addAttribute("currentUser", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String handleUpdateProfile(Authentication authentication, @RequestParam Integer dailyCalorieGoal) {
        User user = getLoggedInUser(authentication);
        if (user != null) {
            userService.updateProfile(user.getUserId(), dailyCalorieGoal);
            return "redirect:/profile?updated=true";
        }
        return "redirect:/login";
    }
}
