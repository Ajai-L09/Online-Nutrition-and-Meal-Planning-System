package com.online_nutrition_and_meal_planning_system.repository;

import com.online_nutrition_and_meal_planning_system.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {
    List<Log> findByUserId(Long userId);
    List<Log> findByUserIdAndDate(Long userId, LocalDate date);
}