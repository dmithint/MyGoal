package org.mygoal.fitnessapp.backend.repository;

import org.mygoal.fitnessapp.backend.model.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AthleteRepository extends JpaRepository<Athlete, Long> {
    Optional<Athlete> findByEmail(String email);
}
