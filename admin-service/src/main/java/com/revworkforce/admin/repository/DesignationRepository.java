package com.revworkforce.admin.repository;

import com.revworkforce.admin.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Optional<Designation> findByName(String name);

    boolean existsByName(String name);
}
