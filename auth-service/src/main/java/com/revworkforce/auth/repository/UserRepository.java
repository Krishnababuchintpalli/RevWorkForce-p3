package com.revworkforce.auth.repository;

import com.revworkforce.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmployeeId(String employeeId);

    boolean existsByEmployeeId(String employeeId);

    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR u.employeeId LIKE CONCAT('%', :query, '%')")
    List<User> searchByNameOrEmployeeId(@Param("query") String query);

}
