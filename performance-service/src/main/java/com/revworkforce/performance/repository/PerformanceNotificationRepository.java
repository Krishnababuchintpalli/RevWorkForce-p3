package com.revworkforce.performance.repository;

import com.revworkforce.performance.entity.PerformanceNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerformanceNotificationRepository extends JpaRepository<PerformanceNotification, Long> {

    List<PerformanceNotification> findByUserIdOrderByCreatedAtDesc(Long userId);
}
