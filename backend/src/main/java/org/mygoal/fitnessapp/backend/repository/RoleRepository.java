package org.mygoal.fitnessapp.backend.repository;

import org.mygoal.fitnessapp.backend.model.Role;
import org.mygoal.fitnessapp.backend.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
