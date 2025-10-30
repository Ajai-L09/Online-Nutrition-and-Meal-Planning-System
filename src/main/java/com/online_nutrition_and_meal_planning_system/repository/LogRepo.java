package com.online_nutrition_and_meal_planning_system.repository;

import com.online_nutrition_and_meal_planning_system.model.Log;
import com.online_nutrition_and_meal_planning_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.time.LocalDate;

@Repository
public interface LogRepo extends JpaRepository<Log, Long> {

    List<Log> findByUser_UserId(Long userId);
    List<Log> findByUser_UserIdAndDate(Long userId, LocalDate date);
    List<Log> findByUserAndStatusAndDateBetween(User user, String status, LocalDate startDate, LocalDate endDate);
    List<Log> findByUser(User user);
}
