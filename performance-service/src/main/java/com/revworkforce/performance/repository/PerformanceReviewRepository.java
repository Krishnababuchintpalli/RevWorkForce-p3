package com.revworkforce.performance.repository;

import com.revworkforce.performance.entity.PerformanceReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {

    List<PerformanceReview> findByEmployeeId(Long employeeId);

    List<PerformanceReview> findByEmployeeIdIn(List<Long> employeeIds);

    List<PerformanceReview> findByReviewerId(Long reviewerId);
}
