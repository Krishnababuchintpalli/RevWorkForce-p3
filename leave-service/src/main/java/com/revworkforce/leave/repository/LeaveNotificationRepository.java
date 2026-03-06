package com.revworkforce.leave.repository;

import com.revworkforce.leave.entity.LeaveNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveNotificationRepository extends JpaRepository<LeaveNotification, Long> {

    List<LeaveNotification> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndReadFlagFalse(Long userId);
}
