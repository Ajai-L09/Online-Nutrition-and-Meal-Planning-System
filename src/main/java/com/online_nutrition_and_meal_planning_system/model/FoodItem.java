package com.online_nutrition_and_meal_planning_system.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FoodItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;
    @Column(nullable = false)
    private String name;
    private int calories;
    private int protein;
    private int carbs;
    private int fats;
}