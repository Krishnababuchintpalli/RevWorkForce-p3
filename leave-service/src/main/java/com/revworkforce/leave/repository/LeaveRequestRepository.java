package com.revworkforce.leave.repository;

import com.revworkforce.leave.entity.LeaveRequest;
import com.revworkforce.leave.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByEmployeeIdIn(List<Long> employeeIds);

    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.employeeId = :employeeId " +
           "AND l.status = :status AND YEAR(l.startDate) = :year")
    long countByEmployeeIdAndStatusAndYear(
            @Param("employeeId") Long employeeId,
            @Param("status") LeaveStatus status,
            @Param("year") int year
    );

    List<LeaveRequest> findByStatus(LeaveStatus status);
}
