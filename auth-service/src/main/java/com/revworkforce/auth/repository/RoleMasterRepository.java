package com.revworkforce.auth.repository;

import com.revworkforce.auth.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer> {

    Optional<RoleMaster> findByName(String name);
}
