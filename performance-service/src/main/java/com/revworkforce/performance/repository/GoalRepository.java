package com.revworkforce.performance.repository;

import com.revworkforce.performance.entity.Goal;
import com.revworkforce.performance.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByEmployeeId(Long employeeId);

    List<Goal> findByEmployeeIdIn(List<Long> employeeIds);

    List<Goal> findByEmployeeIdAndStatus(Long employeeId, GoalStatus status);
}
