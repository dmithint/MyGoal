package org.mygoal.fitnessapp.backend.repository;

import org.mygoal.fitnessapp.backend.model.TrainingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TrainingFeedbackRepository extends JpaRepository<TrainingFeedback, Long> {
}
